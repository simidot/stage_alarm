package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.entity.QArtist;
import com.example.stagealarm.artist.entity.QArtistLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QArtistLikeRepoImpl implements QArtistLikeRepo {
    private final JPAQueryFactory queryFactory;


    public Long getLikesCount(Long artistId) {
        QArtistLike qArtistLike = QArtistLike.artistLike;
        Long likesCount = queryFactory
                .select(qArtistLike.count())
                .from(qArtistLike)
                .where(qArtistLike.artist.id.eq(artistId))
                .fetchOne();
        return likesCount;
    }
}
