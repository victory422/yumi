package com.yumikorea.audit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yumikorea.audit.dto.request.WebAuditRequestDto;
import com.yumikorea.audit.dto.response.WebAuditResponseDto;
import com.yumikorea.audit.service.WebAuditService;
import com.yumikorea.code.dto.request.CodeDetailRequestDto;
import com.yumikorea.code.dto.response.CodeDetailResponseDto;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.code.service.CodeDetailService;
import com.yumikorea.common.enums.EAdminConstants;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/audit")
public class WebAuditController {

	private final WebAuditService service;
	private final CodeDetailService codeService;
	
	
	@GetMapping("/admin/list")
	public String adminList( Model model, WebAuditRequestDto dto ) {
		Map<String, Object> map =  service.getList(dto);
		
		@SuppressWarnings("unchecked")
		List<WebAuditResponseDto> auditList = (List<WebAuditResponseDto>) map.get("list");
		
		CodeDetailRequestDto operationDto = new CodeDetailRequestDto();
		operationDto.setSrcMasterCode(EnumMasterCode.ADMIN_OPERATION_CODE.getMasterCodeValue());
		List<CodeDetailResponseDto> operationList = codeService.getList(operationDto);
		
		CodeDetailRequestDto resultSFDto = new CodeDetailRequestDto();
		resultSFDto.setSrcMasterCode(EnumMasterCode.RESULT_SF.getMasterCodeValue());
		List<CodeDetailResponseDto> resultSFList = codeService.getList(resultSFDto);
		
		for( CodeDetailResponseDto codeDto : operationList ) {
			for( WebAuditResponseDto auditDto : auditList ) {
				if( auditDto.getUrl().equals(codeDto.getValue01()) ) {
					auditDto.setDescription(codeDto.getDescription());
				}
			}
		}
		
		model.addAttribute( "list", auditList );
		model.addAttribute( EAdminConstants.PAGE.getValue(), map.get(EAdminConstants.PAGE.getValue()) );
		model.addAttribute( "resultSFList", resultSFList );
		model.addAttribute( "operationList", operationList );
		model.addAttribute("srcKeyword", dto);

		return "audit/adminAuditList";
	}
}
