package com.example.pension_project.commodity.controller;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.commodity.service.AnalysisService;
import com.example.pension_project.commodity.service.DefaultService;
import com.example.pension_project.commodity.service.ETFService;
import com.example.pension_project.commodity.service.FundService;
import com.example.pension_project.commodity.service.GuaranteeService;
import com.example.pension_project.commodity.service.TDFService;
import com.example.pension_project.jpa.entity.commodity.AnalysisEntity;
import com.example.pension_project.jpa.entity.commodity.DefaultEntity;
import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.entity.commodity.GuaranteeEntity;
import com.example.pension_project.jpa.entity.commodity.TDFEntity;
import com.example.pension_project.jpa.repository.commodity.repositories.GuaranteeRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
 *
 */
@Slf4j
@RestController
@RequestMapping("/commodity")
public class CommodityRestController {

    @Autowired
    private FundService fundService;
    @Autowired
    private ETFService etfService;
    @Autowired
    private TDFService tdfService;
    @Autowired
    private GuaranteeService guaranteeService;
    @Autowired
    private DefaultService defaultService;
    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/fund")
    public ResponseEntity<?> fundList(@RequestBody FormDto formDto) {
    	log.info("fundList...");
        formDto.toString();
        List<FundEntity> list = fundService.fundList(formDto);	

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일반 펀드 목록을 불러오지 못했습니다.");
    }

    @PostMapping("/etf")
    public ResponseEntity<?> etfList(@RequestBody FormDto formDto) {
    	log.info("etfList...");
        formDto.toString();
        List<ETFEntity> list = etfService.fundList(formDto);

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("etf 목록을 불러오지 못했습니다.");
    }

    @PostMapping("/tdf")
    public ResponseEntity<?> tdfList(@RequestBody FormDto formDto) {
    	log.info("tdfList...");
        formDto.toString();
        List<TDFEntity> list = tdfService.fundList(formDto);

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("tdf 목록을 불러오지 못했습니다.");
    }
    
    @GetMapping("/guarantee")
    public ResponseEntity<?> guaranteeList() {
    	log.info("guaranteeList...");
    	List<GuaranteeEntity> list = guaranteeService.findAll();
    	if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
    	else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("원리금 보장 상품 목록을 불러오지 못했습니다.");
    }
    
    @GetMapping("/default")
    public ResponseEntity<?> defaultList() {
    	log.info("defaultList...");
    	List<DefaultEntity> list = defaultService.findAll();
    	if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
    	else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("디폴트 상품 목록을 불러오지 못했습니다.");
    }
    
    @PostMapping("/analysis")
    public ResponseEntity<?> analysis(@RequestParam("prodId") String ProdId) {
    	log.info("analysis...");
    	AnalysisEntity row = analysisService.findById(ProdId);
    	if(row != null) return ResponseEntity.status(HttpStatus.OK).body(row);
    	else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProdId + "의 분석 정보를 가져오지 못했습니다.");
    }
    
    

}
