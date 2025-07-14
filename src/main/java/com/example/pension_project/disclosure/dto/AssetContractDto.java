package com.example.pension_project.disclosure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetContractDto {

    private Long id;

    private String title;

    private String fileName;

    private String filePath;

    private Integer orderIndex;

    public String getDownloadUrl() {
        return "/static/pdf/asset/" + fileName;
    }
}
