package com.example.stagealarm.board.repo;

import com.example.stagealarm.board.dto.ContentSearchParams;
import com.example.stagealarm.board.dto.TitleSearchParams;
import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.entity.QBoard;
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
public class QBoardRepoImpl implements QBoardRepo {
  private final JPAQueryFactory queryFactory;
  private final QBoard board = new QBoard("b");

  @Override
  public Page<Board> searchTitle(TitleSearchParams params, Pageable pageable) {
    List<Board> boardList = queryFactory
      .selectFrom(board)
      .where(
        titleContains(params.getTitle()),
        categoryEquals(params.getCategory())
      )
      .orderBy(board.createdAt.desc())
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetch();

    JPAQuery<Long> countQuery = queryFactory
      .select(board.count())
      .from(board)
      .where();
    return PageableExecutionUtils.getPage(boardList, pageable, countQuery::fetchOne);
  }

  @Override
  public Page<Board> searchContent(ContentSearchParams params, Pageable pageable) {
    List<Board> boardList = queryFactory
      .selectFrom(board)
      .where(
        contentContains(params.getContent()),
        categoryEquals(params.getCategory())
      )
      .orderBy(board.createdAt.desc())
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .fetch();

    JPAQuery<Long> countQuery = queryFactory
      .select(board.count())
      .from(board)
      .where();

    return PageableExecutionUtils.getPage(boardList, pageable, countQuery::fetchOne);
  }

  private BooleanExpression titleContains(String title) {
    return title == null ? null : board.title.containsIgnoreCase(title);
  }

  private BooleanExpression contentContains(String content) {
    return content == null ? null : board.content.containsIgnoreCase(content);
  }

  private BooleanExpression categoryEquals(String categoryName) {
    return categoryName == null ? null : board.category.category.eq(categoryName);
  }
}
