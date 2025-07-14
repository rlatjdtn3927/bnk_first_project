package com.example.pension_project.jpa.entity.commodity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "tdf")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TDFEntity extends BaseEntity{
    @Id
    @Column(name = "prod_id", length = 50)
    private String prodId;  // 상품 식별자 → PK로 설정 (변경 가능)

    @Column(name = "prod_name", length = 200)
    private String prodName;  // 펀드 이름

    @Column(name = "set_date", length = 20)
    private String setDate;  // 설정일

    @Column(name = "nav", length = 50)
    private String nav;  // 기준가

    @Column(name = "one_month")
    private Double oneMonth;  // 1개월 수익률

    @Column(name = "three_month")
    private Double threeMonth;  // 3개월 수익률

    @Column(name = "six_month")
    private Double sixMonth;  // 6개월 수익률

    @Column(name = "year")
    private Double year;  // 1년 수익률

    @Column(name = "accum")
    private Double accum;  // 누적 수익률

    @Column(name = "manager", length = 100)
    private String manager;  // 운용사

    @Column(name = "fund_type_cd", length = 50)
    private String fundTypeCd;  // 카테고리

    @Column(name = "risk", length = 50)
    private String risk;  // 위험률

    @Column(name = "risk_grade")
    private Integer riskGrade;  // 위험률 카테고리 숫자

    @Column(name = "total_fee", length = 50)
    private String totalFee;  // 총보수

    @Column(name="file1", length = 100)
    private String file1; // 투자설명서

    @Column(name="file2", length = 100)
    private String file2; // 상품약관

    @Column(name="file3", length = 100)
    private String file3; // 간이 투자 설명서

    @Column(name="channel")
    private Integer channel; //1: 전체, 2.온라인
}
