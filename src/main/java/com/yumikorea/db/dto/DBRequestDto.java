package com.yumikorea.db.dto;

import java.util.Date;

import com.yumikorea.common.mvc.dto.BasicRequestDto;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.db.entity.DBManagementEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DBRequestDto extends BasicRequestDto {
	
	private int dbSeq;
	private String dbGender;
	private String dbGenderName;
	private String dbName;
	private Date modifyDate;
	private String dbRegPath;
	private String dbRegPathName;
	private String adminId;
	private String dbTel;
	private String deptCode;
	private String deptName;
	private String modifyId;
	private String useYn;
	
	private String srcDbName;
	private String srcDeptCode;
	private String srcDbTel;
	private String srcAdminId;
	
	
	// 등록 dto -> entity
	public DBManagementEntity toSaveEntity() {
		return DBManagementEntity.builder()
				.dbSeq(dbSeq)
				.dbName(dbName)
				.dbGender(dbGender)
				.modifyDate(modifyDate)
				.dbRegPath(dbRegPath)
				.dbTel(dbTel)
				.adminId( adminId )
				.deptCode( deptCode )
				.modifyId( modifyId )
				.useYn( CommonUtil.isNull(useYn) ? "Y" : useYn )
				.build();
	}
	
}
