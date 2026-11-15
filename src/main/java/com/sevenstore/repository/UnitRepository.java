package com.sevenstore.repository;

import com.sevenstore.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UnitRepository extends JpaRepository <Unit, Long> {
}
