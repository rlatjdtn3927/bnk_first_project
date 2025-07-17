package com.example.pension_project.jpa.repository.commodity.queryDsl;

import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.TDFEntity;
import com.example.pension_project.jpa.repository.commodity.custom_interfaces.FundCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.pension_project.jpa.entity.commodity.QTDFEntity.tDFEntity;

@Repository
public class TDFRepositoryImpl implements FundCustomRepository<TDFEntity> {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public PagenationDto<TDFEntity> findAllWithCondition(BooleanBuilder builder, NumberPath<Double> sortedField, Pageable pageable) {
    	
    	List<TDFEntity> list = jpaQueryFactory
                .selectFrom(tDFEntity)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortedField.desc())
                .fetch();
    	
		Integer totalCnt = jpaQueryFactory
				  .select(Wildcard.count)
				  .from(tDFEntity)
				  .where(builder)
				  .fetchOne()
				  .intValue();
		
		 return new PagenationDto<TDFEntity>(totalCnt, list);
    }
}
