package com.example.pension_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    	//fund
        registry.addResourceHandler("/pdf/fund/**")
        	.addResourceLocations("file:///C:/bnk_project/fund/");
        
        //etf
        registry.addResourceHandler("/pdf/etf/**")
        	.addResourceLocations("file:///C:/bnk_project/etf/");
        
        //tdf
        registry.addResourceHandler("/pdf/tdf/**")
        	.addResourceLocations("file:///C:/bnk_project/tdf/");
        
        //pg
        registry.addResourceHandler("/pdf/pg/**")
        	.addResourceLocations("file:///C:/bnk_project/pg/");
        
        //default
        registry.addResourceHandler("/pdf/default/**")
        	.addResourceLocations("file:///C:/bnk_project/default/");
    }
}