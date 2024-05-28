package com.yumikorea.common.securityconfig;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.yumikorea.admin.dto.SecurityAdminResponseDto;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;

public class DuplicateLoginFilter extends GenericFilterBean {

	private SessionRegistry sessionRegistry;
	
	public DuplicateLoginFilter(final SessionRegistry sessionRegistry ) {
		this.sessionRegistry = sessionRegistry;
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// post 방식에 로그인 요청이 아닌 경우 작업종료
		if (!new AntPathRequestMatcher("/loginForm", "POST").matches(request)) {
			chain.doFilter(req, res);
			return;
		}
		
		String loginId = (String) request.getParameter(EAdminConstants.LOGIN_ID.getValue());
		String password = request.getParameter(EAdminConstants.PASSWORD.getValue());
		String confirmLogout = EAdminConstants.FALSE.getValue();
		if( loginId == null ) loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		
		
		if (request.getParameter(EAdminConstants.CONFIRM_LOGOUT.getValue()) != null) {
			confirmLogout = request.getParameter(EAdminConstants.CONFIRM_LOGOUT.getValue());
		}
		
		if( !isLoggedInId(loginId) || Boolean.parseBoolean(confirmLogout) ) {
			chain.doFilter(req, res);
		}else {
			Map<String,String> rstMap = new HashMap<>();
			rstMap.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SESSION_DUPLICATION.getValue());
			rstMap.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.SESSION_AUTHENTICATION_ACCOUNT.getMessage());
	    	rstMap.put(EAdminConstants.LOGIN_ID.getValue(), request.getParameter(EAdminConstants.LOGIN_ID.getValue()));
	    	rstMap.put(EAdminConstants.PASSWORD.getValue(), password);
	    	if( password != null ) {
	    		request.setAttribute(EAdminConstants.LOGIN_FAIL_MAP.getValue(), rstMap);
	    	}
	        request.getRequestDispatcher("/login").forward(request,response);
		}
		return;
	}
	
	// sessionRegistry에 저장된 세션 리스트 반환 :: 없을 시 로그아웃시킨다.
	public Boolean isLoggedInId(String loginId) {
		Boolean rst = false;
		List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
		// 각 Principal(사용자)에 대한 세션 정보를 가져옵니다.
		for (Object principal : allPrincipals) {
			SecurityAdminResponseDto userDetails = (SecurityAdminResponseDto) principal;
			List<SessionInformation> sessionList = sessionRegistry.getAllSessions(principal, false);
			
			for(SessionInformation session : sessionList ) {
				if ( userDetails.getLoginId().equalsIgnoreCase(loginId) ) {
					if( !session.isExpired() ) {
						rst = true;
						break;
					}
				}
			}
		}
		return rst;
	}

	
}