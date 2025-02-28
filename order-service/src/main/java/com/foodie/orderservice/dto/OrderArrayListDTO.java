package com.foodie.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderArrayListDTO {
    private int id;
    private int productId;
    private String title;
    private String category;
    private int quantity;
}
