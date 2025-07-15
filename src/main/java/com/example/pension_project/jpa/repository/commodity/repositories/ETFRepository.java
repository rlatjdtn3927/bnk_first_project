package com.example.pension_project.jpa.repository.commodity.repositories;

import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.repository.commodity.custom_interfaces.FundCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ETFRepository extends JpaRepository<ETFEntity, String>, FundCustomRepository<ETFEntity> {

}
