package com.yumikorea.common.interceptor;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yumikorea.code.dto.response.CodeDetailResponseDto;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.code.service.CodeDetailService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private CodeDetailService codeDetailService;
	
	@Value("${spring.datasource.url}")
	private String datasourceUrl;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;

	
	
	/* filter, interceptor 예외처리 */
	private ArrayList<String> excludePattern;
	
	/* date parameter 체크 url */
	private String[] requiredDateParamArray;
	
	/* spring security 설정 호출 */
	private ArrayList<String> securityException;
	
	/* 공통코드 상태 코드 */
	private String resultSuccess;
	private String resultFail;
	
	@PostConstruct
	public void init() {
		setDefaultValues(0);
		setDefaultValues(1);
		setDefaultValues(2);
		
		this.setResultCode();
	}
	
	
	
	private void setDefaultValues(int key) {
		switch (key) {
			case 0:
				this.excludePattern = new ArrayList<>();
				String[] temp1 = new String[]{"/login","/error","/common","/js/","/css/","/fonts/","/images/"};
				for(String t : temp1 ) this.excludePattern.add(t); 
				break;
			case 1:
				String[] temp2 = new String[]{""};
				this.requiredDateParamArray = temp2;
				break;
			case 2:
				this.securityException = new ArrayList<>();
				String[] temp3 = new String[]{"/error/**","/login","/common/**","/js/**","/css/**","/fonts/**","/images/**"};
				for(String t : temp3 ) this.securityException.add(t);
				break;
			default:
				break;
		}
	}
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	ArrayList<String> patterns = this.excludePattern;
    	registry.addInterceptor(WebInterceptor())
    	.excludePathPatterns(patterns);
    }

    @Bean
    WebInterceptor WebInterceptor() {
        return new WebInterceptor();
    }

    /* ..*Scheduler.java 에서 호출 :: properties 설정파일 값을 읽어오기 위함 */
    @Bean(name = "schedulerProperties")
    PropertiesFactoryBean schedulerProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		ClassPathResource classPathResource = new ClassPathResource("conf/scheduler.properties");
		propertiesFactoryBean.setLocation(classPathResource);
		return propertiesFactoryBean;
	}
    

    /* InitSettingFilter.java 에서 호출 :: 날짜로 검색 파라미터가 있는 목록 */
    @Bean(name = "getRequiredDateParamArray")
    String[] getRequiredDateParamArray() {
    	// Sparrow 조치 : private으로 선언된 컬렉션(또는 배열)을 직접 리턴하는 대신 그 복사본을 만들어 리턴합니다.
    	String[] copiedRequiredDateParamArray = this.requiredDateParamArray;
		return copiedRequiredDateParamArray;
	}

    /* WebInterceptor.java 에서 호출 :: 권한, 세션체크 하지 않는 목록 */
    @Bean(name = "notCheckingUrls")
    ArrayList<String> getNotCheckingUrls() {
    	// Sparrow 조치 : private으로 선언된 컬렉션(또는 배열)을 직접 리턴하는 대신 그 복사본을 만들어 리턴합니다.
    	ArrayList<String> copiedExcludePattern = this.excludePattern;
		return copiedExcludePattern;
	}
    
    /* SecurityConfig.java 에서 호출 :: 권한, 세션체크 하지 않는 목록 */
    @Bean(name = "securityException")
    ArrayList<String> getSecurityException() {
    	// Sparrow 조치 : private으로 선언된 컬렉션(또는 배열)을 직접 리턴하는 대신 그 복사본을 만들어 리턴합니다.
    	ArrayList<String> copiedSecurityException = this.securityException;
		return copiedSecurityException;
	}
    
    
    @Bean(name = "resultCode")
    String[] getResultCode() {
    	// Sparrow 조치 : private으로 선언된 컬렉션(또는 배열)을 직접 리턴하는 대신 그 복사본을 만들어 리턴합니다.
    	String[] resultCode = {this.resultSuccess, this.resultFail};
    	return resultCode;
    }
    
    
	
	/* list to array */
	@SuppressWarnings("unused")
	private ArrayList<String> listToArray(List<CodeDetailResponseDto> list) {
		ArrayList<String> rst = new ArrayList<>();
    	for( int i = 0 ; i < list.size() ; i ++ ) {
    		rst.add(list.get(i).getValue01());
    	}
    	return rst;
	}
	
	private void setResultCode() {
		// 결과값 공통코드
		List<CodeDetailResponseDto> resultSFList =  codeDetailService.getList(EnumMasterCode.RESULT_SF.getMasterCodeValue());
		
		for( int i = 0 ; i < resultSFList.size(); i++ ) {
			CodeDetailResponseDto dto = resultSFList.get(i);
			if( dto.getDescription().contains("성공") ) {
				resultSuccess = dto.getCode();
			} else if( dto.getDescription().contains("실패") ) {
				resultFail = dto.getCode();
			}
		}
	}
	
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setDefaultCharset(Charset.forName("UTF-8"));
		converters.add(converter);
	}

}
