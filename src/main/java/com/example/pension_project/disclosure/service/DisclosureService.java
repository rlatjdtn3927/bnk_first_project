package com.example.pension_project.disclosure.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.pension_project.disclosure.dto.AssetContractDto;

import com.example.pension_project.jpa.entity.disclosure.AssetContract;

import com.example.pension_project.jpa.repository.disclosure.DisclosureRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DisclosureService {
	
	private final DisclosureRepository disclosureRepository;

	 private AssetContractDto convertToDto(AssetContract entity) {
	        return AssetContractDto.builder()
	                .id(entity.getId())
	                .title(entity.getTitle())
	                .fileName(entity.getFileName())
	                .filePath(entity.getFilePath())
	                .orderIndex(entity.getOrderIndex())
	                .build();
	    }

    public List<AssetContractDto> getAllContracts() {
        return disclosureRepository.findAllByOrderByOrderIndexAsc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
