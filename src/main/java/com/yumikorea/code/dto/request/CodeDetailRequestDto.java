package com.yumikorea.code.dto.request;

import java.util.Date;

import com.yumikorea.code.entity.CodeDetail;
import com.yumikorea.code.entity.CodeDetailPK;
import com.yumikorea.common.mvc.dto.BasicRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString
public class CodeDetailRequestDto extends BasicRequestDto {

	private String masterCode;
	private String code;
	private String description;
	private String value01;
	private String value02;
	private String value03;
	private String value04;
	private String value05;
	private String attribute001;
	private String attribute002;
	private String attribute003;
	private String attribute004;
	private String attribute005;
	private String enable;
	private String regDateBy;
	private String modDateBy;
	
	private String srcMasterCode;
	private String srcCode;
	private String srcDescription;
	private String srcValue01;
	private String srcValue02;
	private String srcValue03;
	private String srcValue04;
	private String srcValue05;
	private String SrcEnable;
	
	public CodeDetail toSaveEntity() {
		return CodeDetail.builder()
				.codeDetailPK(new CodeDetailPK(masterCode, code))
				.description(description)
				.value_01(value01)
				.value_02(value02)
				.value_03(value03)
				.value_04(value04)
				.value_05(value05)
				.attribute_001(attribute001)
				.attribute_002(attribute002)
				.attribute_003(attribute003)
				.attribute_004(attribute004)
				.attribute_005(attribute005)
				.enable("Y")
				.reg_date(new Date())
				.reg_date_by(regDateBy)
				.last_date(new Date())
				.last_date_by(regDateBy)
				.build();
	}
	
	public CodeDetail toUpdateEntity() {
		return CodeDetail.builder()
				.codeDetailPK(new CodeDetailPK(masterCode, code))
				.description(description)
				.value_01(value01)
				.value_02(value02)
				.value_03(value03)
				.value_04(value04)
				.value_05(value05)
				.attribute_001(attribute001)
				.attribute_002(attribute002)
				.attribute_003(attribute003)
				.attribute_004(attribute004)
				.attribute_005(attribute005)
				.enable(enable)
				.mod_date(new Date())
				.mod_date_by(modDateBy)
				.last_date(new Date())
				.last_date_by(modDateBy)
				.build();
	}
	
	@Builder
	public CodeDetailRequestDto(String srcMasterCode, String srcCode, String srcDescription, String srcValue01,
			String srcValue02, String srcValue03, String srcValue04, String srcValue05) {
		super();
		this.srcMasterCode = srcMasterCode;
		this.srcCode = srcCode;
		this.srcDescription = srcDescription;
		this.srcValue01 = srcValue01;
		this.srcValue02 = srcValue02;
		this.srcValue03 = srcValue03;
		this.srcValue04 = srcValue04;
		this.srcValue05 = srcValue05;
	}

}
