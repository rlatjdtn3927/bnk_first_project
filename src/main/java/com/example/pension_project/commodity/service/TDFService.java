package com.example.pension_project.commodity.service;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.QTDFEntity;
import com.example.pension_project.jpa.entity.commodity.TDFEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.TDFRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TDFService {

    @Autowired
    private TDFRepository tdfRepository;

    public PagenationDto<TDFEntity> fundList(FormDto formDto) {
        QTDFEntity tdf = QTDFEntity.tDFEntity;
        BooleanBuilder builder = new BooleanBuilder();
        Pageable pageable = PageRequest.of(formDto.getPage(), formDto.getSize());

        if(formDto.getKeyword() != null) {
        	builder.and(tdf.prodName.likeIgnoreCase("%" + formDto.getKeyword() + "%"));
        }
        
        if (formDto.getRiskGrade() != null && formDto.getRiskGrade().length > 0) {
            builder.and(tdf.riskGrade.in((Integer[]) formDto.getRiskGrade()));
        }

        if (formDto.getCategory() != null && formDto.getCategory().length > 0) {
            builder.and(tdf.fundTypeCd.in((String[]) formDto.getCategory()));
        }

        if (formDto.getChannel() != null) {
            builder.and(tdf.channel.eq(formDto.getChannel()));
        }

        NumberPath<Double> sortField;
        switch (formDto.getInterPeriod() != null ? formDto.getInterPeriod() : 100) {
            case 1:
                sortField = tdf.oneMonth; // 1개월 수익률 필드
                break;
            case 3:
                sortField = tdf.threeMonth; // 3개월 수익률 필드
                break;
            case 6:
                sortField = tdf.sixMonth; // 6개월 수익률 필드
                break;
            case 12:
                sortField = tdf.year; // 12개월 수익률 필드
                break;
            case 100:
            default:
                sortField = tdf.accum; // 누적 수익률 필드
                break;
        }
        
        PagenationDto<TDFEntity> page = tdfRepository.findAllWithCondition(builder,sortField, pageable); 
        return page;
    }
}
