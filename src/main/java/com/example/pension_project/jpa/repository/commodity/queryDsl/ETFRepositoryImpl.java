package com.example.pension_project.jpa.repository.commodity.queryDsl;

import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.repository.commodity.custom_interfaces.FundCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.example.pension_project.jpa.entity.commodity.QETFEntity.eTFEntity;

@Repository
@AllArgsConstructor
public class ETFRepositoryImpl implements FundCustomRepository<ETFEntity> {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ETFEntity> findAllWithCondition(BooleanBuilder builder, NumberPath<Double> sortedField) {
        return jpaQueryFactory
                .selectFrom(eTFEntity)
                .where(builder)
                .orderBy(sortedField.desc())
                .fetch();
    }
}
