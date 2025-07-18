package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.commodity.TDFEntity;

public interface TdfSearchRepository extends JpaRepository<TDFEntity, String> {
    @Query(value = """
            SELECT  t.*
            FROM    tdf t
            WHERE   CONTAINS(t.prod_name, :keyword, 1) > 0
            ORDER   BY SCORE(1) DESC
            """, nativeQuery = true)
    List<TDFEntity> searchByKeyword(@Param("keyword") String keyword);
}