package com.foodie.orderservice.repository;

import com.foodie.orderservice.model.RecommendOrders;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderArrayRepository extends JpaRepository<RecommendOrders, Integer> {

    Optional<RecommendOrders> findByTitle(String title);

    Optional<RecommendOrders> findByProductId(int productId);

    @Query("SELECT o FROM RecommendOrders o WHERE o.id = " +
            "(SELECT sub.id FROM RecommendOrders sub " +
            " WHERE sub.category = o.category ORDER BY sub.quantity DESC LIMIT 1)")
    List<RecommendOrders> findAllOrdersSortedByQuantity();
}
