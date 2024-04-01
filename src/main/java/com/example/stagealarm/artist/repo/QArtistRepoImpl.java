package com.example.stagealarm.artist.repo;

import com.example.stagealarm.artist.entity.Artist;
import com.example.stagealarm.artist.entity.QArtist;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QArtistRepoImpl implements QArtistRepo{
  private final JPAQueryFactory queryFactory;
  private final QArtist qArtist = new QArtist("b");

  @Override
  public Page<Artist> searchName(String artistName, Pageable pageable) {
    List<Artist> artistList = queryFactory.selectFrom(qArtist)
        .where(containsIgnoreCaseName(artistName))
        .orderBy(qArtist.name.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    JPAQuery<Long> countQuery = queryFactory
        .select(qArtist.count())
        .from(qArtist)
        .where(containsIgnoreCaseName(artistName)); // 조건 추가
    return PageableExecutionUtils.getPage(artistList, pageable, countQuery::fetchOne);
  }

  @Override
  public Page<Artist> findAll(Pageable pageable) {
    List<Artist> artistList = queryFactory.selectFrom(qArtist)
        .orderBy(qArtist.name.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    JPAQuery<Long> countQuery = queryFactory.select(qArtist.count())
        .from(qArtist);
    return PageableExecutionUtils.getPage(artistList, pageable, countQuery::fetchOne);
  }

  private BooleanExpression containsIgnoreCaseName(String artistName) {
    return artistName == null ? null : qArtist.name.containsIgnoreCase(artistName);
  }
}
