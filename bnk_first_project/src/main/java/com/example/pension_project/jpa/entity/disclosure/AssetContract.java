package com.example.pension_project.jpa.entity.disclosure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ASSET_CONTRACT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DOC_TITLE", nullable = false)
    private String title;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName; // 실제 저장된 파일명 (예: contract1.pdf)

    @Column(name = "FILE_PATH", nullable = false)
    private String filePath; // 상대경로 또는 절대경로 (예: /static/pdf/asset/contract1.pdf)

    @Column(name = "ORDER_INDEX")
    private Integer orderIndex; // 화면에 표시할 순서 (예: 1~9)
}
