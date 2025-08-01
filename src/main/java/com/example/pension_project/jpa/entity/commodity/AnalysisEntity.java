package com.example.pension_project.jpa.entity.commodity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "fund_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisEntity extends BaseEntity{

    @Id
    @Column(name="prod_id")
    private String prodId;

    @Column(name="overview_url")
    private String overviewUrl;

    @Column(name="nav_url")
    private String navUrl;

    @Column(name="performance_chart_url")
    private String performanceChartUrl;

    @Column(name="performance_analysis_url")
    private String performanceAnalysisUrl;

    @Column(name="risk_analysis_url")
    private String riskAnalysisUrl;

    @Column(name="portfolio_analysis_url")
    private String portfolioAnalysisUrl;

    @Column(name="holdings_url")
    private String holdingsUrl;
    
    @Column(name="file1", length = 100)
    private String file1; // 투자설명서

    @Column(name="file2", length = 100)
    private String file2; // 상품약관

    @Column(name="file3", length = 100)
    private String file3; // 간이 투자 설명서

}
