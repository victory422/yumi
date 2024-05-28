package com.yumikorea.common.interceptor;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.DateUtil;


@Component
public class InitSettingFilter implements Filter {

	@Autowired
	private ApplicationContext applicationContext;
	
	private ArrayList<String> notCheckingUrls;
	
	private String[] requiredDateParamArray; 
    
	@SuppressWarnings("unchecked")
	@Override
    public void init(FilterConfig filterConfig) {
		this.notCheckingUrls = applicationContext.getBean("notCheckingUrls", ArrayList.class);
		// 날짜 파라미터 필수화면 리스트 :: 검색기간을 한 달까지만 주기 위함
		this.requiredDateParamArray = applicationContext.getBean("getRequiredDateParamArray", String[].class);
	}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
    	
    	HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        
        
    	Boolean exception = false;
    	String path = ((HttpServletRequest) request).getServletPath();
    	for( String ex : notCheckingUrls ) {
    		if( path.indexOf(ex) == 0 ) {
    			exception = true;
    			break;
    		}
    	}

    	if( !exception ) {
    		// 쿠키에 타임아웃 시간 저장
    		response = setServletTimeOnCookie(request, response);
			request = new ReadableRequestBodyWrapper(request);
    		// list 출력하는 페이지 중 날짜가 포함된 화면은 한 달의 검색기간을 주입
    		if( requiredDateParamters(path) ) {
    			// 날짜 파라미터 체크 (날짜검색 컴포넌트가 있으면 한 달 치 검색만 가능하지만 파라미터를 변조 시 풀스캔 조회 가능하여 추가조치)
    			String[] dateParams = checkDateParamters(request.getParameter("srcFrom"), request.getParameter("srcTo"));

    			((ReadableRequestBodyWrapper) request).setParameter("srcFrom", dateParams[0]);
    			((ReadableRequestBodyWrapper) request).setParameter("srcTo", dateParams[1]);
			}
    	}
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
    
	
	private Boolean requiredDateParamters(String reqUrl) {
		for( String pathname : requiredDateParamArray ) {
			if( reqUrl.indexOf(pathname) > -1 ) return true;
		}
		return false;
	}
	
	
	
	private String[] checkDateParamters(String dateParam1, String dateParam2) throws IOException {
		String[] rst = new String[2];
		Date fromDt = null;
		Date toDt = null;
		dateParam1 = DateUtil.formatDateString(dateParam1);
		dateParam2 = DateUtil.formatDateString(dateParam2);
		if( !CommonUtil.isNull(dateParam1) && !CommonUtil.isNull(dateParam2) ) {
			// 날짜 형식이 안맞음 체크
			try {
				fromDt = DateUtil.parseDate(dateParam1, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
				toDt = DateUtil.parseDate(dateParam2, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
			} catch (ParseException e) {
				e.printStackTrace();
				return checkDateParamters(null, null);
			}

			LocalDateTime toDateTime = DateUtil.dateToLocalDateTime(toDt);
			LocalDateTime tmpDateTime = toDateTime.minus(1, ChronoUnit.MONTHS); // 조회 기준일 : 한 달
			
			rst[1] = DateUtil.formatDate(toDt, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
			// 한 달 이상을 조회하려고 할 때 또는 시작일자가 더 클 때
			if ( DateUtil.localDateTimeToDate(tmpDateTime).getTime() > fromDt.getTime() || 
					fromDt.getTime() > toDt.getTime() ) {
				rst[0] = DateUtil.formatDate(DateUtil.localDateTimeToDate(tmpDateTime), EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
			} else {
				rst[0] = DateUtil.formatDate(fromDt, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
			}
		// 두 값 중 하나만 빈 값 일 때
		} else if( CommonUtil.isNull(dateParam1) && !CommonUtil.isNull(dateParam2) ) {
			try {
				toDt = DateUtil.parseDate(dateParam2, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
				rst[1] = DateUtil.formatDate(toDt, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
				rst[0] = DateUtil.formatDate(DateUtil.addMonth(toDt, -1), EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
			} catch (ParseException e) {
				e.printStackTrace();
				return checkDateParamters(null, null);
			}
		} else if( !CommonUtil.isNull(dateParam1) && CommonUtil.isNull(dateParam2) ) {
			try {
				fromDt = DateUtil.parseDate(dateParam1, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
				rst[0] = DateUtil.formatDate(fromDt, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
				rst[1] = DateUtil.formatDate(DateUtil.addMonth(fromDt, 1), EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
			} catch (ParseException e) {
				e.printStackTrace();
				return checkDateParamters(null, null);
			}
		// date 파라미터가 없을 때 한 달 전 ~ 오늘 검색조건 추가
		} else {
			toDt = new Date(); 
			rst[0] = DateUtil.formatDate(DateUtil.addMonth(toDt, -1), EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
			rst[1] = DateUtil.formatDate(toDt, EAdminConstants.SIMPLE_DATE_FORMAT.getValue());
		}
		
		return rst;
	}
	
	
    private HttpServletResponse setServletTimeOnCookie(HttpServletRequest request, HttpServletResponse response) {
    	long serverTime = System.currentTimeMillis();
        long sessionExpiryTime = serverTime + request.getSession().getMaxInactiveInterval() * 1000;
        Cookie cookie = new Cookie("latestTouch", "" + serverTime);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("sessionExpiry", "" + sessionExpiryTime);
        cookie.setPath("/");
        response.addCookie(cookie);
    	return response;
    }
}