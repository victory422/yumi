package com.yumikorea.audit.dto.request;

import java.util.Date;

import com.yumikorea.audit.entity.WebAudit;
import com.yumikorea.common.mvc.dto.BasicRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString
public class WebAuditRequestDto extends BasicRequestDto {
	
	private String srcRegId;
	private String srcRegIp;
	private String srcCommand;
	private String srcResult;
	private String srcFrom;
	private String srcTo;
	private String srcUrl;
	
	private int id;
	private String admin_id;
	private Date reg_date;
	private String url;
	private String request_msg;
	private String error_msg;
	private String result;
	private String admin_ip;
	private String server_ip;
	private String menu_code;
	
	@Builder
	public WebAuditRequestDto(String srcRegId, String srcRegIp, String srcCommand, 
			String srcResult, String srcFrom, String srcTo, String srcUrl) {
		super();
		this.srcRegId = srcRegId;
		this.srcRegIp = srcRegIp;
		this.srcCommand = srcCommand;
		this.srcResult = srcResult;
		this.srcFrom = srcFrom;
		this.srcTo = srcTo;
		this.srcUrl = srcUrl;
	}
	
	
	public WebAudit setInsertEntity(WebAuditRequestDto dto) {
		return new WebAudit(dto.getId(), dto.getAdmin_id(), dto.getReg_date(), dto.getUrl()
				, dto.getRequest_msg(), dto.getError_msg(), dto.getResult(), dto.getAdmin_ip()
				, dto.getServer_ip(), dto.getMenu_code());
	}


}
