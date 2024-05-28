package com.yumikorea.setting.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.mvc.dto.PageDto;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.setting.dto.request.AuthorityMenuDto;
import com.yumikorea.setting.dto.request.MenuDto;
import com.yumikorea.setting.entity.MenuEntity;
import com.yumikorea.setting.repository.MenuRepository;
import com.yumikorea.setting.repository.MenuRepositoryCustom;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository repository;
	private final MenuRepositoryCustom repositoryCustom;

	@Value("#{schedulerProperties['job-pem-path-classpath']}")
    private String classPath ;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/* 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<MenuDto> getLeftNavList(HttpServletRequest request) {
		/* 관리도구 신규기능 적용 여부 체크 */
		List<MenuDto> result = new ArrayList<>();
		Boolean isMenuChanged = false;
    	if( request.getSession().getAttribute(EAdminConstants.IS_MENU_CHANGED.getValue()) == null ) {
    		isMenuChanged = true;
    	} else {
    		isMenuChanged = (Boolean) request.getSession().getAttribute(EAdminConstants.IS_MENU_CHANGED.getValue());
    	}
    	
    	if( !isMenuChanged ) {	// 메뉴에 변화가 없으면 세션에 저장된 메뉴리스트를 반환
    		result = (List<MenuDto>) request.getSession().getAttribute(EAdminConstants.MENU_LIST.getValue());
    	} else {	// 세션에 메뉴가 없거나 메뉴가 변경되었을 때
    		List<MenuDto> depth1 = new ArrayList<>();
    		List<MenuDto> depth2 = new ArrayList<>();
    		MenuDto reqDto = new MenuDto();
    		Set<String> menuCodes = new HashSet<>();
    		
    		for(AuthorityMenuDto mn : authorityService.getAuthorityUrlList() ) {
    			menuCodes.add(mn.getMenuCode());
    		}
    		
    		reqDto.setMenuUse(EAdminConstants.STR_Y.getValue());
    		reqDto.setMenuCodes(menuCodes);
    		reqDto.setMenuDepth(1);
    		reqDto.setRows(999);
    		depth1 = repositoryCustom.findAllByCondition(reqDto);
    		reqDto.setMenuDepth(2);
    		depth2 = repositoryCustom.findAllByCondition(reqDto);
    		for( MenuDto d1 : depth1 ) {
    			result.add(d1);
    			for( MenuDto d2 : depth2 ) {
    				if( d1.getMenuCode().equals(d2.getMenuUpperCode()) ) {
    					result.add(d2);
    				}
    			}
    		}
    		
    		request.getSession().setAttribute(EAdminConstants.MENU_LIST.getValue(), result);
    		request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), false);
    	}

		return result;
	}

	/* 목록 조회 */
	public Map<String, Object> getSettingMenulist(MenuDto dto) {
		Map<String, Object> map = new HashMap<>();
		List<MenuDto> list = repositoryCustom.findAllByCondition(dto);
		int totalCnt = repositoryCustom.findAllCntByCondition(dto).intValue();
		int maxOrder = 0;
		if( totalCnt > 0 ) {
			maxOrder = repositoryCustom.findMaxOrderByCondition(dto);
		}
		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totalCnt);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.RESULT_MAP.getValue(), list);
		map.put(EAdminConstants.PAGE.getValue(), page);
		map.put(EAdminConstants.MAX_ORDER.getValue(), maxOrder);
		
		return map;
	}


	/* 등록 */
	public Map<String, String> regist(MenuEntity dto) {
		Map<String, String> map = new HashMap<>();
		
		map = this.checkDuplication(dto, EAdminConstants.INSERT.getValue());
		
		if( map.get(EAdminConstants.STATUS.getValue()).equals(EAdminConstants.FAIL.getValue()) ) {
			return map;
		}
		
		repository.save(dto);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.CREATE_SUCCESS.getMessage());
		return map;
	}
	
	/* 상태변경 */
	public Map<String, String> updateSt(String[] idArr, MenuEntity dto) {
		Map<String, String> map = new HashMap<>();
		int len = CommonUtil.getLength(idArr);
		for (int i = 0; i < len; i++) {
			String useYn = ""; 
			if( EAdminConstants.STR_Y.getValue().equalsIgnoreCase(repository.findById(idArr[i]).get().getMenuUse()) ) {
				useYn = EAdminConstants.STR_N.getValue();
			} else {
				useYn = EAdminConstants.STR_Y.getValue();
			}
			dto.setMenuUse(useYn);
			repositoryCustom.updateStById(idArr[i], dto);
		}
		
		if (len == 0 ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NO_SEARCH_RESULT.getMessage());
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
		}
		return map;
	}
	
	/* 삭제 */
	public Map<String, String> delete(String[] idArr) {
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < CommonUtil.getLength(idArr); i++) {
			MenuDto paramDto = new MenuDto();
			paramDto.setMenuCode(idArr[i]);
			paramDto.setIsSearchDownMenu(true);
			List<MenuDto> undderMenus = repositoryCustom.findAllByCondition(paramDto);
			for( MenuDto under : undderMenus ) {
				repository.deleteById(under.getMenuCode());
			}
			repository.deleteById(idArr[i]);
		}
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_SUCCESS.getMessage());
		return map;
	}
	
	/* 수정 */
	public Map<String, String> update(MenuEntity dto) {
		Map<String, String> map = new HashMap<>();
		
		map = this.checkDuplication(dto, EAdminConstants.UPDATE.getValue());
		
		if( map.get(EAdminConstants.STATUS.getValue()).equals(EAdminConstants.FAIL.getValue()) ) {
			return map;
		}
		
		repositoryCustom.updateById(dto);
		
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
		return map;
	}
	
	/* 메뉴 테이블 없을 경우 json파일을 읽어 메뉴 생성 (기존 하드코딩된 메뉴와 동일) */
    private List<MenuDto> readMenuFromJson() {
    	try {
    		ObjectMapper objectMapper = new ObjectMapper();
			Resource resource = resourceLoader.getResource(this.classPath);	// ...생략.../bin/main
			String folderPath = resource.getURL().getPath() + EAdminConstants.MENU_JSON_PATH.getValue();	// "static/json/menuList.json"
			// JSON 파일을 List로 읽어오기
			return objectMapper.readValue(new File(folderPath), new TypeReference<List<MenuDto>>(){});
		} catch (IOException e) {
			e.printStackTrace();
			return this.staticMenu();
		}
    }

    /* 메뉴순서 변경 */
	public Map<String, String> updateOrders(List<MenuEntity> list, HttpServletRequest request ) {
		Map<String, String> map = new HashMap<>();
		for ( MenuEntity dto : list ) {
			dto.setModifyId((String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue()));
			dto.setModifyDate(new Date());
			repositoryCustom.updateOrderById(dto);
		}
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
		return map;
	}
	
	/* code, url 중복체크 */
	private Map<String, String> checkDuplication(MenuEntity dto, String operation) {
		Map<String, String> map = new HashMap<>(); 
		List<MenuEntity> chkDtoList = repository.findAll();
		Boolean duplCode = false;
		Boolean duplUrl = false;
		for( MenuEntity m : chkDtoList ) {
			duplCode = m.getMenuCode().equalsIgnoreCase(dto.getMenuCode());
			duplUrl = m.getMenuUrl().equalsIgnoreCase(dto.getMenuUrl());
			if( duplCode && operation.equals(EAdminConstants.INSERT.getValue()) ) {
				
				map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
				map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.MENU_CREATE_FAIL_CODE_DUPLICATE.getMessage());
				return map;
			} else if ( !duplCode && duplUrl && operation.equals(EAdminConstants.UPDATE.getValue()) ) {
				if ( !m.getMenuUrl().equalsIgnoreCase(EAdminConstants.HASH.getValue()) ) {
					map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
					map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.MENU_CREATE_FAIL_URL_DUPLICATE.getMessage());
					return map;
				}
			}
		}
		
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		
		return map;
	}
	
	private List<MenuDto> staticMenu() {
		ObjectMapper objectMapper = new ObjectMapper();
		String toStringFromJSON = "";
		
		try {
			return objectMapper.readValue(toStringFromJSON, new TypeReference<List<MenuDto>>(){});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ArrayList<MenuDto>();
		}
	}
	
}
