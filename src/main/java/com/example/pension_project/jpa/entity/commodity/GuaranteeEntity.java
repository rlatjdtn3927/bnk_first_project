package com.example.pension_project.jpa.entity.commodity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "principal_guarantee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuaranteeEntity {

    @Id
    private Integer prod_id;
    private String bank;
    private String prodName;
    private String maturityDate;
    private Double dbYn;
    private Double dcYn;
    private Double irpYn;
    private String termsUrl;
    private String descUrl;
    private String threeMonth;
}
