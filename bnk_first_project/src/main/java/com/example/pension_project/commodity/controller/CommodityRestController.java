package com.example.pension_project.commodity.controller;

import com.example.pension_project.commodity.dto.FormDto;
import com.example.pension_project.commodity.service.ETFService;
import com.example.pension_project.commodity.service.FundService;
import com.example.pension_project.commodity.service.TDFService;
import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.entity.commodity.TDFEntity;
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
@RestController
@RequestMapping("/commodity")
public class CommodityRestController {

    @Autowired
    private FundService fundService;
    @Autowired
    private ETFService etfService;
    @Autowired
    private TDFService tdfService;

    @PostMapping("/fund")
    public ResponseEntity<?> fundList(@RequestBody FormDto formDto) {
        formDto.toString();
        List<FundEntity> list = fundService.fundList(formDto);

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("검색결과가 없습니다.");
    }

    @PostMapping("/etf")
    public ResponseEntity<?> etfList(@RequestBody FormDto formDto) {
        formDto.toString();
        List<ETFEntity> list = etfService.fundList(formDto);

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("검색결과가 없습니다.");
    }

    @PostMapping("/tdf")
    public ResponseEntity<?> tdfList(@RequestBody FormDto formDto) {
        formDto.toString();
        List<TDFEntity> list = tdfService.fundList(formDto);

        if(list != null) return ResponseEntity.status(HttpStatus.OK).body(list);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("검색결과가 없습니다.");
    }

}
