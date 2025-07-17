package com.example.pension_project.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;

public interface PrincipalSearchRepository extends JpaRepository<GuaranteeEntity, Integer> {

    @Query(value = """
            SELECT  p.*
            FROM    principal_guarantee p
            WHERE   CONTAINS(p.prod_name, :keyword, 1) > 0
               OR   CONTAINS(p.bank     , :keyword, 2) > 0
            ORDER   BY GREATEST(SCORE(1), SCORE(2)) DESC
            """, nativeQuery = true)
	List<GuaranteeEntity> searchByKeyword(@Param("keyword") String keyword);
}
