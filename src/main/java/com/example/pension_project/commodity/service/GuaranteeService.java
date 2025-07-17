package com.example.pension_project.commodity.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;
import com.example.pension_project.jpa.entity.commodity.QGuaranteeEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.GuaranteeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;

@Service
public class GuaranteeService {
	
	@Autowired
	private GuaranteeRepository guaranteeRepository;
	
	public PagenationDto<GuaranteeEntity> guranteeList(FormDto formDto) {
		 QGuaranteeEntity guarantee = QGuaranteeEntity.guaranteeEntity;
	     BooleanBuilder builder = new BooleanBuilder();
		 Pageable pageable = PageRequest.of(formDto.getPage(), formDto.getSize());
		 
		 if(formDto.getKeyword() != null) {
			 builder.and(guarantee.bank.likeIgnoreCase("%" + formDto.getKeyword() + "%"));
		 }
		 
		 NumberPath<Double> sortField = null;
		 switch (formDto.getGuaranteeCategory() != null ? formDto.getGuaranteeCategory() : "noCategory") {
		 	case "db" :
		 		sortField = guarantee.dbYn;
		 		break;
		 	case "dc":
		 		sortField = guarantee.dcYn;
		 		break;
		 	case "irp":
		 		sortField = guarantee.irpYn;
		 		break;
		 	default:
		 		break;
		 }
		 
		 return guaranteeRepository.guaranteePageable(builder, sortField, pageable);
	}
}
