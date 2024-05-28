package com.yumikorea.dashboard.dto.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardResponseDto {
	
	private int operCnt;
	private String standardTime;
	private Date eventDate;
	private int keyStateCnt;
	private String operDate;
	private String operCode;
	private String keyState;

}
