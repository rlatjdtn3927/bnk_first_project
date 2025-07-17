package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.commodity.ETFEntity;

public interface EtfSearchRepository extends JpaRepository<ETFEntity, String> {
    @Query(value = """
            SELECT  e.*
            FROM    etf e
            WHERE   CONTAINS(e.prod_name, :keyword, 1) > 0
            ORDER   BY SCORE(1) DESC
            """, nativeQuery = true)
    List<ETFEntity> searchByKeyword(@Param("keyword") String keyword);
}

