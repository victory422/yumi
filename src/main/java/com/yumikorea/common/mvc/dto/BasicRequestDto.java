package com.yumikorea.common.mvc.dto;

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
	
}
