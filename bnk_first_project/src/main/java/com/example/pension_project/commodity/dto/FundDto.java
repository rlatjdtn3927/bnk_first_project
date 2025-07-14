package com.example.pension_project.commodity.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundDto {
    private String prodId; // FP_NO; 상품 식별자  + ETF: KR_PDN
    private String prodName; //NENCN_PDM; //펀드 이름
    private String setDate; //PDT_BCT_SE_DT; //설정일
    private String nav; //CRPRC; //기준가
    private String oneMonth; // MN1_JBEF_ENRT; //1개월 수익률
    private String threeMonth; //MN3_JBEF_ENRT; //3개월 수익률
    private String sixMonth; //MN6_JBEF_ENRT; //6개월 수익률
    private String year; //YR1_JBEF_ENRT; //1년 수익률
    private String accum; //SETAF_CUER_RT; //누적 수익률
    private String manager; //AST_OPR_ORGTCD_NM; //운용사
    private String fundTypeCd;//RTPEN_PDT_SMDCD_NM; //카테고리 ex.주식형
    private String risk; //PDT_RISK_GDC_NM; //위험률
    private int riskGrade; //PDT_RISK_GDC //위험률 카테고리 숫자
    private String totalFee;//TOT_PYRT; //총보수
    private String file1; //투자 설명서
    private String file2; //상품 약관
    private String file3; //간이 투자 설명서
}
