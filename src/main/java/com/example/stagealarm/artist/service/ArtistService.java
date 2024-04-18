package com.example.stagealarm.artist.service;

import com.example.stagealarm.artist.dto.ArtistRequestDto;
import com.example.stagealarm.artist.dto.ArtistResponseDto;
import com.example.stagealarm.artist.dto.ShowUploadArtistDto;
import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.dto.ArtistDto;
import com.example.stagealarm.artist.entity.ArtistGenre;
import com.example.stagealarm.artist.repo.ArtistGenreRepo;
import com.example.stagealarm.artist.repo.ArtistRepository;
import com.example.stagealarm.artist.repo.QArtistRepo;
import com.example.stagealarm.awsS3.S3FileService;
import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.genre.entity.Genre;
import com.example.stagealarm.genre.repo.GenreRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final QArtistRepo qArtistRepo;
    private final GenreRepository genreRepository;
    private final ArtistGenreRepo artistGenreRepo;
    private final AuthenticationFacade facade;
    private final S3FileService s3FileService;
    private final EntityManager entityManager;


    // 아티스트 저장
    @Transactional
    public ArtistResponseDto join(ArtistRequestDto dto, MultipartFile file) {
        String imgUrl = "";
        if (file != null && !file.isEmpty()) {
            // 파일 처리 로직
            imgUrl = s3FileService.uploadIntoS3("/artistImg", file);
        }

        Artist artist = Artist.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .gender(dto.getGender())
                .profileImg(imgUrl)
                .genres(new ArrayList<>())
                .build();
        artist = artistRepository.save(artist);
        Artist finalArtist = artist;

        if (dto.getGenreIds() != null) {
            List<Long> genreIds = dto.getGenreIds();
            genreIds.stream()
                    .map(genreId -> genreRepository.findById(genreId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(genre -> {
                        ArtistGenre artistGenre = ArtistGenre.builder()
                                .genre(genre)
                                .build();
                        artistGenre.addArtist(finalArtist); // addArtist 메서드를 사용하여 아티스트와의 연관 관계 설정
                        return artistGenre;
                    })
                    .forEach(artistGenreRepo::save);
        }

        return ArtistResponseDto.fromEntity(artist);
    }

    // 모든 아티스트 조회 (구독/좋아요 상황 반영)
    public Page<ArtistDto> searchAll(Pageable pageable) {
        Page<Artist> artistPage = qArtistRepo.findAll(pageable);

        // 인증된 사용자라면 좋아요한 정보를 함께 전달
        if (facade.getAuth().isAuthenticated()) {
            Long userId = facade.getUserEntity().getId();
            String authority = facade.getUserEntity().getAuthorities();
            return artistPage.map(artist -> {
                boolean isLiked = artist.getLikes().stream().anyMatch(
                        like -> like.getUserEntity().getId().equals(userId)
                );
                boolean isSubscribed = artist.getSubscribes().stream().anyMatch(
                        subscribe -> subscribe.getUserEntity().getId().equals(userId)
                );
                return ArtistDto.fromEntityWithLikeStatusAndSubStatus(artist, isLiked, isSubscribed, authority);
            });
        } else { // 인증되지 않은 경우는 없이 전달
            return artistPage.map(ArtistDto::fromEntity);
        }
    }

    // 모든 아티스트 조회 (구독상황 필요 없을때)
    public List<ShowUploadArtistDto> searchAll() {
        List<Artist> artists = qArtistRepo.findAllFetch();

        return artists.stream().map(ShowUploadArtistDto::fromEntity).collect(Collectors.toList());
    }


    // 아티스트 검색
    public Page<ArtistDto> searchByArtistName(String param, Pageable pageable) {
        Page<Artist> artistPage = qArtistRepo.searchName(param, pageable);

        // 인증된 사용자라면 좋아요한 정보를 함께 전달
        if (facade.getAuth().isAuthenticated()) {
            Long userId = facade.getUserEntity().getId();
            String authority = facade.getUserEntity().getAuthorities();

            return artistPage.map(artist -> {
                boolean isLiked = artist.getLikes().stream().anyMatch(
                        like -> like.getUserEntity().getId().equals(userId)
                );
                boolean isSubscribed = artist.getSubscribes().stream().anyMatch(
                        subscribe -> subscribe.getUserEntity().getId().equals(userId)
                );
                return ArtistDto.fromEntityWithLikeStatusAndSubStatus(artist, isLiked, isSubscribed, authority);
            });
        } else { // 인증되지 않은 경우는 없이 전달
            return artistPage.map(ArtistDto::fromEntity);
        }
    }

    // id로 아티스트 조회
    public ArtistDto searchById(Long id) {
        return ArtistDto.fromEntity(artistRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        ));
    }

    public boolean artistExists(String artistName) {
        return artistRepository.existsByName(artistName);
    }


    // 아티스트 수정 로직
    @Transactional
    public ArtistResponseDto update(Long id, ArtistRequestDto dto, MultipartFile file) {
        Artist artist = artistRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found")
        );

        String imgUrl = artist.getProfileImg();
        if (file != null && !file.isEmpty()) {
            // 파일 처리 로직
            s3FileService.deleteFile("/artistImg", artist.getProfileImg());
            imgUrl = s3FileService.uploadIntoS3("/artistImg", file);
        }

        artist.setName(dto.getName());
        artist.setAge(dto.getAge());
        artist.setGender(dto.getGender());
        artist.setProfileImg(imgUrl);

        if (dto.getGenreIds() != null) {
            List<Long> genreIds = dto.getGenreIds();
            // 아티스트가 현재 가진 모든 장르를 제거
            artist.getGenres().clear();
            artistGenreRepo.deleteByArtist(artist);
            entityManager.flush(); // 변경 사항을 데이터베이스에 즉시 반영


            // 새로운 장르                      할당
            genreIds.forEach(genreId -> {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                ArtistGenre artistGenre = ArtistGenre.builder()
                        .artist(artist)
                        .genre(genre)
                        .build();
                artistGenre.addArtist(artist);
                artistGenreRepo.save(artistGenre);
            });
        }

        return ArtistResponseDto.fromEntity(artistRepository.save(artist));
    }

    // 아티스트 삭제
    @Transactional
    public void deleteById(Long id){
        artistRepository.deleteById(id);
    }



}
