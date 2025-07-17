package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.admin.Notice;

public interface NoticeSearchRepository extends JpaRepository<Notice, Integer> {
    @Query(value = "SELECT * FROM tbl_notice WHERE CONTAINS(b_title, :keyword) > 0 OR CONTAINS(b_content, :keyword) > 0", nativeQuery = true)
    List<Notice> searchByKeyword(@Param("keyword") String keyword);
}
