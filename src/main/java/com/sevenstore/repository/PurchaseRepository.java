package com.sevenstore.repository;

import com.sevenstore.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository <Purchase, Long> {
}
