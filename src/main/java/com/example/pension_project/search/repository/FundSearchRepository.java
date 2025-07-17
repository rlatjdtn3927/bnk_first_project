package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.commodity.FundEntity;

public interface FundSearchRepository extends JpaRepository<FundEntity, String> {
    @Query(value = "SELECT * FROM fund WHERE CONTAINS(prod_name, :keyword) > 0", nativeQuery = true)
    List<FundEntity> searchByKeyword(@Param("keyword") String keyword);
}
