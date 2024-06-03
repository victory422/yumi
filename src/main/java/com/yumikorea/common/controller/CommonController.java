package com.yumikorea.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumikorea.admin.dto.AdminRequestDto;
import com.yumikorea.admin.service.AdminService;
import com.yumikorea.code.dto.request.CodeDetailRequestDto;
import com.yumikorea.code.service.CodeDetailService;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.login.dto.LoginRequestDto;
import com.yumikorea.login.service.SecurityLoginService;
import com.yumikorea.setting.dto.request.MenuDto;
import com.yumikorea.setting.service.AuthorityService;
import com.yumikorea.setting.service.MenuService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping( "/common" )
public class CommonController {
	
	private final AuthorityService authorityService;
	private final AdminService adminService;
	private final SecurityLoginService securityLoginService;
	private final MenuService menuService;
	private final CodeDetailService codeDetailService;
	
	
	/* commonJs.js에서 호출된 화면 권한 목록 */
	@GetMapping( "/getScreenAuthorityList/{thisMenuCode}" )
	public ResponseEntity<Object[]> getScreenAuthorityList( HttpServletRequest request, @PathVariable String thisMenuCode) {
		return ResponseEntity.ok(authorityService.getScreenAuthorityList(thisMenuCode));
	}
	/* 메뉴 구성을 위한 조회 (계층에 따른 list 순서처리) */
	@GetMapping( "/getMenuList" )
	public ResponseEntity<List<MenuDto>> getLeftNavList( HttpServletRequest request ) {
		List<MenuDto> responseDto = menuService.getLeftNavList(request);
		return ResponseEntity.ok(responseDto);
	}
	
	
	/* 사용자정보 session값 load */
	@GetMapping(value = "/getLoginInfo")
	public ResponseEntity<Map<String,String>> getLoginInfo(HttpServletRequest request) {
		Map<String,String> rstMap = new HashMap<>(); 
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		if( CommonUtil.isNull(loginId) ) {
			loginId = (String) request.getParameter(EAdminConstants.LOGIN_ID.getValue());
		}
		
		rstMap.put(EAdminConstants.SESSION_ID.getValue(), request.getSession().getId());
		rstMap.put(EAdminConstants.USER_NAME.getValue(), (String) request.getSession().getAttribute(EAdminConstants.USER_NAME.getValue()));
		rstMap.put(EAdminConstants.LOGIN_ID.getValue(), loginId);
		return ResponseEntity.ok(rstMap);
	}
	
	/* 최초 로그인 비밀번호 수정 */
	@GetMapping("/initPw")
	public String initPw() {
		return "common/initPassword";
	}
	
	/* 최초 로그인 비밀번호 수정 */
	@PutMapping(value = "/updateInitPw")
	public ResponseEntity<Map<String,String>> updateInitPw(@RequestBody LoginRequestDto requestDto, HttpServletRequest request) {
		Map<String,String> rstMap = securityLoginService.updateInitPw(requestDto, request.getSession().getId());
		
		return ResponseEntity.ok(rstMap);
	}
	
	/* 내정보 보기 */
	@GetMapping("/myInfo")
	public ResponseEntity<Map<String, Object>> myInfo( HttpServletRequest request ){
		
		String loginId = (String) request.getSession().getAttribute( EAdminConstants.LOGIN_ID.getValue() );
		Map<String, Object> rstMap = adminService.getDetail( loginId ) ;
		rstMap.putAll(authorityService.getAuthorityMyList(loginId));
		return ResponseEntity.ok( rstMap );
	}
	
	/* 내 정보 수정 */
	@ResponseBody
	@PutMapping("/updateMyInfo")
	public ResponseEntity<Map<String, String>> updateMyInfo(@RequestBody AdminRequestDto dto, HttpServletRequest request ) throws Exception {
		return ResponseEntity.ok(adminService.udpateMyInfo( dto, request ));
	}
	
	/* 내 비밀번호 변경 */
	@ResponseBody
	@PutMapping("/updateMyPw")
	public Map<String, String> updateMyPw(@RequestBody AdminRequestDto dto,  HttpServletRequest request ) {
		HttpSession session = request.getSession();
		
		String loginId = (String) session.getAttribute( EAdminConstants.LOGIN_ID.getValue() );
		
		Map<String, String> map = adminService.updateMyPw( dto, loginId, session.getId() );
		
		// 비밀번호 변경 성공시 재로그인해야하므로 세션 삭제
//		if( map.get( EAdminConstants.STATUS.getValue() ).equals( EAdminConstants.SUCCESS.getValue() ) ) {
//			session.invalidate();
//		} 
		
		return map;
	}
	
	/* commonJs.js에서 호출된 화면 권한 목록 */
	@GetMapping( "/getCodeDetail" )
	public ResponseEntity<Map<String,Object>> getCodeDetail( HttpServletRequest request, CodeDetailRequestDto codeDetailDto) {
		Map<String,Object> map = new HashMap<>();
		codeDetailDto.setSrcEnable(EAdminConstants.STR_Y.getValue());
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.RESULT_MAP.getValue(), codeDetailService.getList(codeDetailDto));
		return ResponseEntity.ok(map);
	}
	
}
