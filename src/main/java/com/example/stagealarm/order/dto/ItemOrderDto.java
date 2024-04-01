package com.example.stagealarm.order.dto;


import com.example.stagealarm.enums.Status;
import com.example.stagealarm.order.entity.ItemOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderDto {
    private Long id;

    private Long userId;

    private Long itemId;

    private Integer amount;

    private Integer totalPrice;

    private String tossPaymentKey;

    private String tossOrderId;

    private Status status;

    public static ItemOrderDto fromEntity(ItemOrder itemOrder){
        return ItemOrderDto.builder()
                .id(itemOrder.getId())
                .userId(itemOrder.getUser().getId())
                .itemId(itemOrder.getItem().getId())
                .amount(itemOrder.getAmount())
                .totalPrice(itemOrder.getTotalPrice())
                .tossPaymentKey(itemOrder.getTossPaymentKey())
                .tossOrderId(itemOrder.getTossOrderId())
                .status(itemOrder.getStatus())
                .build();
    }


}
