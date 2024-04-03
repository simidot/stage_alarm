package com.example.stagealarm.item.entity;

import com.example.stagealarm.BaseEntity;
import com.example.stagealarm.item.dto.ItemDto;
import com.example.stagealarm.show.entity.ShowInfo;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private ShowInfo showInfo;

    private String itemImg;

    private String name;

    private String content;

    private Integer price;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static Item fromDto(ItemDto dto){
        return Item.builder()
                .itemImg(dto.getItemImg())
                .name(dto.getName())
                .content(dto.getContent())
                .price(dto.getPrice())
                .amount(dto.getAmount())
                .status(dto.getStatus())
                .build();
    }
}
