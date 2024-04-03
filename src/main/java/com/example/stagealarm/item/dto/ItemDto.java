package com.example.stagealarm.item.dto;

import com.example.stagealarm.enums.Status;
import com.example.stagealarm.item.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    private Long showInfoId;

    private String itemImg;

    private String name;

    private String content;

    private Integer price;

    private Integer amount;

    private Status status;

    public static ItemDto fromEntity(Item item){
        return ItemDto.builder()
                .id(item.getId())
                .itemImg(item.getItemImg())
                .name(item.getName())
                .content(item.getContent())
                .price(item.getPrice())
                .amount(item.getAmount())
                .status(item.getStatus())
                .build();
    }
}
