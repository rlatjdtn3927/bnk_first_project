package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.admin.Notice;

public interface NoticeSearchRepository extends JpaRepository<Notice, Integer> {
    
	@Query(value = """
            SELECT  n.*
            FROM    tbl_notice n
            WHERE   CONTAINS(n.b_title  , :keyword, 1) > 0
               OR   CONTAINS(n.b_content, :keyword, 2) > 0
            ORDER   BY GREATEST(SCORE(1), SCORE(2)) DESC
            """, nativeQuery = true)
    List<Notice> searchByKeyword(@Param("keyword") String keyword);
}
