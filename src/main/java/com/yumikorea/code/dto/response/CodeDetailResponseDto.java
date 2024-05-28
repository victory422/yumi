package com.yumikorea.code.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeDetailResponseDto {
	private String masterCode;
	private String code;
	private String description;
	private String value01; 
	private String value02;
	private String value03;
	private String value04;
	private String value05;
	private String attribute001;
	private String attribute002;
	private String attribute003;
	private String attribute004;
	private String attribute005;
	private String enable;
}
