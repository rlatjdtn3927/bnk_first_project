package com.example.pension_project.faq.dto;

import com.example.pension_project.jpa.entity.faq.Faq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqDto {

    private Long id;
    private String mainType;
    private String subType;
    private String question;
    private String answer;

    public FaqDto(Faq faq) {
        this.id = faq.getId();
        this.mainType = faq.getMainType();
        this.subType = faq.getSubType();
        this.question = faq.getQuestion();
        this.answer = faq.getAnswer();
    }
}
