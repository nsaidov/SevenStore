package com.sevenstore.repository;

import com.sevenstore.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository <Unit, Long> {
}
