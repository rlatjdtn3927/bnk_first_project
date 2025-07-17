package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.disclosure.AssetContract;

public interface AssetContractSearchRepository extends JpaRepository<AssetContract, Long> {


    @Query(value = """
        SELECT  a.*
        FROM    asset_contract a
        WHERE   CONTAINS(a.doc_title, :keyword, 1) > 0
        ORDER   BY SCORE(1) DESC
        """, nativeQuery = true)
    List<AssetContract> searchByKeyword(@Param("keyword") String keyword);
}