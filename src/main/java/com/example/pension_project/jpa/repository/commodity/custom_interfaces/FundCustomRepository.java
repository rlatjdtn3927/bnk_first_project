package com.example.pension_project.jpa.repository.commodity.custom_interfaces;

import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface FundCustomRepository<T> {
    public PagenationDto<T> findAllWithCondition(BooleanBuilder builder, NumberPath<Double> sortedField, Pageable pagable);
}

