package com.yumikorea.common.mvc.dto;

import com.google.gson.JsonObject;
import com.yumikorea.common.enums.EAdminConstants;

import lombok.Getter;

@Getter
public class PageDto {
	private int startPage; // 시작 페이지
	private int endPage; // 끝 페이지
	private int realEnd; // 총 페이지 수
	private int totalData;

	private int page;
	private int rows;
	
	private int offset;
	private int limit;
	
	boolean prev, next;

	public PageDto(int page, int rows, int total) {
		this.page = page;
		this.rows = rows;
		this.totalData = total;

		// 페이지 시작번호
		this.startPage = ((int)(Math.ceil((double) page / 5 )) - 1 ) * 5 + 1;
		
		// 페이지의 끝번호
		// Math.ceil -> 소숫점 자리에서 올림
		this.endPage = startPage + 4;
		
		this.realEnd = (int) (Math.ceil((double) total / rows));

		// 실제 끝번호보다 endPage가 큰 경우 실제 번호로 대입
		if (realEnd < endPage) {
			this.endPage = realEnd;
		}
			
		this.prev = page > 1;
		this.next = page < realEnd;

	}
	
	public PageDto(JsonObject baseTable) {
		this.page = baseTable.get(EAdminConstants.PAGE.getValue()).getAsInt();
		this.rows = baseTable.get("rows").getAsInt();
		this.totalData = baseTable.get("records").getAsInt();
		this.realEnd = baseTable.get("total").getAsInt();
		
		this.startPage = ((int)(Math.ceil((double) page / 5 )) - 1 ) * 5 + 1;
		
		// 페이지의 끝번호
		// Math.ceil -> 소숫점 자리에서 올림
		this.endPage = startPage + 4;
		
		// 실제 끝번호보다 endPage가 큰 경우 실제 번호로 대입
		if (realEnd < endPage) {
			this.endPage = realEnd;
		}
			
		this.prev = page > 1;
		this.next = page < realEnd;
				
	}
	

}
