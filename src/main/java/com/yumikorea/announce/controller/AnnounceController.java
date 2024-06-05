package com.yumikorea.announce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
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

import com.yumikorea.announce.dto.AnnounceRequestDto;
import com.yumikorea.announce.service.AnnounceService;
import com.yumikorea.audit.dto.response.WebAuditResponseDto;
import com.yumikorea.code.dto.response.CodeDetailResponseDto;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.common.enums.EAdminConstants;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping( "/announce" )
public class AnnounceController {
	
	private final AnnounceService announceService;
	
	/* 목록 조회 */
	@GetMapping( "/list" )
	public String list( Model model, AnnounceRequestDto dto, HttpServletRequest request ) {
		Map<String, Object> map =  announceService.getList(dto);
		
		model.addAllAttributes(map);
		model.addAttribute( EAdminConstants.PAGE.getValue(), map.get(EAdminConstants.PAGE.getValue()) );
		model.addAttribute(EAdminConstants.SEARCH_KEY_WORD.getValue(), dto);

		return "announce/announceList";
	}
	
	/* 등록 */
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> register( @RequestBody AnnounceRequestDto dto, HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute( EAdminConstants.LOGIN_ID.getValue() );
		dto.setAdminId(loginId);
		Map<String, Object> map = new HashMap<>();
		
		map = announceService.register( dto );
		
		return ResponseEntity.ok(map);
	}

	/* 수정 */
	@PutMapping("/update")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> update( @RequestBody AnnounceRequestDto dto, HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		dto.setAdminId(loginId);
		return ResponseEntity.ok( announceService.update( dto ) );
	}

	/* 삭제 */
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, Object>> delete( @RequestParam( required =  true ) int announceId , HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		return new ResponseEntity<Map<String, Object>>( announceService.delete( announceId ), HttpStatus.OK);
	}
	
	
	@GetMapping("/add-count")
	public ResponseEntity<Map<String, Object>> addCount( @RequestParam( required = true ) int announceId, HttpServletRequest request ) {
		return new ResponseEntity<Map<String, Object>>( announceService.addCount( announceId ), HttpStatus.OK);
	}
	
	
	
	
}