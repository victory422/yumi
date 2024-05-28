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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import com.yumikorea.admin.dto.AdminResponseDto;
import com.yumikorea.admin.repository.AdminRepository;
import com.yumikorea.admin.repository.AdminRepositoryCustom;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;

@Component
@Transactional
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
	@Autowired
	private AdminRepositoryCustom adminRepositoryCustom;
	
	@Autowired
	private AdminRepository adminRepository;
	
	private final Logger logger = LoggerFactory.getLogger(CustomAuthFailureHandler.class);
	
	private final String DEFAULT_FAIL_URL = "/login";
	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "";
        Map<String,String> rstMap = new HashMap<>();
        rstMap.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
		if( exception instanceof InternalAuthenticationServiceException ) {	// 검색한 ID가 DB에 없을 때
			errorMessage = EAdminMessages.UNDEFINED_ACCOUNT.getMessage();
		}else if(exception instanceof BadCredentialsException ){	// 비밀번호 틀렸을 때
			errorMessage = this.setAccessCntAdd(request.getParameter(EAdminConstants.LOGIN_ID.getValue()));
        }else if(exception instanceof DisabledException){	// 미사용계정일 때. 여기에서는 미사용 (SecurityAdminResponseDto[UserDetails] 에 allways true 로 반환)
            errorMessage = EAdminMessages.DISABLED_ACCOUNT.getMessage();
        }else if(exception instanceof CredentialsExpiredException){	//  비밀번호 유효기간이 만료. 여기에서는 미사용 (SecurityAdminResponseDto[UserDetails] 에 allways true 로 반환)
            errorMessage = EAdminMessages.CREDENTIALS_ACCOUNT.getMessage();
        }else if(exception instanceof SessionAuthenticationException){	// 기 로그인된 사용자가 있음. SecurityConfig :: maximumSessions를 1로 설정
        	// 여기는 SecurityConfig에서 maximumSessions 를 1로 설정할 때만 들어온다.
    		errorMessage = EAdminMessages.SESSION_AUTHENTICATION_ACCOUNT.getMessage();
    		rstMap.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SESSION_DUPLICATION.getValue());
        }else if (exception instanceof LockedException) {	// 비밀번호 5회 fault 계정이 잠겼을 때
        	errorMessage = EAdminMessages.LOCKED_ACCOUNT.getMessage();
        }else{
        	exception.printStackTrace();
            errorMessage = EAdminMessages.EXCEPTION_ACCOUNT.getMessage();
        }
		logger.info("errorMessage : " + errorMessage);
    	rstMap.put(EAdminConstants.MESSAGE.getValue(), errorMessage);
    	rstMap.put(EAdminConstants.LOGIN_ID.getValue(), request.getParameter(EAdminConstants.LOGIN_ID.getValue()));
    	rstMap.put(EAdminConstants.PASSWORD.getValue(), request.getParameter(EAdminConstants.PASSWORD.getValue()));
    	request.setAttribute(EAdminConstants.LOGIN_FAIL_MAP.getValue(), rstMap);
    	request.getSession().setAttribute(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
        request.getRequestDispatcher(DEFAULT_FAIL_URL).forward(request,response);
    }
	
	/* access count 1 증가 */
    public String setAccessCntAdd(String loginId) {
    	if( loginId == null ) return "계정상태가 정상적이지 않습니다."; 
    	loginId = loginId.trim();
    	String rstMessage = "";
		
		AdminResponseDto responseDto = new AdminResponseDto(adminRepository.findById(loginId).get());
	
		// 5번째 틀린경우 (responseDto의 access count는 4임)
		if (responseDto.getAccessCnt() == 4) {
	
			// user state 잠금(03)으로 update
			adminRepositoryCustom.updateAdminUserStateLock(loginId);
	
			// 로그인 화면(index 페이지)으로 redirect
			rstMessage = EAdminMessages.LOCKING_ACCOUNT.getMessage();
		} else {
			// 로그인 화면(index 페이지)으로 redirect
			rstMessage = EAdminMessages.UNDEFINED_ACCOUNT.getMessage();
			if(!EAdminConstants.SUPER_ACCOUNT.getValue().equals(loginId)) {
				rstMessage += " (시도 : " + (responseDto.getAccessCnt() + 1) + "/5회)";
				adminRepositoryCustom.updateAccessCnt(loginId);
			}
		}
		
		return rstMessage;
	}
    

}
