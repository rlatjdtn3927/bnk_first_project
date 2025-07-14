package com.example.pension_project.jpa.repository.faq;

import com.example.pension_project.jpa.entity.faq.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FaqRepository extends JpaRepository<Faq, Long> {
    Page<Faq> findByMainType(String mainType, Pageable pageable);
    Page<Faq> findByMainTypeAndSubType(String mainType, String subType, Pageable pageable);

    @Query("""
    SELECT f FROM Faq f
    WHERE f.mainType = :mainType
      AND (:subType IS NULL OR :subType = '전체' OR f.subType = :subType)
      AND (
        LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    """)
    Page<Faq> searchFaq(
            @Param("mainType") String mainType,
            @Param("subType") String subType,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}

