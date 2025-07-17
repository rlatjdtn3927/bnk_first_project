package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.disclosure.AssetContract;

public interface AssetContractSearchRepository extends JpaRepository<AssetContract, Long> {

    @Query(value = "SELECT * FROM asset_contract WHERE CONTAINS(DOC_TITLE, :keyword) > 0", nativeQuery = true)
    List<AssetContract> searchByKeyword(@Param("keyword") String keyword);
}