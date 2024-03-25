package com.example.stagealarm.show.dto;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

public class OrderByNull extends OrderSpecifier {
    public static final OrderByNull DEFAULT = new OrderByNull();
    private OrderByNull() {
        super(Order.DESC, NullExpression.DEFAULT, NullHandling.Default);
    }
}
