package com.example.pension_project.jpa.repository.commodity.custom_interfaces;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;

import java.util.List;

public interface FundCustomRepository<T> {
    public List<T> findAllWithCondition(BooleanBuilder builder, NumberPath<Double> sortedField);
}

