package com.yumikorea.audit.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yumikorea.audit.dto.request.WebAuditRequestDto;
import com.yumikorea.audit.service.WebAuditService;
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

		model.addAllAttributes(service.getList(dto));
		
		List<CodeDetailResponseDto> operationList = codeService.getList(EnumMasterCode.ADMIN_OPERATION_CODE.getMasterCodeValue());
		List<CodeDetailResponseDto> resultSFList = codeService.getList(EnumMasterCode.RESULT_SF.getMasterCodeValue());
		
		
		model.addAttribute( "resultSFList", resultSFList );
		model.addAttribute( "operationList", operationList );
		model.addAttribute(EAdminConstants.SEARCH_KEY_WORD.getValue(), dto);

		return "audit/adminAuditList";
	}
}
