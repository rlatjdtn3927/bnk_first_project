package com.example.pension_project.commodity.service;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.entity.commodity.QETFEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.ETFRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ETFService {
    @Autowired
    private ETFRepository etfRepository;

    public List<ETFEntity> fundList(FormDto formDto) {
        QETFEntity fund = QETFEntity.eTFEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (formDto.getRiskGrade() != null) {
            builder.and(fund.riskGrade.eq(formDto.getRiskGrade()));
        }

        if (formDto.getCategory() != null) {
            builder.and(fund.fundTypeCd.eq(formDto.getCategory()));
        }

        if (formDto.getChannel() != null) {
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

        return etfRepository.findAllWithCondition(builder,sortField);
    }
}
