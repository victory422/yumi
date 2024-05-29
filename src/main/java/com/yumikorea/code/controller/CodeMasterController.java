package com.yumikorea.code.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumikorea.code.dto.request.CodeMasterRequestDto;
import com.yumikorea.code.service.CodeMasterService;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.mvc.controller.BasicController;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeMasterController extends BasicController {

	private final CodeMasterService service;

	@GetMapping("/list")
	public String list(Model model, CodeMasterRequestDto requestDto) {

		// MASTER CODE LIST
		Map<String, Object> map = service.getList(requestDto);
		model.addAttribute("list", map.get("list"));
		model.addAttribute(EAdminConstants.PAGE.getValue(), map.get(EAdminConstants.PAGE.getValue()));

		// SEARCH KEYWORD
		model.addAttribute("srcMasterCode", requestDto.getSrcMasterCode());

		return "code/codeList";
	}

	// 등록
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Map<String, String>> register(@RequestBody CodeMasterRequestDto requestDto, HttpServletRequest request) {
		String sessionId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		requestDto.setRegDateBy(sessionId);
		return ResponseEntity.ok(service.save(requestDto));
	}

	
	// 코드명 중복체크용 리스트
	@GetMapping("/get-code-names")	
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getCodeNames() {
		return ResponseEntity.ok(service.getCodeNames());
	}

	// 수정
	@PutMapping("/update")
	@ResponseBody
	public ResponseEntity<Map<String, String>> update(@RequestBody CodeMasterRequestDto requestDto, HttpServletRequest request) {
		String sessionId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		requestDto.setModDateBy(sessionId);
		return ResponseEntity.ok(service.update(requestDto));
	}

	// 삭제
	@DeleteMapping("/delete")
	@ResponseBody
	public ResponseEntity<Map<String, String>> delete(@RequestParam(value = "idArr") String[] mastercodes) {
		Map<String, String> map = service.delete(mastercodes);
		return ResponseEntity.ok(map);
	}
}
