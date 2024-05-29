package com.yumikorea.db.dto;

import java.util.Date;

import com.yumikorea.common.mvc.dto.BasicRequestDto;
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
	
	private int dbId;
	private String dbGender;
	private String dbName;
	private Date modifyDate;
	private String dbRegPath;
	private String adminId;
	private String dbTel;
	private String deptCode;
	private String modifyId;
	
	private String srcDbName;
	private String srcDbTel;
	private String srcAdminId;
	
	
	// 등록 dto -> entity
	public DBManagementEntity toSaveEntity() {
		return DBManagementEntity.builder()
				.db_seq(dbId)
				.db_name(dbName)
				.db_gender(dbGender)
				.modify_date(modifyDate)
				.db_reg_path(dbRegPath)
				.db_tel(dbTel)
				.admin_id( adminId )
				.dept_code( deptCode )
				.modify_id( modifyId )
				.build();
	}
}
