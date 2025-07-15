package com.example.pension_project.commodity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pension_project.jpa.entity.commodity.AnalysisEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.AnalysisRepository;

@Service
public class AnalysisService {
	
	@Autowired
	private AnalysisRepository analysisRepository;
	
	public AnalysisEntity findById(String prodId) {
		try {
			AnalysisEntity entity = analysisRepository.findById(prodId).orElseThrow();
			return entity;
		} catch(Exception e) {
			return null;
		}		
	}
}
