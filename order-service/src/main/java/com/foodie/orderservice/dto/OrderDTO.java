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

public class OrderDTO {
    private int userId;
    private int cartId;
    private double totalAmount;
    private String orderDetails;
    private List<OrderArrayListDTO> orderArrayList;
    private OrderStatus status;
    private String address;
    private Date createdAt;

}