package com.yumikorea.code.controller;

import java.util.List;
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
import com.yumikorea.code.dto.response.CodeDetailResponseDto;
import com.yumikorea.code.service.CodeDetailService;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.mvc.controller.BasicController;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/code/detail")
public class CodeDetailController extends BasicController {

	private final CodeDetailService service;
	
	@GetMapping(value = "/list")
	@ResponseBody
	public ResponseEntity<List<CodeDetailResponseDto>> list(@RequestParam("masterCode") String masterCode) {
		return ResponseEntity.ok(service.getList(masterCode));
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
