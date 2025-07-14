package com.example.pension_project.jpa.repository.commodity.queryDsl;

import com.example.pension_project.jpa.entity.commodity.TDFEntity;
import com.example.pension_project.jpa.repository.commodity.custom_interfaces.FundCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.example.pension_project.jpa.entity.commodity.QTDFEntity.tDFEntity;

@Repository
public class TDFRepositoryImpl implements FundCustomRepository<TDFEntity> {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TDFEntity> findAllWithCondition(BooleanBuilder builder, NumberPath<Double> sortedField) {
        return jpaQueryFactory
                .selectFrom(tDFEntity)
                .where(builder)
                .orderBy(sortedField.desc())
                .fetch();
    }
}
