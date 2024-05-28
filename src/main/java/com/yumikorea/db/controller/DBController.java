package com.yumikorea.db.controller;

import java.util.HashMap;
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

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.mvc.controller.BasicController;
import com.yumikorea.db.dto.DBRequestDto;
import com.yumikorea.db.service.DBService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping( "/db" )
public class DBController extends BasicController {
	
	private final DBService service;
	
	/* 목록 조회 */
	@GetMapping( "/list" )
	public String list( Model model, DBRequestDto dto, HttpServletRequest request ) {
		return "db/dbManagement";
	}
	
	/* 등록 */
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> register( @RequestBody DBRequestDto dto, HttpServletRequest request ) {
		String token = (String) request.getSession().getAttribute( EAdminConstants.TOKEN.getValue() );
		
		Map<String, Object> map = new HashMap<>();
		
		map = service.register( dto, token );
		
		return ResponseEntity.ok(map);
	}

	/* 상태 변경 */
	@PutMapping("/updateSt")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> updateSt( @RequestParam( value = "arr" ) String [] arr ) {
		return ResponseEntity.ok( service.updateSt( arr ) );
	}
	
	/* 수정 */
	@PutMapping("/update")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> update( @RequestBody DBRequestDto dto, HttpServletRequest request ) {
		return ResponseEntity.ok( service.update( dto ) );
	}

	/* 삭제 */
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, Object>> delete( @RequestParam( value = "arr" ) String [] arr , HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		return new ResponseEntity<Map<String, Object>>( service.delete( arr , loginId ), HttpStatus.OK);
	}
	
	
	
	
}