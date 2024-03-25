package com.example.stagealarm.show.dto;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.query.SortDirection;

public class SortableUtility {

    public static OrderSpecifier[] of(Sortable sortable, SortBinder... sortBinders) {
        if (sortable == null || sortable.getSort() == null) {
            return new OrderSpecifier[]{OrderByNull.DEFAULT};
        }

        List<SortOption> sortOptions = SortOption.of(sortable);

        Map<String, SortBinder> map = Arrays.stream(sortBinders)
                .filter(binder -> sortOptions.stream().anyMatch(sortOption -> binder.getName().equals(sortOption.getName())))
                .collect(
                        Collectors.toMap(
                                binder -> binder.getName(),
                                binder -> binder
                        )
                );

        List<OrderSpecifier> orderSpecifiers = sortOptions.stream()
                .filter(option -> map.containsKey(option.getName()))
                .map(option -> new OrderSpecifier(
                        option.getSortDirection().equals(SortDirection.ASCENDING) ? Order.ASC : Order.DESC,
                        map.get(option.getName()).getTarget()
                )).toList();

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    public static class SortOption {
        private String name;
        private SortDirection sortDirection;

        // 프론트에서 받아온 배열을 List로 바꿔주는 내부 클래스
        public static List<SortOption> of(Sortable sortable) {
            List<SortOption> sortOptions = new ArrayList<>();
            for (int i = 0 ; i < sortable.getSort().length; i++ ) {
                sortOptions.add(SortOption.of(
                        sortable.getSort()[i],
                        SortDirection.interpret(sortable.getOrder()[i])
                ));
            }
            return sortOptions;
        }
    }
}
