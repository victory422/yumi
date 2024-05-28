package com.yumikorea.common.securityconfig;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import com.yumikorea.admin.dto.SecurityAdminResponseDto;
import com.yumikorea.audit.dto.request.WebAuditRequestDto;
import com.yumikorea.audit.service.WebAuditService;
import com.yumikorea.common.enums.EAdminConstants;

@Component
public class CustomSessionExpiredStrategy implements HttpSessionListener {
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private WebAuditService auditService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private final Logger logger = LoggerFactory.getLogger(CustomSessionExpiredStrategy.class);
	
    public void sessionCreated(HttpSessionEvent se) {
//    	System.out.println("sessionCreated " + se.getSession().getId());
    }

    public void sessionDestroyed(HttpSessionEvent se) {
    	this.expireSession(se.getSession());
    }
    
  //CustomSessionExpiredStrategy.sessionDestroyed 에서 호출 :: 세션만료 시 정보 삭제
  	private void expireSession(HttpSession session) {
  		SessionInformation sessionInfo = sessionRegistry.getSessionInformation(session.getId());
  		if ( sessionInfo != null && sessionInfo.isExpired() == false ) {
			String loginId = (String) session.getAttribute(EAdminConstants.LOGIN_ID.getValue());
			SecurityAdminResponseDto adminDto = (SecurityAdminResponseDto) sessionInfo.getPrincipal();
			WebAuditRequestDto requestDto = new WebAuditRequestDto();
			logger.info("[ logout id = {} ]", loginId);
			String resultSuccess = applicationContext.getBean(EAdminConstants.RESULT_CODE.getValue(), String[].class)[0];
			requestDto.setAdmin_id( loginId );
			requestDto.setReg_date( new Date() );
			requestDto.setUrl( "/logout" );
			requestDto.setAdmin_ip( adminDto.getRemoteAddr() );
			requestDto.setServer_ip( adminDto.getServerName() );
			requestDto.setResult( resultSuccess );
			requestDto.setMenu_code( "로그아웃" );
			auditService.insertAuthAudit(requestDto, null);
  			logger.info("[expired session] "+ loginId + " : " + session.getId());
  			sessionInfo.expireNow();
  			sessionRegistry.removeSessionInformation(session.getId());
  		}
  	}
    
}