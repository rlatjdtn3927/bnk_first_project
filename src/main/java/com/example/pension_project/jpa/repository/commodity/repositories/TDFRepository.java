package com.example.pension_project.jpa.repository.commodity.repositories;

import com.example.pension_project.jpa.entity.commodity.TDFEntity;
import com.example.pension_project.jpa.repository.commodity.custom_interfaces.FundCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TDFRepository extends JpaRepository<TDFEntity, String>, FundCustomRepository {
}
