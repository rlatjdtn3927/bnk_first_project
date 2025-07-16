package com.example.pension_project.commodity.service;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.entity.commodity.QETFEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.ETFRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ETFService {
    @Autowired
    private ETFRepository etfRepository;

    public PagenationDto<ETFEntity> fundList(FormDto formDto) {
        QETFEntity etf = QETFEntity.eTFEntity;
        BooleanBuilder builder = new BooleanBuilder();
        Pageable pageable = PageRequest.of(formDto.getPage(), formDto.getSize());
        
        if(formDto.getKeyword() != null) {
        	builder.and(etf.prodName.likeIgnoreCase("%" + formDto.getKeyword() + "%"));
        }
        
        if (formDto.getRiskGrade() != null && formDto.getRiskGrade().length > 0) {
            builder.and(etf.riskGrade.in((Integer[]) formDto.getRiskGrade()));
        }

        if (formDto.getCategory() != null && formDto.getCategory().length > 0) {
            builder.and(etf.fundTypeCd.in((String[]) formDto.getCategory()));
        }

        if (formDto.getChannel() != null) {
            builder.and(etf.channel.eq(formDto.getChannel()));
        }
        
        NumberPath<Double> sortField;
        switch (formDto.getInterPeriod() != null ? formDto.getInterPeriod() : 100) {
            case 1:
                sortField = etf.oneMonth; // 1개월 수익률 필드
                break;
            case 3:
                sortField = etf.threeMonth; // 3개월 수익률 필드
                break;
            case 6:
                sortField = etf.sixMonth; // 6개월 수익률 필드
                break;
            case 12:
                sortField = etf.year; // 12개월 수익률 필드
                break;
            case 100:
            default:
                sortField = etf.accum; // 누적 수익률 필드
                break;
        }
        

        PagenationDto<ETFEntity> page = etfRepository.findAllWithCondition(builder,sortField, pageable); 
        return page;
        
    }
}
