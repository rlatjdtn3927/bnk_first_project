package com.example.pension_project.jpa.repository.commodity.custom_interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;

public interface GuaranteeCustomRepository {
	public PagenationDto<GuaranteeEntity> guaranteePageable(Pageable pageable);
}
