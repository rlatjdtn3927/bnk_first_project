package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.faq.Faq;

public interface FaqSearchRepository extends JpaRepository<Faq, Long> {
    @Query(value = """
            SELECT  f.*
            FROM    faq f
            WHERE   CONTAINS(f.question, :keyword, 1) > 0
               OR   CONTAINS(f.answer  , :keyword, 2) > 0
            ORDER   BY GREATEST(SCORE(1), SCORE(2)) DESC
            """, nativeQuery = true)
    List<Faq> searchByKeyword(@Param("keyword") String keyword);
}
