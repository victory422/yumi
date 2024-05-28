package com.yumikorea.setting.controller;

import java.util.Date;
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

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.mvc.controller.BasicController;
import com.yumikorea.setting.dto.request.MenuDto;
import com.yumikorea.setting.entity.MenuEntity;
import com.yumikorea.setting.service.MenuService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping( "/menu" )
public class MenuController extends BasicController {
	
	private final MenuService service;
	
	/* 페이지 이동 */
	@GetMapping( "/list" )
	public String list( HttpServletRequest request ) {
		return "setting/menuList";
	}
	
	/* 조건별 목록 조회 */
	@GetMapping( "/getSettingMenulist" )
	public ResponseEntity<Map<String, Object>> getSettingMenulist( MenuDto dto, HttpServletRequest request ) {
		Map<String, Object> map = service.getSettingMenulist(dto);
		return ResponseEntity.ok(map);
	}
	
	/* 등록 */
	@PostMapping("/regist")
	@ResponseBody
	public ResponseEntity<Map<String, String>> regist( @RequestBody MenuEntity dto, HttpServletRequest request ) {
		dto.setModifyId((String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue()));
		dto.setModifyDate(new Date());
		request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		return ResponseEntity.ok(service.regist(dto));
	}
	
	/* 수정 */
	@PutMapping("/update")
	public ResponseEntity<Map<String, String>> update( @RequestBody MenuEntity dto, HttpServletRequest request ) {
		dto.setModifyId((String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue()));
		dto.setModifyDate(new Date());
		request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		return ResponseEntity.ok(service.update( dto ));
	}
	
	/* 상태변경 */
	@PutMapping("/updateSt")
	public ResponseEntity<Map<String, String>> updateSt( @RequestParam( value = "arr" ) String [] arr, HttpServletRequest request ) {
		MenuEntity dto = new MenuEntity();
		dto.setModifyId((String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue()));
		dto.setModifyDate(new Date());
		request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		return ResponseEntity.ok(service.updateSt( arr , dto ));
	}
	
	/* 순서 수정 */
	@PutMapping("/updateOrders")
	public ResponseEntity<Map<String, String>> updateOrders( @RequestBody List<MenuEntity> dto, HttpServletRequest request ) {
		request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		return ResponseEntity.ok(service.updateOrders( dto, request ));
	}

	
	
	/* 삭제 */
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> delete( @RequestParam( value = "arr" ) String [] arr, HttpServletRequest request ) {
		request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		return ResponseEntity.ok(service.delete( arr ));
	}

}