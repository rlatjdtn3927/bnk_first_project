package com.example.pension_project.jpa.entity.faq;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="faq")
@Data
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_type")
    @JsonProperty("mainType")
    private String mainType;

    @JsonProperty("subType")
    @Column(name = "sub_type")
    private String subType;

    private String question;
    private String answer;
}
