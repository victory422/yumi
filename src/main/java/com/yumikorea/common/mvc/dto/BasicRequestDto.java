package com.yumikorea.common.mvc.dto;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicRequestDto {
	private int page = 1;
	private int rows = 10;
	private int offset;
	
	public void setPage(int page) {
		this.page = page;
		this.offset = (page - 1) * rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public Date toDate(String eDate) {
		String[] dates = eDate.split("-");
		return new Date(eDate);
//		return DateUtils.t(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
	}
}
