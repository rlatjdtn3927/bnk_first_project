package com.example.pension_project.jpa.entity.commodity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "default_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultEntity {
    @Id
    private Integer prodId;

    private String prodName;

    private String descUrl;

    private String guideUrl;

    private String subProd1;

    private String subProd2;
    
    private String risk;
    
    private String riskGrade;
}
