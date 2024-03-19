package com.example.stagealarm.artist.service;

import com.example.stagealarm.artist.Artist;
import com.example.stagealarm.artist.ArtistLike;
import com.example.stagealarm.artist.dto.ArtistLikeDto;
import com.example.stagealarm.artist.repo.ArtistLikeRepository;
import com.example.stagealarm.artist.repo.ArtistRepository;
import com.example.stagealarm.user.UserEntity;
import com.example.stagealarm.user.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistLikeServiceTest {
    @Mock
    private ArtistLikeRepository artistLikeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistLikeService artistLikeService;


    @Test
    void insertLike_Success() {
        // given
        ArtistLikeDto dto = new ArtistLikeDto();
        dto.setUserId(1L);
        dto.setArtistId(2L);

//        UserEntity userEntity = UserEntity.builder()
//                .loginId("1").build();
//        Artist artist = Artist.builder()
//                .name("아이유")
//                .build();

        UserEntity userEntity = new UserEntity(1L);
        Artist artist = new Artist(2L);

        System.out.println(artist.getId());

        // when
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(artistRepository.findById(2L)).thenReturn(Optional.of(artist));
        when(artistLikeRepository.findByUserEntityAndArtist(userEntity, artist))
                .thenReturn(Optional.empty());
        System.out.println(artistLikeRepository.findByUserEntityAndArtist(userEntity, artist).toString());


        // then
        assertDoesNotThrow(() -> artistLikeService.insertLike(dto));
        verify(artistLikeRepository, times(1)).save(any());
    }

    @Test
    void insertLike_UserNotFound() {
        //given
        ArtistLikeDto dto = new ArtistLikeDto();
        dto.setUserId(1L);
        dto.setArtistId(2L);

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> artistLikeService.insertLike(dto));
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);

        verify(artistLikeRepository, never()).save(any());
    }

    @Test
    void deleteLike_Success() {
        //given
        ArtistLikeDto dto = new ArtistLikeDto();
        dto.setUserId(1L);
        dto.setArtistId(2L);

        UserEntity userEntity = new UserEntity(1L);
        Artist artist = new Artist(2L);

        ArtistLike like = new ArtistLike();
        like.setArtist(artist);
        like.setUserEntity(userEntity);

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(artistRepository.findById(2L)).thenReturn(Optional.of(artist));
        when(artistLikeRepository.findByUserEntityAndArtist(userEntity, artist))
                .thenReturn(Optional.of(like));

        //then
        assertDoesNotThrow(() -> artistLikeService.deleteLike(dto));
        verify(artistLikeRepository, times(1)).delete(like);

    }
}