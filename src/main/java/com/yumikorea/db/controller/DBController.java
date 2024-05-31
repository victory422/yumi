package com.yumikorea.db.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.db.dto.DBRequestDto;
import com.yumikorea.db.dto.MemoRequestDto;
import com.yumikorea.db.service.DBService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping( "/db" )
public class DBController {
	
	private final DBService service;
	
	/* 화면이동 */
	@GetMapping( "/list" )
	public String goPage( Model model, DBRequestDto dto, HttpServletRequest request ) {
		return "db/dbManagement";
	}
	
	/* 목록 조회 */
	@GetMapping( "/get-list" )
	public ResponseEntity<Map<String, Object>> getList( DBRequestDto dto, HttpServletRequest request ) {
		return ResponseEntity.ok(service.getList( dto ));
	}
	
	/* 등록 */
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> register( @RequestBody DBRequestDto dto, HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute( EAdminConstants.LOGIN_ID.getValue() );
		dto.setAdminId(loginId);
		return ResponseEntity.ok(service.register( dto ));
	}

	/* 수정 */
	@PutMapping("/update")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> update( @RequestBody DBRequestDto dto, HttpServletRequest request ) {
		return ResponseEntity.ok( service.update( dto ) );
	}

	/* 삭제 */
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, Object>> delete( @RequestBody List<DBRequestDto> dtoList , HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		return new ResponseEntity<Map<String, Object>>( service.delete( dtoList , loginId ), HttpStatus.OK);
	}
	
	/* 메모 등록 */
	@PostMapping("/registMemo")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> registMemo( @RequestBody MemoRequestDto dto, HttpServletRequest request ) {
		String loginId = (String) request.getSession().getAttribute( EAdminConstants.LOGIN_ID.getValue() );
		dto.setAdminId(loginId);
		return ResponseEntity.ok(service.registMemo( dto ));
	}
	
	
	
	/* 메모 목록 조회 */
	@GetMapping( "/get-list-memo" )
	public ResponseEntity<Map<String, Object>> getListMemo( MemoRequestDto dto, HttpServletRequest request ) {
		return ResponseEntity.ok(service.getListMemo( dto ));
	}
	
	
}