package com.yumikorea.common.interceptor;


import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.yumikorea.audit.dto.request.WebAuditRequestDto;
import com.yumikorea.audit.service.WebAuditService;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class YumiAdminAOP {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private WebAuditService auditService;
	
	@Before("execution(* com.yumikorea..service..*(..))")
    public void doServiceTrace(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("[request service] {} args={}", joinPoint.getSignature(), args);
    }
	
	@SuppressWarnings("unchecked")
	@Around(	"execution(* com.yumikorea..controller..*(..)) " )
	public Object insertAuthAuditGetMethod(JoinPoint joinPoint) throws Throwable {
		String signatrue = joinPoint.getSignature().toString();
		ProceedingJoinPoint proceedingJoinPoint = (ProceedingJoinPoint) joinPoint;
		Object proceed = null;
		Object[] args = joinPoint.getArgs();
		
		if( args != null ) {
			proceed = proceedingJoinPoint.proceed(args);
		} else {
			proceed = proceedingJoinPoint.proceed();
		}
		
		ArrayList<String> notCheckingUrls = (ArrayList<String>) applicationContext.getBean("notCheckingUrls", ArrayList.class).clone();
		notCheckingUrls.remove("/common");
		notCheckingUrls.add("/common/getLoginInfo");
		notCheckingUrls.add("/common/getScreenAuthorityList");
		notCheckingUrls.add("/common/getMenuList");
		notCheckingUrls.add("/common/initPw");
		notCheckingUrls.add("/common/myInfo");
		notCheckingUrls.add("/common/getCodeDetail");
		notCheckingUrls.add("/common/service-by-server-info");
		notCheckingUrls.add("/common/check-duplicate");
		notCheckingUrls.add("/dashboard");
		notCheckingUrls.add("/keypair/download/");
		notCheckingUrls.add("/authority/getListAuthorityUrl");
		notCheckingUrls.add("/authority/getListAuthority");
		notCheckingUrls.add("/authority/getAuthorityMenulist");
		notCheckingUrls.add("/menu/getSettingMenulist");
		if ( CommonUtil.isCheckingUrls(request.getRequestURI(), notCheckingUrls) ) {
			log.info("[insertAuthAudit repository]  {} args={}", signatrue, args);
			Map<String, Object> auditMap = new HashMap<>();
			if( proceed != null ) {
				auditMap = this.getProceedResult(proceed);
			} else {	// void type의 메소드 처리 (지양해야 하는 타입)
				String status = EAdminConstants.SUCCESS.getValue();
				auditMap.put(EnumMasterCode.RESULT_SF.getMasterCodeValue(), getStatusCode(status));
			}
			
			String requestBody = "";
			if ( request.getContentType() != null && request.getContentType().contains("application/json") ) {
				ReadableRequestBodyWrapper wrapper = new ReadableRequestBodyWrapper((HttpServletRequest) request);
				requestBody = wrapper.getRequestBody();
				wrapper.setAttribute("requestBody", requestBody);
			} else {
				requestBody = this.getQueryString(request); 
			}
			log.debug("request args : {}", requestBody);
			
			auditService.insertAuthAudit(request, auditMap, requestBody);
		}
		
		return proceed;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getProceedResult(Object proceed) {
		Map<String, Object> auditMap = new HashMap<>();
		String status = EAdminConstants.FAIL.getValue();
		if ( proceed instanceof ResponseEntity ) {
			auditMap = getProceedResult(((ResponseEntity<?>) proceed).getBody());
			status = (String) auditMap.get(EAdminConstants.STATUS.getValue());
		} else if( proceed instanceof HashMap || proceed instanceof HashMap) {
			Map<String, Object> resultMap = (Map<String, Object>) proceed;
			log.info("[insertAuthAudit debug]  {}", resultMap.toString());
			if( (String) resultMap.get(EAdminConstants.STATUS.getValue()) != null ) {
				status = (String) resultMap.get(EAdminConstants.STATUS.getValue());
			}
			
			if( status.equals(EAdminConstants.FAIL.getValue()) ) {
				auditMap.put(EAdminConstants.ERROR_MESSAGE.getValue(), resultMap.get(EAdminConstants.MESSAGE.getValue()));
			}
		} else if(proceed instanceof Resource ) {
			// pem 다운로드
			status = EAdminConstants.SUCCESS.getValue();
		} else if(proceed instanceof String ) {
			// 화면 조회
			status = EAdminConstants.SUCCESS.getValue();
		} else if (proceed != null ) {
			log.info("[insertAuthAudit debug classname]  {}", proceed.getClass().getName());
		} else {
			log.info("[insertAuthAudit proceed is null]"); 
		}
		
		auditMap.put(EAdminConstants.STATUS.getValue(), status);
		auditMap.put(EnumMasterCode.RESULT_SF.getMasterCodeValue(), getStatusCode(status));
		return auditMap;
	}

	@After("execution(* com.yumikorea.common.securityconfig.CustomLogInSuccessHandler.onAuthenticationSuccess(..)) ")
	public void insertLoginSuccessAudit(JoinPoint joinPoint) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		log.info("[insertLoginSuccessAudit]  loginId={}", loginId);
		String resultSuccess = applicationContext.getBean(EAdminConstants.RESULT_CODE.getValue(), String[].class)[0];
		request.getSession().setAttribute(EAdminConstants.MENU_NAME.getValue(),"로그인");
		
		Map<String, Object> auditMap = new HashMap<String,Object>(); 
		auditMap.put(EnumMasterCode.RESULT_SF.getMasterCodeValue(), resultSuccess);
		
		auditService.insertAuthAudit(request, auditMap, "loginId="+ loginId==null ? "" : loginId);
	}
	
	@After("execution(* com.yumikorea.common.securityconfig.CustomAuthFailureHandler.onAuthenticationFailure(..)) ")
	public void insertLoginFailAudit(JoinPoint joinPoint) {
		@SuppressWarnings("unchecked")
		Map<String,String> failMap = (Map<String, String>) request.getAttribute(EAdminConstants.LOGIN_FAIL_MAP.getValue());
		if( failMap != null ) {
			String resultFail = applicationContext.getBean(EAdminConstants.RESULT_CODE.getValue(), String[].class)[1];
			String loginId = failMap.get(EAdminConstants.LOGIN_ID.getValue());
			WebAuditRequestDto requestDto = new WebAuditRequestDto();
			
			requestDto.setAdmin_id( loginId );
			requestDto.setReg_date( new Date() );
			requestDto.setUrl( "/loginForm" );
			requestDto.setAdmin_ip( request.getRemoteAddr() );
			requestDto.setServer_ip( request.getServerName() );
			requestDto.setResult( resultFail );
			requestDto.setMenu_code( "로그인" );
			log.info("loginFailMap: " + failMap);
			requestDto.setError_msg(failMap.get(EAdminConstants.MESSAGE.getValue()));
			log.info("[insertLoginFailAudit]  loginId={}", loginId);
			auditService.insertAuthAudit(requestDto, "loginId="+loginId);
		}
	}

	private String getStatusCode(String status) {
		String resultSuccess = applicationContext.getBean(EAdminConstants.RESULT_CODE.getValue(), String[].class)[0];
		String resultFail = applicationContext.getBean(EAdminConstants.RESULT_CODE.getValue(), String[].class)[1];
		if( status == null ) return resultFail; 
		
		if( !status.equals(EAdminConstants.SUCCESS.getValue()) ) {
			return resultFail;
		} else {
			return resultSuccess;
		}
	}
	
	private String getQueryString(HttpServletRequest request) {
		Enumeration<String> params = request.getParameterNames();
//		int cnt = 0;
		String queryString = "";
		while (params.hasMoreElements()){
		    String name = (String)params.nextElement();
		    queryString += String.format("%s : %s\n", name, request.getParameter(name));
//		    if( cnt == 0) {
//		    	queryString += "?" + name + "=" +request.getParameter(name);
//		    } else {
//		    	queryString += "&" + name + "=" +request.getParameter(name);
//		    }
//		    cnt++;
		}
		return queryString;
	}

}
