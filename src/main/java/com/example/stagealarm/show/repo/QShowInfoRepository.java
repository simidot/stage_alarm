package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.dto.SortBinder;
import com.example.stagealarm.show.dto.Sortable;
import com.example.stagealarm.show.dto.SortableUtility;
import com.example.stagealarm.show.entity.QShowInfo;
import com.example.stagealarm.show.entity.QShowLike;
import com.example.stagealarm.show.entity.ShowInfo;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.stagealarm.show.entity.QShowInfo.*;
import static com.example.stagealarm.show.entity.QShowLike.*;

@Repository
@RequiredArgsConstructor
public class QShowInfoRepository {
    private final JPAQueryFactory queryFactory;

    public Page<ShowInfo> findAll(String title, Pageable pageable, Sortable sortable) {
        List<ShowInfo> showInfos = queryFactory.selectFrom(showInfo)
                .where(containIgnoreCaseTitle(title))
                .leftJoin(showInfo.showLikes, showLike)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(SortableUtility.bind(
                        sortable,
                        SortBinder.of("createdAt", showInfo.createdAt),
                        SortBinder.of("like", showLike)
                ))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(showInfo.count())
                .from(showInfo)
                .where();

        return PageableExecutionUtils.getPage(showInfos, pageable, countQuery::fetchOne);
    }


    public BooleanExpression containIgnoreCaseTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        return showInfo.title.containsIgnoreCase(title);
    }
}

