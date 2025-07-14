package com.example.pension_project.dataroom.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.pension_project.dataroom.dto.ManualDto;

import com.example.pension_project.jpa.entity.dataroom.Manual;

import com.example.pension_project.jpa.repository.dataroom.DataroomRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DataroomService {
	private final DataroomRepository dataroomRepository;
	
	public Page<ManualDto> getManualList(String keyword, String systemType, Pageable pageable) {
        Specification<Manual> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                predicates.add(cb.like(root.get("title"), "%" + keyword + "%"));
            }
            
            if (systemType != null && !systemType.equals("전체")) {
                String likePattern = "%," + systemType + ",%";
                predicates.add(cb.like(cb.concat(cb.concat(",", root.get("systemType")), ","), likePattern));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return dataroomRepository.findAll(spec, pageable)
                .map(manual -> new ManualDto(
                        manual.getId(),
                        manual.getTitle(),
                        manual.getSystemType(),
                        manual.getFileName(),
                        manual.getCreatedDate()
                ));
    }
}
