package com.yumikorea.dashboard.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yumikorea.dashboard.dto.serivce.DashboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class DashboardController {
	
	private final DashboardService service;
	
	@GetMapping
	public String dashboard() {
		return "dashboard/dashboard";
	}
	
	
	@RequestMapping("/dashboard")
	public String defaultMapping(HttpServletRequest request) {
		return "dashboard/dashboard";
	}
	
	
	
	//사용자 메뉴별 요청 명령어 통계
	@GetMapping("/dashboard/admin_request")
	public ResponseEntity<Map<String, Object>> create( HttpServletRequest request ) {
//		
		Map<String, Object> map = null;
		return ResponseEntity.ok(map);
	}
	
	//서버 시간
	@GetMapping("/dashboard/server_timer")
	public ResponseEntity<Map<String, Object>> server_timer( HttpServletRequest request ) {
		Map<String, Object> map = service.getSvrCurTime();
		return ResponseEntity.ok(map);
	}
	
	//CPU % 이용률
	@GetMapping("/dashboard/cpu")
	public ResponseEntity<Map<String, Object>> cpu( HttpServletRequest request ) throws InterruptedException {
		Map<String, Object> map = service.getCpu();
		return ResponseEntity.ok(map);
	}
	
	//메모리 % 이용률
	@GetMapping("dashboard/memory")
	public ResponseEntity<Map<String, Object>> memory( HttpServletRequest request ) {
		Map<String, Object> map = service.getMem();
		return ResponseEntity.ok(map);
	}
	
	//Driver % 이용률
	@GetMapping("dashboard/driver")
	public ResponseEntity<Map<String, Object>> driver( HttpServletRequest request ) {
		Map<String, Object> map = service.getDriver();
		return ResponseEntity.ok(map);
	}
	
	

	// 목록 조회
//	@GetMapping("/list")
//	public String list(PolicyRequestDto dto, Model model) {
//		Map<String, Object> map = service.getList(dto);
//		
//		model.addAttribute( "list", map.get("list") );
//		model.addAttribute( EAdminConstants.PAGE.getValue(), map.get(EAdminConstants.PAGE.getValue()) );
//		
//		model.addAttribute("srcKeyword", dto);
//		
//		return "policy/policyList";
//	}
//
//	// 등록
//	@PostMapping("/register")
//	@ResponseBody
//	public ResponseEntity<Map<String,String>> register(@RequestBody PolicyRequestDto dto, HttpServletRequest request) {
//		String regId = (String) request.getSession().getAttribute("loginId");
//		dto.setRegId(regId);
//		Map<String,String> rstMap = service.register(dto);
//		return ResponseEntity.ok( rstMap );
//	}

}
