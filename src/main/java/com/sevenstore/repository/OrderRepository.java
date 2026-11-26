package com.sevenstore.repository;

import com.sevenstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository <Order, Long> {
}
