package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.commodity.DefaultEntity;

public interface DefaultOptionSearchRepository extends JpaRepository<DefaultEntity, Integer> {
    @Query(value = "SELECT * FROM default_option WHERE CONTAINS(prod_name, :keyword) > 0", nativeQuery = true)
    List<DefaultEntity> searchByKeyword(@Param("keyword") String keyword);
}