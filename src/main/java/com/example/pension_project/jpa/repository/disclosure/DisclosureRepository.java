package com.example.pension_project.jpa.repository.disclosure;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pension_project.jpa.entity.disclosure.AssetContract;


public interface DisclosureRepository extends JpaRepository<AssetContract,Long> {
	List<AssetContract> findAllByOrderByOrderIndexAsc();
}

