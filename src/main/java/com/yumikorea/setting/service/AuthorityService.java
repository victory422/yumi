package com.yumikorea.setting.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.mvc.dto.PageDto;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.setting.dto.request.AuthorityMenuDto;
import com.yumikorea.setting.dto.request.MenuDto;
import com.yumikorea.setting.entity.AuthorityEntity;
import com.yumikorea.setting.entity.AuthorityUrlEntity;
import com.yumikorea.setting.entity.AuthorityUserEntity;
import com.yumikorea.setting.entity.MenuEntity;
import com.yumikorea.setting.repository.AuthorityRepository;
import com.yumikorea.setting.repository.AuthorityRepositoryCustom;
import com.yumikorea.setting.repository.AuthorityUrlRepository;
import com.yumikorea.setting.repository.AuthorityUrlRepositoryCustom;
import com.yumikorea.setting.repository.AuthorityUserRepository;
import com.yumikorea.setting.repository.AuthorityUserRepositoryCustom;
import com.yumikorea.setting.repository.MenuRepository;
import com.yumikorea.setting.repository.MenuRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorityService {

	private final AuthorityRepository authorityRepository;
	private final AuthorityRepositoryCustom authorityRepositoryCustom;
	
	private final AuthorityUrlRepository urlRepository;
	private final AuthorityUrlRepositoryCustom urlRepositoryCustom;
	
	private final AuthorityUserRepository userRepository;
	private final AuthorityUserRepositoryCustom userRepositoryCustom;
	
	private final MenuRepository menuRepository;
	private final MenuRepositoryCustom menuRepositoryCutom;
	
	@Autowired
	private final HttpServletRequest request;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private final Logger logger = LoggerFactory.getLogger( AuthorityService.class );
	
	/* Authority 목록 조회 */
	public Map<String, Object> getAuthorityList(AuthorityMenuDto dto) {
		Map<String, Object> map = new HashMap<>();
		
		Long totalCnt = authorityRepositoryCustom.findCntByCondition(dto);
		List<AuthorityMenuDto> list = authorityRepositoryCustom.findAllByCondition(dto);
		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totalCnt.intValue());
		map.put("listAuthority", list);
		map.put("page1", page);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		return map;
	}
	
	/* 사용자가 가진 Authority 목록 조회 */
	public Map<String, Object> getAuthorityMyList(String adminId) {
		Map<String, Object> map = new HashMap<>();
		List<String> idList = new ArrayList<>(); 
		List<String> nameList = new ArrayList<>(); 
		
		/* admin 관리도구 신규기능 지원 체크 */
//		if( !applicationContext.getBean(EAdminConstants.IS_NEW_ADMIN.getValue(), Boolean.class) ) {
//			map.put("listAuthority", idList);
//			map.put("listAuthorityName", nameList);
//			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
//			return map;
//		}
		
		for( AuthorityMenuDto list : authorityRepositoryCustom.findAllByAdminId(adminId) ) {
			idList.add(list.getAuthorityId());
			nameList.add("[" + list.getAuthorityName() + "]");
		}
		map.put("listAuthority", idList);
		map.put("listAuthorityName", nameList);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		return map;
	}
	
	/* 권한에 허가된 URL 목록 조회 */
	public Map<String, Object> getListAuthorityUrl(AuthorityMenuDto dto) {
		Map<String, Object> map = new HashMap<>();
		int totalCnt = urlRepositoryCustom.findCntAuthorityAndMenuByCondition(dto).size();
		List<AuthorityMenuDto> amdList = urlRepositoryCustom.findAuthorityAndMenuByCondition(dto);
		
		for( AuthorityMenuDto amd : amdList ) {
			amd.setMethods(urlRepositoryCustom.findMethods(amd));
		}
		
		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totalCnt);
		map.put("listAuthorityUrl", amdList);
		map.put("page2", page);
		map.put("srcKeyword",  dto);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		
		return map;
	}
	
	/* 유저에 허가된 권한 목록 조회 */
	public Map<String, Object> getListAuthorityUser(AuthorityMenuDto dto) {
		Map<String, Object> map = new HashMap<>();
		
		Long totalCnt = userRepositoryCustom.findCntAuthorityAndMenuByCondition(dto);
		List<AuthorityMenuDto> list = authorityRepositoryCustom.findAllByCondition(dto);
		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totalCnt.intValue());
		map.put("listAuthorityUser", list);
		map.put(EAdminConstants.PAGE.getValue(), page);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		return map;
	}
	
	/* 권한-화면 신규등록을 위한 메뉴리스트 조회 */
	public Map<String, Object> getAuthorityMenulist(String authorityId) {
		Map<String, Object> map = new HashMap<>();
		
		List<MenuDto> dtoList = menuRepositoryCutom.findAllByException(authorityId);
		
		map.put(EAdminConstants.MENU_LIST.getValue(), dtoList);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());
		
		return map;
	}

	/* commonJs.js에서 호출된 화면 권한 목록 */
	public Object[] getScreenAuthorityList(String thisMenuCode) {
		Set<String> rst = new HashSet<>();
		
		/* admin 관리도구 신규기능 지원 체크 */
//		if( !applicationContext.getBean(EAdminConstants.IS_NEW_ADMIN.getValue(), Boolean.class) ) {
//		if( true ) {
//			rst.add(EAdminConstants.HTTP_METHOD_GET.getValue());
//			rst.add(EAdminConstants.HTTP_METHOD_POST.getValue());
//			rst.add(EAdminConstants.HTTP_METHOD_PUT.getValue());
//			rst.add(EAdminConstants.HTTP_METHOD_DELETE.getValue());
//			rst.add(EAdminConstants.HTTP_METHOD_UPLOAD.getValue());
//			rst.add(EAdminConstants.HTTP_METHOD_DOWNLOAD.getValue());
//			return rst.toArray();
//		}
		
		Set<AuthorityMenuDto> amdList = this.getAuthorityUrlList();
		for( AuthorityMenuDto dto : amdList ) {
			if( dto.getMenuCode().equals(thisMenuCode) ) {
				rst.add(dto.getMethod());
			}
		}
		rst.add(EAdminConstants.COMMON.getValue().toUpperCase());
		return rst.toArray();
	}

	/* 사용자 권한 체크를 위한 URL LIST 조회 */
	@SuppressWarnings("unchecked")
	public Set<AuthorityMenuDto> getAuthorityUrlList() {
		String loginId = (String) this.request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		Set<AuthorityMenuDto> amdList = new HashSet<>();
		if( CommonUtil.isNull(loginId) ) return amdList;
		
		Boolean isAuthUrlChanged = false;
		if( request.getSession().getAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue()) == null ) {
			isAuthUrlChanged = true;
		} else {
			isAuthUrlChanged = (Boolean) request.getSession().getAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue());
		}
		
		Object tempObj = this.request.getSession().getAttribute(EAdminConstants.AUTHORITY_URL_LIST.getValue());
		if( tempObj != null && !isAuthUrlChanged ) {
			amdList = (Set<AuthorityMenuDto>) tempObj; 
		} else {
			
			if( loginId.equals(EAdminConstants.SUPER_ACCOUNT.getValue()) ) {
				amdList.addAll(this.setSuperAuthority(amdList));
			}else {
				/*
				 * 로그인ID로 AUTHORITY_USER 에서 권한을 검색
				 * 해당 권한으로 AUTHORITY_URL을 검색
				 * */
				AuthorityMenuDto reqDto = new AuthorityMenuDto();
				reqDto.setLoginId(loginId);
				Boolean hasHomeAuthority = false;
				for( AuthorityMenuDto amd : userRepositoryCustom.findAuthorityAndMenuByCondition(reqDto) ) {
					amd.setIsGetChild(false);
					amdList.add(amd);
					if( !EAdminConstants.HOME.getValue().equalsIgnoreCase(amd.getMenuUpperCode()) ) {
						Optional<MenuEntity> menuOptional = menuRepository.findById(amd.getMenuUpperCode());
						if( menuOptional.isPresent() ) {
							MenuEntity menuEntity = menuOptional.get();
							AuthorityMenuDto nAmd = new AuthorityMenuDto();
							nAmd.setMenuCode(menuEntity.getMenuCode());
							nAmd.setMenuUrl(menuEntity.getMenuUrl());
							nAmd.setMethod(EAdminConstants.HTTP_METHOD_GET.getValue());
							nAmd.setIsGetChild(true);
							nAmd.setMenuUpperCode(menuEntity.getMenuUpperCode());
							amdList.add(nAmd);
						}
					}
					
					if( amd.getMenuAttribute2() != null ) {
						hasHomeAuthority = true;
					}
				}
				
				if( !hasHomeAuthority ) {
					MenuDto param = new MenuDto();
					param.setMenuAttribute2(EAdminConstants.HOME.getValue());
					MenuDto menu = menuRepositoryCutom.findOneByAttribute(param);
					AuthorityMenuDto nAmd = new AuthorityMenuDto();
					nAmd.setMenuCode(menu.getMenuCode());
					nAmd.setMenuUrl(menu.getMenuUrl());
					nAmd.setMethod(EAdminConstants.HTTP_METHOD_GET.getValue());
					nAmd.setIsGetChild(false);
					amdList.add(nAmd);
				}
			}
			
			this.request.getSession().setAttribute(EAdminConstants.AUTHORITY_URL_LIST.getValue(), amdList);
			this.request.getSession().setAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue(), false);
		}
	
		return amdList;
	}

	/* 사용자 권한-요청 request 간 권한 체크 WebInterceptor.java 에서 호출 */
	public Boolean isAuthorityValid() {
		String requestUrl = this.request.getRequestURI();
		String method = this.request.getMethod();
		/*
		 * 로그인ID로 AUTHORITY_USER 에서 권한을 검색
		 * 해당 권한으로 AUTHORITY_URL을 검색, 유효한 url인지 체크
		 * */
		Set<AuthorityMenuDto> amdList = this.getAuthorityUrlList();
		String checkUrlPrefix = requestUrl.split("/").length > 1 ? requestUrl.split("/")[1] : "" ;
		String message = "";
		boolean result = false;
		String menuName = "";
		for( AuthorityMenuDto amd : amdList ) {
			if( !amd.getIsGetChild() && !amd.getMenuUrl().equals(EAdminConstants.HASH.getValue()) ) {
				
				// URL PATH로 화면(메뉴)을 특정하기 위함
				if( amd.getMenuUrl().equals("/") || amd.getMenuUrl().equals("") ) amd.setMenuUrl("/dashboard");
				
				String menuUrlPrefix = amd.getMenuUrl().split("/")[1];
				if( menuUrlPrefix.equals(checkUrlPrefix) ) {
					// download 일 때는 http method와 관계없이 체크하는 로직 추가
					if( requestUrl.toUpperCase().indexOf(EAdminConstants.HTTP_METHOD_DOWNLOAD.getValue()) > -1 ) {
						if( amd.getMethod().equalsIgnoreCase(EAdminConstants.HTTP_METHOD_DOWNLOAD.getValue()) ) {
							this.request.getSession().setAttribute(EAdminConstants.MESSAGE.getValue(), "");
							logger.info("============= this request is vaild authoirty (true) =============");
							menuName = amd.getMenuName();
							result = true;
						}
						// 일반적인 요청과 get, post, put, delete 의 메소드를 처리 
					} else if ( amd.getMethod().equalsIgnoreCase(method) ) {
						this.request.getSession().setAttribute(EAdminConstants.MESSAGE.getValue(), "");
						logger.info("============= this request is vaild authoirty (true) =============");
						menuName = amd.getMenuName();
						result = true;
					} else {
						message = String.format("No have authority about this URL. method : {}", method) ;
					}
				} else {
					message = String.format("No have authority about this URL.") ;
				}
			}
		}
		if( result ) {
			this.request.getSession().setAttribute(EAdminConstants.MENU_NAME.getValue(), menuName);
		} else {
			this.request.getSession().setAttribute(EAdminConstants.MESSAGE.getValue(), message);
			
		}
		
		return result;
	}

	/* 권한 신규등록 */
	public Map<String, String> registAuthority(AuthorityMenuDto dto) {
		Map<String, String> rst = new HashMap<>();
		if( !authorityRepository.findById(dto.getAuthorityId()).isPresent() ) {
			AuthorityEntity insertDto = new AuthorityEntity();
			insertDto.setAuthorityId(dto.getAuthorityId());
			insertDto.setAuthorityName(dto.getAuthorityName());
			insertDto.setAuthorityDesc(dto.getAuthorityDesc());
			insertDto.setModifyDate(new Date());
			insertDto.setModifyId(dto.getModifyId());
			authorityRepository.save(insertDto);
			
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());
		} else {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.ID_DUPLICATE.getMessage());
		}
		return rst;
	}

	
	/* 권한 수정 */
	public Map<String, String> updateAuthority(AuthorityMenuDto dto) {
		Map<String, String> rst = new HashMap<>();
		if( authorityRepository.findById(dto.getAuthorityId()).isPresent() ) {
			AuthorityEntity insertDto = new AuthorityEntity();
			insertDto.setAuthorityId(dto.getAuthorityId());
			insertDto.setAuthorityName(dto.getAuthorityName());
			insertDto.setAuthorityDesc(dto.getAuthorityDesc());
			insertDto.setModifyDate(new Date());
			insertDto.setModifyId(dto.getModifyId());
			authorityRepository.save(insertDto);
			
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
		} else {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_FAIL.getMessage());
		}
		return rst;
	}

	/* 권한 삭제 */
	public Map<String, String> deleteAuthority(List<String> authorityIdList) {
		Map<String, String> rst = new HashMap<>();
		int cnt = 0;
		for( String authorityId : authorityIdList ) {
			if( authorityRepository.findById(authorityId).isPresent() ) {
				authorityRepository.deleteById(authorityId);
				cnt++;
			}
		}
		
		if(cnt == authorityIdList.size() ) {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_SUCCESS.getMessage());
			this.request.getSession().setAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue(), true);
			this.request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		} else {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_FAIL.getMessage());
		}
		
		return rst;
	}

	/* 권한-화면 신규등록 */
	public Map<String, String> registAuthorityUrl(AuthorityMenuDto reqDto) {
		Map<String, String> rst = new HashMap<>();
		AuthorityUrlEntity dto = new AuthorityUrlEntity();
		dto.setAuthorityId(reqDto.getAuthorityId());
		dto.setMenuCode(reqDto.getMenuCode());
		dto.setModifyDate(new Date());
		dto.setModifyId(reqDto.getLoginId());
		
		// 모든 화면 등록
		if( EAdminConstants.ALL.getValue().equalsIgnoreCase(reqDto.getOperation()) ) {
			
			List<String> allMenuCodes = urlRepositoryCustom.findNotHasAuthorityMenus(reqDto);
			for( String menuCode : allMenuCodes ) {
				dto.setMenuCode(menuCode);
				for( String method : reqDto.getMethods()) {
					dto.setMethod(method);
					urlRepository.save(dto);
				}
			}
			
			if( !CommonUtil.isNull(reqDto.getMenuCode()) ) {
				dto.setMenuCode(reqDto.getMenuCode());
				urlRepository.save(dto);
			}
			
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());
			
			this.request.getSession().setAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue(), true);
			this.request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		} else if( urlRepositoryCustom.findByPk(reqDto) == null ) {
			for( String method : reqDto.getMethods()) {
				dto.setMethod(method);
				urlRepository.save(dto);
			}
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());
			
			this.request.getSession().setAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue(), true);
			this.request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		} else {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_FAIL.getMessage());
		}
		
		return rst;
	}

	/* URL 메소드 수정 */
	public Map<String, String> updateMethods(List<AuthorityMenuDto> dtoList, String loginId) {
		Map<String, String> rst = new HashMap<>();
		int cnt = 0;
		for( AuthorityMenuDto dto : dtoList ) {
			AuthorityUrlEntity url = new AuthorityUrlEntity();
			url.setAuthorityId(dto.getAuthorityId());
			url.setMenuCode(dto.getMenuCode());
			url.setMethod(dto.getMethod());
			url.setModifyDate(new Date());
			url.setModifyId(loginId);
			if( dto.getOperation().equalsIgnoreCase(EAdminConstants.INSERT.getValue()) ) {
				urlRepository.save(url);
				cnt++;
			} else if( dto.getOperation().equalsIgnoreCase(EAdminConstants.DELETE.getValue())) {
				urlRepository.delete(url);
				cnt++;
			}
		}
		
		if( cnt == dtoList.size() ) {
			
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
			
			this.request.getSession().setAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue(), true);
			this.request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
		} else {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_FAIL.getMessage());
		}
		
		return rst;	
	}

	/* 권한-화면 삭제 */
	public Map<String, String> deleteAuthorityUrl(List<AuthorityMenuDto> dtoList) {
		Map<String, String> rst = new HashMap<>();
		int cnt = 0;
		for( AuthorityMenuDto ids : dtoList ) {
			urlRepositoryCustom.deleteMenuUrl(ids.getAuthorityId(), ids.getMenuCode());
			cnt++;
		}
		
		if( cnt == dtoList.size() ) {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_SUCCESS.getMessage());
			
			this.request.getSession().setAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue(), true);
			this.request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
			
		} else {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_FAIL.getMessage());
		}
		
		return rst;	
	}

	/* 권한-사용자 등록 */
	public Map<String, String> reigstAuthorityUser(List<AuthorityMenuDto> list, String loginId) {
		Map<String, String> rst = new HashMap<>();
		int cnt = 0;
		for( AuthorityMenuDto dto : list ) {
			AuthorityUserEntity reqDto = new AuthorityUserEntity();
			reqDto.setAuthorityId(dto.getAuthorityId());
			reqDto.setAdminId(dto.getAdminId());
			reqDto.setModifyId(loginId);
			reqDto.setModifyDate(new Date());
			if( dto.getOperation().equals(EAdminConstants.INSERT.getValue()) ) {
				userRepository.save(reqDto);
				cnt++;
			} else if( dto.getOperation().equals(EAdminConstants.DELETE.getValue()) ) {
				userRepository.delete(reqDto);
				cnt++;
			}
		}

		if( cnt == list.size() ) {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());
			
			this.request.getSession().setAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue(), true);
			this.request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
			
		} else {
			rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_FAIL.getMessage());
		}
		
		return rst;
	}
	
	/* 권한-사용자 삭제 */
	@Deprecated
	public Map<String, String> deleteAuthorityUser(List<AuthorityMenuDto> list) {
		Map<String, String> rst = new HashMap<>();
		for( AuthorityMenuDto dto : list ) {
			// 복합키 PK 조회 로직
			if ( userRepositoryCustom.findAuthorityUserByPK(dto.getAuthorityId(), dto.getAdminId()).intValue() == 1 ) {
				AuthorityUserEntity deleteDto = new AuthorityUserEntity();
				deleteDto.setAuthorityId(dto.getAuthorityId());
				deleteDto.setAdminId(dto.getAdminId());
				userRepository.delete(deleteDto);
				
				rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
				rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_SUCCESS.getMessage());
				
				this.request.getSession().setAttribute(EAdminConstants.IS_AUTHORITY_URL_LIST_CHANGE.getValue(), true);
				this.request.getSession().setAttribute(EAdminConstants.IS_MENU_CHANGED.getValue(), true);
				
			} else {
				rst.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
				rst.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_FAIL.getMessage());
			}
			
		}
		return rst;
	}
	
	/* yumi-korea 슈퍼권한일 때 */
	private Set<AuthorityMenuDto> setSuperAuthority(Set<AuthorityMenuDto> amdList) {
		Set<AuthorityMenuDto> rstList = new HashSet<>();
		
		MenuDto paramDto = new MenuDto();
		paramDto.setMenuUse(EAdminConstants.STR_Y.getValue());
		paramDto.setRows(999);
		List<MenuDto> menuList = menuRepositoryCutom.findAllByCondition(paramDto);
		
		String[] methods = {
				EAdminConstants.HTTP_METHOD_GET.getValue(), EAdminConstants.HTTP_METHOD_POST.getValue(), 
				EAdminConstants.HTTP_METHOD_PUT.getValue(), EAdminConstants.HTTP_METHOD_DELETE.getValue(), 
				EAdminConstants.HTTP_METHOD_DOWNLOAD.getValue(), EAdminConstants.HTTP_METHOD_UPLOAD.getValue() 
		};
		
		for( MenuDto m : menuList ) {
			for( String method : methods  ) {
				AuthorityMenuDto rst = new AuthorityMenuDto();
				rst.setMenuCode(m.getMenuCode());
				rst.setMenuUrl(m.getMenuUrl());
				rst.setMenuUpperCode(m.getMenuUpperCode());
				rst.setMenuAttribute2(m.getMenuAttribute2());
				rst.setIsGetChild(m.getIsGetChild());
				rst.setMethod(method);
				rstList.add(rst);
			}
		}
		return rstList;
	}
}