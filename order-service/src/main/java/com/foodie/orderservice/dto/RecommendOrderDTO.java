package com.foodie.orderservice.dto;

import java.util.Date;
import java.util.List;

import com.foodie.orderservice.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RecommendOrderDTO {
    private int id;
    private String title;
    private int quantity;
    private int productId;
    private String category;
}