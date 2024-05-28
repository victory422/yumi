package com.yumikorea.code.entity;

import java.util.Date;

import javax.persistence.EmbeddedId;
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
@Table(name = "code_detail")
public class CodeDetail {

	@Id
	@EmbeddedId
	private CodeDetailPK codeDetailPK;
	
	private String description;
	private String value_01;
	private String value_02;
	private String value_03;
	private String value_04;
	private String value_05;
	private String attribute_001;
	private String attribute_002;
	private String attribute_003;
	private String attribute_004;
	private String attribute_005;
	private String enable;
	private Date reg_date;
	private String reg_date_by;
	private Date mod_date;
	private String mod_date_by;
	private Date last_date;
	private String last_date_by;
	
}
