package com.yumikorea.setting.controller;

import java.util.List;
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

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.mvc.controller.BasicController;
import com.yumikorea.setting.dto.request.AuthorityMenuDto;
import com.yumikorea.setting.service.AuthorityService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping( "/authority" )
public class AuthorityController extends BasicController {
	
	private final AuthorityService service;
	
	/* 페이지 이동 */
	@GetMapping( "/list" )
	public String list( HttpServletRequest request, Model model, AuthorityMenuDto dto ) {
		return "authority/authorityManagement";
	}
	
	/* 권한목록 조회 */
	@GetMapping( "/getListAuthority" )
	public ResponseEntity<Map<String, Object>> getListAuthority( HttpServletRequest request, AuthorityMenuDto dto ) {
		/* authorityList.html 으로 보낼 데이터 */
		return ResponseEntity.ok(service.getAuthorityList(dto));
	}
	
	/* 권한에 허가된 URL 목록 조회 */
	@GetMapping( "/getListAuthorityUrl" )
	public ResponseEntity<Map<String, Object>> getListAuthorityUrl( HttpServletRequest request, AuthorityMenuDto dto ) {
		/* authorityUrlList.html 으로 보낼 데이터 */
		return ResponseEntity.ok(service.getListAuthorityUrl(dto));
	}
	
	/* 유저에 허가된 권한 목록 조회 */
	@GetMapping( "/getListAuthorityUser" )
	public ResponseEntity<Map<String, Object>> getListAuthorityUser( HttpServletRequest request, AuthorityMenuDto dto ) {
		/* adminAuthorityDetail.html 으로 보낼 데이터 */
		return ResponseEntity.ok(service.getListAuthorityUser(dto));
	}
	
	
	/* 권한-화면 신규등록을 위한 메뉴리스트 조회 */
	@GetMapping( "/getAuthorityMenulist" )
	public ResponseEntity<Map<String, Object>> getAuthorityMenulist( HttpServletRequest request
			, @RequestParam(required = true) String authorityId) {
		return ResponseEntity.ok(service.getAuthorityMenulist(authorityId));
	}
	
	
	/* 권한 신규등록 */
	@PostMapping( "/registAuthority" )
	@ResponseBody
	public ResponseEntity<Map<String, String>> registAuthority( HttpServletRequest request, @RequestBody AuthorityMenuDto dto ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		dto.setModifyId(loginId);
		return ResponseEntity.ok(service.registAuthority(dto));
	}
	
	/* 권한 수정 */
	@PutMapping( "/updateAuthority" )
	@ResponseBody
	public ResponseEntity<Map<String, String>> updateAuthority( HttpServletRequest request, @RequestBody AuthorityMenuDto dto ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		dto.setModifyId(loginId);
		return ResponseEntity.ok(service.updateAuthority(dto));
	}
	
	/* 권한-사용자 삭제 :: 저장에서 같이 처리하도록 변경 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@DeleteMapping( "/deleteAuthorityUser" )
	public ResponseEntity<Map<String, String>> deleteAuthorityUser( HttpServletRequest request, @RequestBody List<AuthorityMenuDto> list ) {
		return ResponseEntity.ok(service.deleteAuthorityUser(list));
	}
	
	/* 권한 삭제 */
	@DeleteMapping( "/deleteAuthority" )
	public ResponseEntity<Map<String, String>> deleteAuthority( HttpServletRequest request, @RequestBody List<String> authorityIdList ) {
		return ResponseEntity.ok(service.deleteAuthority(authorityIdList));
	}
	
	
	/* URL 메소드 수정 */
	@PutMapping( "/updateMethods" )
	@ResponseBody
	public ResponseEntity<Map<String, String>> updateMethods( HttpServletRequest request, @RequestBody List<AuthorityMenuDto> dtoList ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		return ResponseEntity.ok(service.updateMethods(dtoList, loginId));
	}
	
	
	/* 권한-화면 신규등록 */
	@PostMapping( "/registAuthorityUrl" )
	@ResponseBody
	public ResponseEntity<Map<String, String>> registAuthorityUrl( HttpServletRequest request, @RequestBody AuthorityMenuDto dto ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		dto.setModifyId(loginId);
		return ResponseEntity.ok(service.registAuthorityUrl(dto));
	}
	
	/* 권한-화면 삭제 */
	@DeleteMapping( "/deleteAuthorityUrl" )
	@ResponseBody
	public ResponseEntity<Map<String, String>> deleteAuthorityUrl( HttpServletRequest request, @RequestBody List<AuthorityMenuDto> dtoList ) {
		return ResponseEntity.ok(service.deleteAuthorityUrl(dtoList));
	}
	
}