package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.faq.Faq;

public interface FaqSearchRepository extends JpaRepository<Faq, Long> {
    @Query(value = "SELECT * FROM faq WHERE CONTAINS(question, :keyword) > 0 OR CONTAINS(answer, :keyword) > 0", nativeQuery = true)
    List<Faq> searchByKeyword(@Param("keyword") String keyword);
}
