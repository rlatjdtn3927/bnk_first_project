package com.example.pension_project.commodity.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PagenationDto<T> {
	
	private Integer totalCnt;
	private List<T> entityList;

}
