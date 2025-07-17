package com.example.pension_project.jpa.repository.commodity.custom_interfaces;

import org.springframework.data.domain.Pageable;

import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;

public interface GuaranteeCustomRepository {
	public PagenationDto<GuaranteeEntity> guaranteePageable(BooleanBuilder builder, NumberPath<Double> sortField, Pageable pageable);
}
