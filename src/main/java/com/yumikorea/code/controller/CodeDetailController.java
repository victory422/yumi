package com.yumikorea.code.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumikorea.code.dto.request.CodeDetailRequestDto;
import com.yumikorea.code.service.CodeDetailService;
import com.yumikorea.common.enums.EAdminConstants;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/code/detail")
public class CodeDetailController {

	private final CodeDetailService service;
	
	@GetMapping(value = "/list")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> list(@RequestParam("masterCode") String masterCode) {
		Map<String, Object> map = new HashMap<>();
		
		CodeDetailRequestDto paramDto = new CodeDetailRequestDto();
		paramDto.setSrcMasterCode(masterCode);
		paramDto.setSrcEnable(EAdminConstants.ALL.getValue());
		
		map.put(EAdminConstants.RESULT_MAP.getValue(), service.getList(paramDto));
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		return ResponseEntity.ok(map);
	}
	
	@PostMapping(value = "/register")
	@ResponseBody
	public ResponseEntity<Map<String, String>> register(@RequestBody CodeDetailRequestDto requestDto, HttpServletRequest request) {
		String sessionId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		requestDto.setRegDateBy(sessionId);
		return ResponseEntity.ok(service.save(requestDto));
	}
	
	// 수정
	@PutMapping(value = "/update")
	@ResponseBody
	public ResponseEntity<Map<String, String>> update(@RequestBody CodeDetailRequestDto requestDto, HttpServletRequest request) {
		String sessionId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		requestDto.setModDateBy(sessionId);
		return ResponseEntity.ok(service.update(requestDto));
	}
	
	// 상태 수정
	@PutMapping(value = "/updateSt")
	public ResponseEntity<Map<String, String>> updateState(@RequestBody CodeDetailRequestDto requestDto, HttpServletRequest request) {
		String sessionId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		requestDto.setModDateBy(sessionId);
		return ResponseEntity.ok(service.updateState(requestDto));
	}
	
	// 삭제
	@DeleteMapping("/delete")
	@ResponseBody
	public ResponseEntity<Map<String, String>> delete(@RequestParam(value = "idArr") String[] ids) {
		return ResponseEntity.ok(service.delete(ids));
	}
}
