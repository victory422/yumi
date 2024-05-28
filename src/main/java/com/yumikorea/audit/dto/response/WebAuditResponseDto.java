package com.yumikorea.audit.dto.response;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class WebAuditResponseDto {

	private int id;
	private String adminId;
	private String adminName;
	private String url; 
	private String description; 
	private String adminIp;
	private Date regDate;
	private String result;
	private String errMsg;
	private String reqMsg;
	
	public WebAuditResponseDto(int id, String adminId, String adminName, String url, String description, String adminIp, Date regDate,
			String result, String errMsg, String reqMsg) {
		super();
		this.id = id;
		this.adminId = adminId;
		
		// adminName 복호화
//		this.adminName = adminName != null ? AesUtil.decrypt(adminName) : "";
		// 복호화되어 들어온다.
		this.adminName = adminName;
		
		this.url = url;
		this.description = description;
		this.adminIp = adminIp;
		this.regDate = regDate;
		this.result = result;
		this.errMsg = errMsg;
		this.reqMsg = reqMsg;
	}
	
}
