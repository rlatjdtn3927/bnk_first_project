package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pension_project.jpa.entity.dataroom.Manual;

@Repository
public interface ManualSearchRepository extends JpaRepository<Manual, Long> {

    @Query(value = "SELECT * FROM manual WHERE CONTAINS(FILE_NAME, :keyword) > 0", nativeQuery = true)
    List<Manual> searchByKeyword(@Param("keyword") String keyword);
}