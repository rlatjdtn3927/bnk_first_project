package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pension_project.jpa.entity.dataroom.Manual;

@Repository
public interface ManualSearchRepository extends JpaRepository<Manual, Long> {

    @Query(value = """
            SELECT  m.*
            FROM    manual m
            WHERE   CONTAINS(m.title     , :keyword, 1) > 0
               OR   CONTAINS(m.file_name , :keyword, 2) > 0
            ORDER   BY GREATEST(SCORE(1), SCORE(2)) DESC
            """, nativeQuery = true)
    List<Manual> searchByKeyword(@Param("keyword") String keyword);
}