package com.yumikorea.common.securityconfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import com.yumikorea.admin.dto.SecurityAdminResponseDto;
import com.yumikorea.admin.repository.AdminRepository;
import com.yumikorea.admin.repository.AdminRepositoryCustom;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;

@Component
@Transactional
public class CustomLogInSuccessHandler implements AuthenticationSuccessHandler {
	private String loginidname;
	private String defaultUrl = "/dashboard";
	private String loginPage = "/login";
	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy rediretStratgy = new DefaultRedirectStrategy();

	@Value("${server.servlet.session.timeout}")
	private int SESSION_TIMEOUT_IN_SECOND;

	@Autowired
	private AdminRepositoryCustom adminRepositoryCustom;
	@Autowired
	private AdminRepository adminRepository;
	private final Logger logger = LoggerFactory.getLogger(CustomLogInSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// session time out 설정
		request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECOND);
		
		// 시큐리티 세션에 저장
		((SecurityAdminResponseDto) authentication.getPrincipal()).setRemoteAddr(request.getRemoteAddr());
		((SecurityAdminResponseDto) authentication.getPrincipal()).setServerName(request.getServerName());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// 로그인 성공 시 access count 초기화
		this.initAccessCntAndCreateToken(request);

		SecurityAdminResponseDto dto = (SecurityAdminResponseDto) authentication.getPrincipal();
		
		// 최초로그인 flag
		if (this.isFirstLoginAccount(dto)) {
			logger.info("최초로그인으로 비밀번호 설정 초기화 필요계정");
			Map<String, String> rstMap = new HashMap<>();
			rstMap.put(EAdminConstants.STATUS.getValue(), EAdminConstants.ROLE_INIT.getValue());
			rstMap.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.INIT_LOGIN.getMessage());
			request.setAttribute(EAdminConstants.LOGIN_FAIL_MAP.getValue(), rstMap);
//			request.sendRedirect(request, response, "/login?init");
			request.getRequestDispatcher(loginPage).forward(request, response);
		} else if (requestCache.getRequest(request, response) != null) {
			rediretStratgy.sendRedirect(request, response, defaultUrl);
		} else {
			response.sendRedirect(defaultUrl);
			//request.getRequestDispatcher(defaultUrl).forward(request, response);
		}
		request.getSession().setAttribute(EAdminConstants.LOGIN_ID.getValue(), dto.getLoginId());	// 스패로우취약점 도출:: VO로 처리
		request.getSession().setAttribute(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
	}

	public String getLoginidname() {
		return loginidname;
	}

	public void setLoginidname(String loginidname) {
		this.loginidname = loginidname;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	@Transactional
	public void initAccessCntAndCreateToken(HttpServletRequest request) {
		String loginId = request.getParameter(EAdminConstants.LOGIN_ID.getValue());
		// 접근횟수 0으로 초기화
		adminRepositoryCustom.initAccessCnt(loginId);
		// 토큰 세션에 저장
		SecurityAdminResponseDto adminDto = new SecurityAdminResponseDto(adminRepository.findById(loginId).get());
		request.getSession().setAttribute(EAdminConstants.USER_NAME.getValue(), adminDto.getName());
	}

	// 최초로그인 사용자 판별 ROLE_INIT 이 있는지에 따라 체크
	public Boolean isFirstLoginAccount(UserDetails user) {
		Boolean firstLogin = false;
		Object[] roles = user.getAuthorities().stream().toArray();
		
		if( roles == null ) return firstLogin;
		
		for (int i = 0; i < roles.length; i++) {
			String role = roles[i].toString();
			if (role.equalsIgnoreCase(EAdminConstants.ROLE_INIT.getValue())) {
				firstLogin = true;
				break;
			}
		}
		return firstLogin;
	}
	

}
