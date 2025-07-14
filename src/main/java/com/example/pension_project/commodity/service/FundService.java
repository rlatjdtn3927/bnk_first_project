package com.example.pension_project.commodity.service;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.entity.commodity.QFundEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.FundRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
/*
 **위험등급 1: 매우높은위험, 2: 높은위험, 3: 다소높은위험, 4. 보통위험, 5.낮은위험, 6.매우낮은위험
 **펀드유형 : MMF, 채권형, 채권혼합형, 주식혼합형, 주식형, 파생상품형, 재간접
 **체널 구분: 1: 전체 / 2: 온라인전용 디폴트: 1
 ** 수익률 구분: 1,3,6,12,누적 (1, 3, 6, 12, 100) 디폴트 : 누적(100)
 *  펀드 (위험등급, 유형, 체널구분) --> 검색 후 수익률(1,3,6,12,누적) 정렬
 *  etf (위험등급, 유형, 체널구분) --> 검색 후 수익률(1,3,6,12,누적) 정렬
 *  tdf (위험등급, 유형, 체널구분) --> 검색 후 수익률(1,3,6,12,누적) 정렬
 *  예금 (표로보여줌)
 *  디폴트옵션 (표로보여줌)
 *  private Integer riskGrade;
    private String category;
    private Integer channel;
    private Integer interPeriod;
 */
@Service
public class FundService {

    @Autowired
    private FundRepository fundRepository;

    public List<FundEntity> fundList(FormDto formDto) {
        QFundEntity fund = QFundEntity.fundEntity;
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


        return fundRepository.findAllWithCondition(builder,sortField);
    }

}
