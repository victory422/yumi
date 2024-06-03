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
		model.addAttribute("srcKeyword", dto);

		return "announce/announceList";
	}
	
	/* 등록 */
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> register( @RequestBody AnnounceRequestDto dto, HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute( EAdminConstants.LOGIN_ID.getValue() );
		
		Map<String, Object> map = new HashMap<>();
		
		map = announceService.register( dto, loginId );
		
		return ResponseEntity.ok(map);
	}

	/* 상태 변경 */
	@PutMapping("/updateSt")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> updateSt( @RequestParam( value = "arr" ) String [] arr ) {
		return ResponseEntity.ok( announceService.updateSt( arr ) );
	}
	
	/* 수정 */
	@PutMapping("/update")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> update( @RequestBody AnnounceRequestDto dto, HttpServletRequest request ) {
		return ResponseEntity.ok( announceService.update( dto ) );
	}

	/* 삭제 */
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, Object>> delete( @RequestParam( value = "arr" ) String [] arr , HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		return new ResponseEntity<Map<String, Object>>( announceService.delete( arr , loginId ), HttpStatus.OK);
	}
	
	
	
	
}