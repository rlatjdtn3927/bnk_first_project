package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.commodity.FundEntity;

public interface FundSearchRepository extends JpaRepository<FundEntity, String> {
    @Query(value = """
            SELECT  f.*
            FROM    fund f
            WHERE   CONTAINS(f.prod_name, :keyword, 1) > 0
            ORDER   BY SCORE(1) DESC
            """, nativeQuery = true)   
	List<FundEntity> searchByKeyword(@Param("keyword") String keyword);
}
