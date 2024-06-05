package com.yumikorea.admin.controller;

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

import com.yumikorea.admin.dto.AdminRequestDto;
import com.yumikorea.admin.dto.AdminResponseDto;
import com.yumikorea.admin.service.AdminService;
import com.yumikorea.common.enums.EAdminConstants;

import com.yumikorea.setting.dto.request.AuthorityMenuDto;
import com.yumikorea.setting.service.AuthorityService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping( "/admin" )
public class AdminController {
	
	private final AdminService service;
	private final AuthorityService authorityService;
	
	/* 목록 조회 */
	@GetMapping( "/list" )
	public String list( Model model, AdminRequestDto dto, HttpServletRequest request ) {
		
		String sessionId = (String) request.getSession().getAttribute( EAdminConstants.LOGIN_ID.getValue() );
		
		AdminResponseDto responseDto = (AdminResponseDto) service.getDetail( sessionId ).get(EAdminConstants.RESULT_MAP.getValue());
		model.addAttribute( "admin", responseDto );
		
		model.addAllAttributes(service.getList( dto ));
		
		model.addAttribute( "srcAdminId", dto.getSrcAdminId() );
		model.addAttribute( "srcAdminName", dto.getSrcAdminName() );
		
		return "admin/adminList";
	}
	
	/* 등록 */
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Map<String, String>> register( @RequestBody AdminRequestDto dto, HttpServletRequest request ) {
		
		Map<String, String> map = new HashMap<>();
		String loginId = (String)request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		
		map = service.register( dto, loginId );
		
		return ResponseEntity.ok(map);
	}

	/* 상태 변경 */
	@PutMapping("/updateSt")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> updateSt( @RequestParam( value = "arr" ) String [] arr ) {
		return ResponseEntity.ok( service.updateSt( arr ) );
	}

	/* 삭제 */
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, Object>> delete( @RequestParam( value = "arr" ) String [] arr , HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		return new ResponseEntity<Map<String, Object>>( service.delete( arr , loginId ), HttpStatus.OK);
	}
	
	/* admin_id에 할당된 권한목록 조회 */
	@GetMapping( "/getListMyAuthority" )
	public ResponseEntity<Map<String, Object>> getListMyAuthority( HttpServletRequest request
			, @RequestParam(required = true) String adminId) {
		/* authorityListTable.html 으로 보낼 데이터 */
		return ResponseEntity.ok(authorityService.getAuthorityMyList(adminId));
	}
	
	/* 권한목록 조회 */
	@GetMapping( "/getListAuthority" )
	public ResponseEntity<Map<String, Object>> getListAuthority( HttpServletRequest request, AuthorityMenuDto dto ) {
		/* authorityList.html 으로 보낼 데이터 */
		return ResponseEntity.ok(authorityService.getAuthorityList(dto));
	}
	
	
	/* 권한-사용자 저장 */
	@PostMapping( "/reigstAuthorityUser" )
	@ResponseBody
	public ResponseEntity<Map<String, String>> reigstAuthorityUser( HttpServletRequest request, @RequestBody List<AuthorityMenuDto> list ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		return ResponseEntity.ok(authorityService.reigstAuthorityUser(list, loginId));
	}
	
	
}