package com.foodie.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int cartId;
    private double totalAmount;
    private String orderDetails;
    private List<Object> orderArrayList;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String address;
    private Date createdAt;
}
