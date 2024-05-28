package com.yumikorea.common.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yumikorea.admin.dto.SecurityAdminResponseDto;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.setting.service.AuthorityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebInterceptor implements HandlerInterceptor {
	
	private final Logger logger = LoggerFactory.getLogger( WebInterceptor.class );
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private AuthorityService authorityService;
	
	private String bestMatchingPattern;
	private String bestMatchingHandler;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
		bestMatchingPattern = (String) request.getRequestURI();
		if ( bestMatchingPattern.equals("") || bestMatchingPattern.equals("/") ) {
			request.getRequestDispatcher("/login").forward(request, response);
		}
		Object matchedHandler = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
		this.bestMatchingHandler = matchedHandler.toString();
		@SuppressWarnings("unchecked")
		ArrayList<String> notCheckingUrls = applicationContext.getBean("notCheckingUrls", ArrayList.class);
		if( bestMatchingHandler.indexOf("controller") > -1 && CommonUtil.isCheckingUrls(bestMatchingPattern, notCheckingUrls) ) {
			
			/* 관리도구 신규기능 적용 여부 체크 */
			// 사용자 권한체크
			if ( !authorityService.isAuthorityValid() ) {
				// ErrorHandler.java 에서 처리
				request.setAttribute(EAdminConstants.REQUESTED_URI.getValue(), request.getRequestURI());
				request.getRequestDispatcher("/error/invalidAuthority").forward(request, response);
				return false;
			}
			
			// 세션 유효성 체크
			if( !this.sessionCheck(request.getSession().getId()) ) {
				logger.info("session invalid :: " + bestMatchingPattern);
				request.getSession().invalidate();
				if( request.getAttribute(EAdminConstants.SESSION_INFO.getValue()) == null ) {
					request.setAttribute(EAdminConstants.SESSION_INFO.getValue(), EAdminMessages.SESSION_INVALID.getMessage());
					// ErrorHandler.java 에서 처리
					request.getRequestDispatcher("/error/invalidSession").forward(request, response);
					return false;
				}
			}
		}
		return true;
	}
	
	// sessionRegistry에 저장된 세션 리스트 반환 :: 없을 시 로그아웃시킨다.
	@SuppressWarnings("unused")
	private List<Map<String, String>> getAllSession() {
		List<Map<String, String>> sessionList = new ArrayList<>();
		List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
		// 각 Principal(사용자)에 대한 세션 정보를 가져옵니다.
		for (Object principal : allPrincipals) {
			if (principal instanceof UserDetails) {
				SecurityAdminResponseDto userDetails = (SecurityAdminResponseDto) principal;
				// 현재 사용자의 모든 세션 정보를 가져옵니다.
				List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
				for (SessionInformation s : sessions) {
					Map<String,String> map = new HashMap<>();
					logger.info(userDetails.getLoginId() + " : " + s.getSessionId());
					SessionInformation session = sessionRegistry.getSessionInformation(s.getSessionId());
					if (!session.isExpired() == true) {
						map.put(userDetails.getLoginId(),s.getSessionId());
						sessionList.add(map);
					}
				}
			}
		}
		return sessionList;
	}
	
	private Boolean sessionCheck(String chkSessionId) {
		if( chkSessionId == null ) return false;
		SessionInformation sessionInfomation = sessionRegistry.getSessionInformation(chkSessionId);
		if( sessionInfomation == null || sessionInfomation.isExpired() ) {
			return false;
		} else {
			return true;
		}
	}
	
	/* 컨트롤러로 들어오는 메소드 체크 */
//	private Boolean isControllerMethod(Object matchedHandler) {
//		Boolean rst = false;
//		Class<?> methodObj = ((HandlerMethod) matchedHandler).getMethod().getReturnType();
//		if ( methodObj.getTypeName().equalsIgnoreCase("java.lang.String") ||
//				methodObj.getTypeName().equalsIgnoreCase("org.springframework.http.ResponseEntity") ) {
//			rst = true;
//		}
//		return rst;
//	}
	
//	private void readAttributes(HttpServletRequest request) {
//			Enumeration<String> params = request.getAttributeNames();
//			int cnt = 0;
//			String queryString = "";
//			while (params.hasMoreElements()){
//			    String name = (String)params.nextElement();
//			    System.out.println("request attr name : " + name + ", val : " + request.getAttribute(name));
//			}
//	}
//	private void readAttributes(HttpServletResponse request) {
//		Collection<String> params = request.getHeaderNames();
//		int cnt = 0;
//		String queryString = "";
//		for(String name: params){
//			System.out.println("response header name : " + name + ", val : " + request.getHeader(name));
//		}
//	}
	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws IOException {
		
		Object matchedHandler = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
		this.bestMatchingHandler = matchedHandler.toString();
		if( bestMatchingHandler.indexOf("controller") > -1 ) {
			request.getSession().setAttribute(EAdminConstants.STATUS.getValue(), null);
			request.getSession().setAttribute(EAdminConstants.RESULT_MAP.getValue(), null);
		}
	}
}
