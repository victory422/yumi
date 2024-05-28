package com.yumikorea.code.dto.response;

import java.util.Date;

import com.yumikorea.code.entity.CodeMaster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeMasterResponseDto {
	private String masterCode;
	private String description;
	private int length;
	private String attribute001;
	private String attribute002;
	private String attribute003;
	private String attribute004;
	private String attribute005;
	private Date regDate;
	private String regDateBy;
	private Date modDate;
	private String modDateBy;
	private Date lastDate;
	private String lastDateBy;
	
	
	public CodeMasterResponseDto(CodeMaster codeMaster) {
		this.masterCode = codeMaster.getMaster_code();
		this.description = codeMaster.getDescription();
		this.length = codeMaster.getLength();
		this.attribute001 = codeMaster.getAttribute_001();
		this.attribute002 = codeMaster.getAttribute_002();
		this.attribute003 = codeMaster.getAttribute_003();
		this.attribute004 = codeMaster.getAttribute_004();
		this.attribute005 = codeMaster.getAttribute_005();
		this.regDate = codeMaster.getReg_date();
		this.regDateBy = codeMaster.getReg_date_by();
		this.modDate = codeMaster.getMod_date();
		this.modDateBy = codeMaster.getMod_date_by();
		this.lastDate = codeMaster.getLast_date();
		this.lastDateBy = codeMaster.getLast_date_by();
	}
	
	
}
