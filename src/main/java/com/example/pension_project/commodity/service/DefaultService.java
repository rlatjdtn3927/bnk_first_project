package com.example.pension_project.commodity.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pension_project.jpa.entity.commodity.DefaultEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.DefaultRepository;

@Service
public class DefaultService {
	
	@Autowired
	private DefaultRepository defaultRepository;

	public List<DefaultEntity> findAll() { return defaultRepository.findAll(); }

}
