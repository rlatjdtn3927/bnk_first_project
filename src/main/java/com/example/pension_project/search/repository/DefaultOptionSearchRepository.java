package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.commodity.DefaultEntity;

public interface DefaultOptionSearchRepository extends JpaRepository<DefaultEntity, Integer> {
    @Query(value = """
            SELECT  d.*
            FROM    default_option d
            WHERE   CONTAINS(d.prod_name, :keyword, 1) > 0
            ORDER   BY SCORE(1) DESC
            """, nativeQuery = true)    
    List<DefaultEntity> searchByKeyword(@Param("keyword") String keyword);
}