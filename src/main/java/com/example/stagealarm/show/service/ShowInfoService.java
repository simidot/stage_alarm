package com.example.stagealarm.show.service;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.repo.ArtistRepository;
import com.example.stagealarm.awsS3.S3FileService;
import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.genre.entity.Genre;
import com.example.stagealarm.genre.repo.GenreRepository;
import com.example.stagealarm.show.dto.ShowInfoRequestDto;
import com.example.stagealarm.show.dto.ShowInfoResponseDto;
import com.example.stagealarm.show.dto.Sortable;
import com.example.stagealarm.show.entity.ShowArtist;
import com.example.stagealarm.show.entity.ShowGenre;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.repo.QShowInfoRepository;
import com.example.stagealarm.show.repo.ShowArtistRepo;
import com.example.stagealarm.show.repo.ShowGenreRepo;
import com.example.stagealarm.show.repo.ShowInfoRepository;
import com.example.stagealarm.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShowInfoService {
    private final ShowInfoRepository showInfoRepository;
    private final QShowInfoRepository qShowInfoRepository;
    private final S3FileService s3FileService;
    private final AuthenticationFacade facade;

    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final ShowGenreRepo showGenreRepo;
    private final ShowArtistRepo showArtistRepo;


    // 공연 정보 등록
    @Transactional
    public ShowInfoResponseDto create(ShowInfoRequestDto dto, MultipartFile file) {
        // 공연 기본정보 + 아티스트 정보 + 장르 정보 + 이미지 파일

        ShowInfo showInfo = ShowInfo.builder()
            .date(dto.getDate())
            .startTime(dto.getStartTime())
            .hours(dto.getHours())
            .duration(dto.getDuration())
            .location(dto.getLocation())
            .title(dto.getTitle())
            .ticketVendor(dto.getTicketVendor())
            // s3service
            .posterImage(s3FileService.uploadIntoS3("/showInfoImg", file))
            .price(dto.getPrice())
            .build();

        // 검색된 아티스트/장르 찾아서 show와 artist/genre 연결지어주는 엔티티 객체 생성 후 저장
        Artist artist = artistRepository.findById(dto.getArtistId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Genre genre = genreRepository.findById(dto.getGenreId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ShowArtist newShowArtist = ShowArtist.builder()
            .artist(artist)
            .showInfo(showInfo)
            .build();
        ShowGenre newShowGenre = ShowGenre.builder()
            .genre(genre)
            .showInfo(showInfo)
            .build();

        ShowInfo saved = showInfoRepository.save(showInfo);
        showArtistRepo.save(newShowArtist);
        showGenreRepo.save(newShowGenre);
        return ShowInfoResponseDto.fromEntity(saved);
    }


    // 공연 정보 조회 (전체)
    public Page<ShowInfoResponseDto> readAll(String title, Pageable pageable, Sortable sortable) {
        UserEntity userEntity = facade.getUserEntity();
        Long userId = userEntity.getId();

        return qShowInfoRepository.findAll(title, pageable, sortable, userId);
    }

    // 공연 정보 조히 (단일)
    public ShowInfoResponseDto readOne(Long id) {
        ShowInfo showInfo = showInfoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ShowInfoResponseDto.fromEntity(showInfo);
    }


    // 공연 정보 업데이트
    @Transactional
    public void update(Long id, ShowInfoRequestDto dto) {
        // 관리자 계정인지 아닌지 확인하는 과정 추가로 필요

        ShowInfo showInfo = showInfoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        showInfo.setDate(dto.getDate());
        showInfo.setStartTime(dto.getStartTime());
        showInfo.setHours(dto.getHours());
        showInfo.setDuration(dto.getDuration());
        showInfo.setLocation(dto.getLocation());
        showInfo.setTitle(dto.getTitle());
        showInfo.setTicketVendor(dto.getTicketVendor());
        // s3service
        showInfo.setPosterImage("image");
        showInfo.setPrice(dto.getPrice());

    }

    @Transactional
    public void delete(Long id) {
        showInfoRepository.deleteById(id);
    }

}