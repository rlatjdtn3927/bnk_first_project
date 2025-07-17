	package com.example.pension_project.commodity.controller;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.commodity.dto.PagenationDto;
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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        PagenationDto<FundEntity> list = fundService.fundList(formDto);	

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일반 펀드 목록을 불러오지 못했습니다.");
    }

    @PostMapping("/etf")
    public ResponseEntity<?> etfList(@RequestBody FormDto formDto) {
    	log.info("etfList...");
        formDto.toString();
        PagenationDto<ETFEntity> list = etfService.fundList(formDto);

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("etf 목록을 불러오지 못했습니다.");
    }

    @PostMapping("/tdf")
    public ResponseEntity<?> tdfList(@RequestBody FormDto formDto) {
    	log.info("tdfList...");
        formDto.toString();
        PagenationDto<TDFEntity> list = tdfService.fundList(formDto);

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("tdf 목록을 불러오지 못했습니다.");
    }
    
    @PostMapping("/guarantee")
    public ResponseEntity<?> guaranteeList(@RequestBody FormDto formDto) {
    	log.info("guaranteeList...");
    	PagenationDto<GuaranteeEntity> list = guaranteeService.guranteeList(formDto);
    	if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
    	else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("원리금 보장 상품 목록을 불러오지 못했습니다.");
    }
    
    @PostMapping("/default")
    public ResponseEntity<?> defaultList() {
    	log.info("defaultList...");
    	PagenationDto<DefaultEntity> list = defaultService.findAll();
    	if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
    	else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("디폴트 상품 목록을 불러오지 못했습니다.");
    }
    
    @PostMapping("/analysis")
    public ResponseEntity<?> analysis(@RequestBody Map<String, String> body) {
    	log.info("analysis...");
    	String prodId = body.get("prodId");
    	AnalysisEntity entity = analysisService.findById(prodId);
    	Map<String, AnalysisEntity> result = new HashMap<>();
    	result.put("analysisEntity", entity);
    	if(entity != null) return ResponseEntity.status(HttpStatus.OK).body(result);
    	else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(prodId + "의 분석 정보를 가져오지 못했습니다.");
    }
    
    

}
