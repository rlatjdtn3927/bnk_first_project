package com.example.pension_project.jpa.repository.commodity.queryDsl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.pension_project.commodity.dto.PagenationDto;
import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;
import static com.example.pension_project.jpa.entity.commodity.QGuaranteeEntity.guaranteeEntity;
import com.example.pension_project.jpa.repository.commodity.custom_interfaces.GuaranteeCustomRepository;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor


public class GuaranteeRepositoryImpl implements GuaranteeCustomRepository{
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    
    @Override
    public PagenationDto<GuaranteeEntity> guaranteePageable(Pageable pageable) {
    	
    	List<GuaranteeEntity> list = jpaQueryFactory
                .selectFrom(guaranteeEntity)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    	
    	Integer totalCnt = jpaQueryFactory
    			.select(Wildcard.count)
    			.from(guaranteeEntity)
    			.fetchOne()
    			.intValue();
  
    	
        return new PagenationDto<GuaranteeEntity>(totalCnt,list);
    }
}
