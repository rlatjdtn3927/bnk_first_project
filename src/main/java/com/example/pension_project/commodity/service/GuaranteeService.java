package com.example.pension_project.commodity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.GuaranteeRepository;

@Service
public class GuaranteeService {
	
	@Autowired
	private GuaranteeRepository guaranteeRepository;
	
	public List<GuaranteeEntity> findAll() { return guaranteeRepository.findAll();}

}
