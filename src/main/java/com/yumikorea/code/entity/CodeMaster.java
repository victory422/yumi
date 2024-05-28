package com.yumikorea.code.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "code_master")
public class CodeMaster {
	@Id
	private String master_code;
	private String description;
	private int length;
	private String attribute_001;
	private String attribute_002;
	private String attribute_003;
	private String attribute_004;
	private String attribute_005;
	private Date reg_date;
	private String reg_date_by;
	private Date mod_date;
	private String mod_date_by;
	private Date last_date;
	private String last_date_by;
}
