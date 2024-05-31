package com.yumikorea.db.dto;

import java.util.Date;

import com.yumikorea.common.mvc.dto.BasicRequestDto;
import com.yumikorea.db.entity.MemoEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemoRequestDto extends BasicRequestDto {
	
	private int memoSeq;
	private int dbSeq;
	private String memoContent;
	private String memoResult;
	private String adminId;
	private Date registDate;
	
	private String srcTo;
	private String srcFrom;
	private int srcDbSeq;
	private String srcContactResult;
	
	// 등록 dto -> entity
	public MemoEntity toSaveEntity() {
		return  MemoEntity.builder()
				.memoSeq(memoSeq)
				.dbSeq(dbSeq)
				.memoContent(memoContent)
				.memoResult(memoResult)
				.adminId(adminId)
				.registDate(registDate)
				.build();
	}
	
}
