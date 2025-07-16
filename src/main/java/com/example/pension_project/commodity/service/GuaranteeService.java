package com.example.pension_project.commodity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.GuaranteeRepository;

@Service
public class GuaranteeService {
	
	@Autowired
	private GuaranteeRepository guaranteeRepository;
	
	public PagenationDto<GuaranteeEntity> findAll(FormDto formDto) {
		 Pageable pageable = PageRequest.of(formDto.getPage(), formDto.getSize());
		 return guaranteeRepository.guaranteePageable(pageable);
	}
}
