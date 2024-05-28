package com.yumikorea.code.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yumikorea.code.entity.CodeMaster;
import com.yumikorea.common.mvc.dto.BasicRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CodeMasterRequestDto extends BasicRequestDto {

	private String mastercode;
	private String description;
	private int length;
	private String attribute01;
	private String attribute02;
	private String attribute03;
	private String attribute04;
	private String attribute05;
	private String regDate;
	private String regDateBy;
	private String modDate;
	private String modDateBy;
	
	private String srcMasterCode;

	@Builder
	public CodeMasterRequestDto(String mastercode, String description, int length, String attribute01,
			String attribute02, String attribute03, String attribute04, String attribute05, String regDate,
			String regDateBy, String srcMasterCode) {
		super();
		this.mastercode = mastercode;
		this.description = description;
		this.length = length;
		this.attribute01 = attribute01;
		this.attribute02 = attribute02;
		this.attribute03 = attribute03;
		this.attribute04 = attribute04;
		this.attribute05 = attribute05;
		this.regDate = regDate;
		this.regDateBy = regDateBy;
		this.srcMasterCode = srcMasterCode;
	}
	
	public CodeMaster toSaveEntity() {
		return CodeMaster.builder()
				.master_code(mastercode)
				.length(length)
				.description(description)
//				.attribute_001(attribute01)
//				.attribute_002(attribute02)
//				.attribute_003(attribute03)
//				.attribute_004(attribute04)
//				.attribute_005(attribute05)
				.reg_date( new Date() )
				.reg_date_by(regDateBy)
				.last_date(new Date())
				.last_date_by(regDateBy)
				.build();
	}
	
	public CodeMaster toUpdateEntity() {
		try {
			return CodeMaster.builder()
					.master_code(mastercode)
					.description(description)
					.length(length)
//				.attribute_001(attribute01)
//				.attribute_002(attribute02)
//				.attribute_003(attribute03)
//				.attribute_004(attribute04)
//				.attribute_005(attribute05)
					.reg_date( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(regDate) )
					.reg_date_by(regDateBy)
					.mod_date( new Date() )
					.mod_date_by(modDateBy)
					.last_date(new Date())
					.last_date_by(modDateBy)
					.build();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
