package com.example.stagealarm.artist.repo;

public interface QArtistLikeRepo {
    // 좋아요 갯수
    Long getLikesCount(Long artistId);
}
