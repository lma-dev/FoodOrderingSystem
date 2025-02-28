package com.foodie.orderservice.service;

import com.foodie.orderservice.dto.OrderArrayListDTO;
import com.foodie.orderservice.dto.OrderDTO;
import com.foodie.orderservice.feign.NotificationServiceClient;
import com.foodie.orderservice.feign.UserServiceClient;
import com.foodie.orderservice.model.*;

import com.foodie.orderservice.repository.CartProductRepository;
import com.foodie.orderservice.repository.CartRepository;
import com.foodie.orderservice.repository.OrderArrayRepository;
import com.foodie.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    @Autowired
    private OrderArrayRepository orderArrayRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Transactional
    public Orders createOrder(OrderDTO orders) {
        Cart cart = cartRepository.findById(orders.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getCartProducts().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);

        // ResponseEntity<?> userResponse =
        // userServiceClient.getEmail(orders.getUserId() + "");
        // if (userResponse.getStatusCode().is2xxSuccessful()) {
        // Map<String, Object> responseBody = (Map<String, Object>)
        // userResponse.getBody();
        // if (responseBody != null && responseBody.containsKey("email")) {
        // String email = (String) responseBody.get("email");
        // notificationServiceClient.createAndSendNotification("Foodie, Payment
        // Successfully",
        // "Your order has been placed. Order Status: PENDING", email);
        // }
        // }
        Orders order = new Orders();
        order.setUserId(orders.getUserId());
        order.setCartId(orders.getCartId());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(orders.getTotalAmount());
        order.setOrderDetails(orders.getOrderDetails());
        order.setCreatedAt(new Date());
        int quantiInteger = 0;
        for (OrderArrayListDTO ordersArray : orders.getOrderArrayList()) {
            RecommendOrders roD = orderArrayRepository.findByProductId(ordersArray.getProductId())
                    .orElse(new RecommendOrders());
            quantiInteger = ordersArray.getQuantity() + roD.getQuantity();
            roD.setTitle(ordersArray.getTitle());
            roD.setQuantity(quantiInteger);
            roD.setCategory(ordersArray.getCategory());
            roD.setProductId(ordersArray.getProductId());
            roD.setCreatedAt(new Date());
            orderArrayRepository.save(roD);
        }

        return orderRepository.save(order);
    }

    public Orders updateOrderStatus(int orderId, OrderStatus status) {
        Orders orders = orderRepository.findById(orderId).get();
        orders.setStatus(status);
        ResponseEntity<?> userResponse = userServiceClient.getEmail(orders.getUserId() + "");
        if (userResponse.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = (Map<String, Object>) userResponse.getBody();
            if (responseBody != null && responseBody.containsKey("email")) {
                String email = (String) responseBody.get("email");
                notificationServiceClient.createAndSendNotification("Foodie, Order Status Changed",
                        "Your Order Status: " + status, email);
            }
        }
        return orderRepository.save(orders);
    }

    public Orders getOrderById(int orderId) {
        return orderRepository.findById(orderId).get();
    }

    public List<RecommendOrders> getRecommendOrdersList() {
        return orderArrayRepository.findAllOrdersSortedByQuantity();
    }

    public List<Orders> getOrdersByUserId(int userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Iterable<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Orders> getPendingOrders() {
        return orderRepository.findByStatus(OrderStatus.PENDING);
    }

}
