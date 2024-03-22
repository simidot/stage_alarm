package com.example.stagealarm.show.dto;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.query.SortDirection;

public class SortableUtility {

    public static OrderSpecifier[] bind(Sortable sortable, SortBinder... sortBinders) {

        List<SortOption> sortOptions = SortOption.of(sortable);
        if (sortOptions == null)
            return new OrderSpecifier[]{new OrderSpecifier(Order.ASC, sortBinders[0].getTarget())};

        Map<String, SortBinder> map = Arrays.stream(sortBinders)
                .filter(binder -> sortOptions.stream().anyMatch(sortOption -> binder.getName().equals(sortOption.getName())))
                .collect(
                        Collectors.toMap(
                                binder -> binder.getName(),
                                binder -> binder
                        )
                );

        List<OrderSpecifier> orderSpecifiers = sortOptions.stream()
                .filter(sortOption -> map.containsKey(sortOption.getName()))
                .map(sortOption -> new OrderSpecifier(
                        sortOption.getSortDirection().equals(SortDirection.ASCENDING) ? Order.ASC : Order.DESC,
                        map.get(sortOption.getName()).getTarget()
                )).toList();

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
