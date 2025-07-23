package com.example.pension_project.faq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaqTestController {

    @GetMapping("/faq")
    public String faqPage(){
        return "faq";
    }

    @GetMapping("/retirement-pension")
    public String retriementPage(){
        return "retirement-pension";
    }

    @GetMapping("/")
    public String index(){
        return "index2";
    }

//    @GetMapping("/retriement-type")
    public String retriementTypePage(){
        return "retriement-type";
    }
    
    @GetMapping("/retriement-type")
    public String retriementTypeTest(){
        return "retriement-typetest";
    }
}
