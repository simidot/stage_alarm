package com.example.stagealarm.item.repo;

import com.example.stagealarm.item.entity.Item;
import com.example.stagealarm.item.entity.QItem;
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
public class QItemRepoImpl implements QItemRepo{
  private final JPAQueryFactory queryFactory;
  private final QItem qItem = new QItem("qItem");

  @Override
  public Page<Item> findAll(Pageable pageable) {
    List<Item> itemList = queryFactory.selectFrom(qItem)
        .orderBy(qItem.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    JPAQuery<Long> countQuery = queryFactory.select(qItem.count()).from(qItem);

    return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
  }

  @Override
  public Page<Item> findAllByShowInfoId(Long showInfoId, Pageable pageable) {
    List<Item> itemList = queryFactory.selectFrom(qItem)
        .where(qItem.showInfo.id.eq(showInfoId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    JPAQuery<Long> countQuery = queryFactory.select(qItem.count()).from(qItem);

    return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
  }
}
