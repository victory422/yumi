package com.yumikorea.announce.dto;

import java.util.Date;

import com.yumikorea.announce.entity.AnnounceEntity;
import com.yumikorea.common.mvc.dto.BasicRequestDto;
import com.yumikorea.common.utils.CommonUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnnounceRequestDto extends BasicRequestDto {

	private int announceId;
	private String adminId;
	private String announceObject;
	private String announceContent;
	private Date modifyDate;
	private Date reigistDate;
	private int announceCount;
	private String deleteYn;

	private String srcAdminId;
	private String srcAnnounceObject;
	private String srcFrom;
	private String srcTo;
	
	// 등록 dto -> entity
	public AnnounceEntity toSaveEntity() {
		
		return AnnounceEntity.builder()
				.announceId(announceId)
				.adminId(adminId)
				.announceObject(announceObject)
				.announceContent(announceContent)
				.modifyDate(modifyDate)
				.reigistDate(reigistDate)
				.announceCount(announceCount)
				.deleteYn((String) CommonUtil.ifNull(deleteYn, "N"))
				.build();
	}
	
	
}
