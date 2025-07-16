package com.example.pension_project.jpa.repository.commodity.repositories;

import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;
import com.example.pension_project.jpa.repository.commodity.custom_interfaces.GuaranteeCustomRepository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GuaranteeRepository extends JpaRepository<GuaranteeEntity, Integer>, GuaranteeCustomRepository {
}
