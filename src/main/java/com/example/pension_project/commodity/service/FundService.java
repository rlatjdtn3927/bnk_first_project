package com.example.pension_project.commodity.service;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.entity.commodity.QFundEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.FundRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class FundService {

    @Autowired
    private FundRepository fundRepository;

    public PagenationDto<FundEntity> fundList(FormDto formDto) {
        QFundEntity fund = QFundEntity.fundEntity;
        BooleanBuilder builder = new BooleanBuilder();
        
        Pageable pageable = PageRequest.of(formDto.getPage(), formDto.getSize());
        
        if(formDto.getKeyword() != null) {
        	builder.and(fund.prodName.likeIgnoreCase("%" + formDto.getKeyword() + "%"));
        }
        
        if (formDto.getRiskGrade() != null && formDto.getRiskGrade().length > 0) {
            builder.and(fund.riskGrade.in((Integer[]) formDto.getRiskGrade()));
        }

        if (formDto.getCategory() != null && formDto.getCategory().length > 0) {
            builder.and(fund.fundTypeCd.in((String[]) formDto.getCategory()));
        }

        if (formDto.getChannel() != null && formDto.getChannel() != 1) {
            builder.and(fund.channel.eq(formDto.getChannel()));
        }

        NumberPath<Double> sortField;
        switch (formDto.getInterPeriod() != null ? formDto.getInterPeriod() : 100) {
            case 1:
                sortField = fund.oneMonth; // 1개월 수익률 필드
                break;
            case 3:
                sortField = fund.threeMonth; // 3개월 수익률 필드
                break;
            case 6:
                sortField = fund.sixMonth; // 6개월 수익률 필드
                break;
            case 12:
                sortField = fund.year; // 12개월 수익률 필드
                break;
            case 100:
            default:
                sortField = fund.accum; // 누적 수익률 필드
                break;
        }

        PagenationDto<FundEntity> page = fundRepository.findAllWithCondition(builder,sortField, pageable); 
        return page;
    }

}
