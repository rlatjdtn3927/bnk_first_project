package com.example.pension_project.jpa.entity.dataroom;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manual")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String systemType; // 제도별 (예: 공통, DB, DC, 개인형IRP, 기업형IRP)

    @Column(name="file_name")
    private String fileName;

    private LocalDate createdDate;
}


