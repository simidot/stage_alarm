package com.example.stagealarm.order.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.item.entity.Item;
import com.example.stagealarm.enums.Status;
import com.example.stagealarm.order.dto.ItemOrderDto;
import com.example.stagealarm.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
    private Integer amount;
    private Integer totalPrice;
    private String tossPaymentKey;
    private String tossOrderId;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static ItemOrder fromDto(ItemOrderDto dto){
        return ItemOrder.builder()
                .amount(dto.getAmount())
                .totalPrice(dto.getTotalPrice())
                .tossPaymentKey(dto.getTossPaymentKey())
                .tossOrderId(dto.getTossOrderId())
                .status(dto.getStatus())
                .build();
    }
}
