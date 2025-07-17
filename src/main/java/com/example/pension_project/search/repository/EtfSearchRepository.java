package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.commodity.ETFEntity;

public interface EtfSearchRepository extends JpaRepository<ETFEntity, String> {
    @Query(value = "SELECT * FROM etf WHERE CONTAINS(prod_name, :keyword) > 0", nativeQuery = true)
    List<ETFEntity> searchByKeyword(@Param("keyword") String keyword);
}

