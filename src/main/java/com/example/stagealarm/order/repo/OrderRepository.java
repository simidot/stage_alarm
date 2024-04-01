package com.example.stagealarm.order.repo;

import com.example.stagealarm.order.entity.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<ItemOrder, Long> {
}
