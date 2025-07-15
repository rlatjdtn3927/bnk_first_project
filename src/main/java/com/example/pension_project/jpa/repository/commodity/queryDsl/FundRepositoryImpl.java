package com.example.pension_project.jpa.repository.commodity.queryDsl;

import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.repository.commodity.custom_interfaces.FundCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.pension_project.jpa.entity.commodity.QFundEntity.fundEntity;

@Repository
@AllArgsConstructor
public class FundRepositoryImpl implements FundCustomRepository<FundEntity> {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FundEntity> findAllWithCondition(BooleanBuilder builder, NumberPath<Double> sortedField) {
        return jpaQueryFactory
                .selectFrom(fundEntity)
                .where(builder)
                .orderBy(sortedField.desc())
                .fetch();
    }
}
