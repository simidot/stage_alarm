package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.dto.ShowInfoResponseDto;
import com.example.stagealarm.show.dto.SortBinder;
import com.example.stagealarm.show.dto.Sortable;
import com.example.stagealarm.show.dto.SortableUtility;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.stagealarm.show.entity.QShowInfo.*;
import static com.example.stagealarm.show.entity.QShowLike.*;

@Repository
@RequiredArgsConstructor
public class QShowInfoRepository {
    private final JPAQueryFactory queryFactory;

    public Page<ShowInfoResponseDto> findAll(String title,
                                             Pageable pageable,
                                             Sortable sortable,
                                             Long userId) {
        List<ShowInfoResponseDto> content = queryFactory.select(
                        // Projections : 일부 컬럼만 가져오기 위함
                        // constructor : Dto의 생성자를 기준으로 select하기 위해
                        Projections.constructor(
                                ShowInfoResponseDto.class,
                                showInfo.id,
                                showInfo.date,
                                showInfo.startTime,
                                showInfo.hours,
                                showInfo.duration,
                                showInfo.location,
                                showInfo.title,
                                showInfo.ticketVendor,
                                showInfo.price,
                                showInfo.posterImage,
                                // showInfo가 총 몇개 인지 count => showLike안의 showInfo 갯수를 셈
                                //select t1.id, count(t1.id) from SHOW_INFO t1 join SHOW_LIKE t2
                                //on t1.id = t2.SHOW_INFO_ID group by (t1.id);
                                showLike.showInfo.id.count()
                        )
                )
                .from(showInfo)
                .leftJoin(showLike)
                .on(showLike.showInfo.id.eq(showInfo.id))
                .where(containIgnoreCaseTitle(title))
                // on showInfo.id = showLike.showInfo_id
                .groupBy(
                        // showInfo에서 좋아요개수를 제외한 나머지가 같은 showInfo끼리 묶어준다.
                        showInfo.id,
                        showInfo.date,
                        showInfo.startTime,
                        showInfo.hours,
                        showInfo.duration,
                        showInfo.location,
                        showInfo.title,
                        showInfo.ticketVendor,
                        showInfo.price,
                        showInfo.posterImage,
                        showInfo.createdAt
                )
                .orderBy(SortableUtility.of(
                        // 프론트에서 받아온 sort와 order
                        sortable,
                        // sortbinder : querydsl객체로 변환하는 역할
                        SortBinder.of("createdAt", showInfo.date),
                        SortBinder.of("like", showLike.showInfo.id.count())
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        handleIsliked(userId, content);


        JPAQuery<Long> countQuery = queryFactory
                .select(showInfo.count())
                .where(containIgnoreCaseTitle(title))
                .from(showInfo);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private void handleIsliked(Long userId, List<ShowInfoResponseDto> content) {
        if (userId != null) {
            List<Long> likedShowIds = queryFactory.select(showLike.showInfo.id)
                    .from(showLike)
                    .where(showLike.userEntity.id.eq(userId))
                    .fetch();


            Map<Long, Long> showLikeMap = new HashMap<>();
            for (Long showId : likedShowIds) {
                showLikeMap.put(showId, showId);
            }
            // list로 가져온 showInfo가 query문으로 가져온 좋아요가 눌러진 showInfo Map에 있냐 없나를 for문으로 확인
            content.forEach(item -> {
                item.setIsLiked(showLikeMap.containsKey(item.getId()));
            });
        } else {
            content.forEach(item -> {
                item.setIsLiked(false);
            });
        }
    }


    public BooleanExpression containIgnoreCaseTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        return showInfo.title.containsIgnoreCase(title);
    }
}

