package com.yumikorea.common.securityconfig;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.yumikorea.common.enums.EAdminConstants;

@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
public class SecurityConfig {
	
	@Autowired
	private CustomAuthFailureHandler customAuthFailureHandler;
	
	@Autowired
	private CustomLogInSuccessHandler loginSuccessHandler;
	
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private ArrayList<String> securityException;
	private String[] roleInitRrl = {"/common/initPw", "/common/updateInitPw"};
	
	private final Logger logger = LoggerFactory.getLogger( SecurityConfig.class );

	
	@Bean
	static BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@SuppressWarnings("unchecked")
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		logger.info("Spring Security Filter configration");
		this.securityException = applicationContext.getBean("securityException", ArrayList.class);
		
		logger.info("-------------- Spring Security Exception URLS --------------");
		for( String exceptionUrl: this.securityException ) {
			logger.info(exceptionUrl);
		}
		logger.info("-------------- Spring Security Exception URLS END --------------");
		http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
	                config.setAllowedOrigins(Collections.singletonList("http://127.0.0.1"));
	                config.setAllowedMethods(Collections.singletonList("*"));
	                config.setAllowCredentials(true);
	                config.setAllowedHeaders(Collections.singletonList("*"));
	                config.setMaxAge(3600L); //1시간
	                return config;
				}
        	})
        )
        .addFilterBefore(duplicatLoginFilter(), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement((sessionManagement) -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .sessionFixation().newSession()
                .maximumSessions(1)	// -1 은 무제한
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/error/invalidSession")	// 세션만료된 경우 이동할 페이지
                
        )
        
        .authorizeHttpRequests((authorizeRequests) ->
		authorizeRequests.antMatchers( this.securityException.toArray(new String[0])
				).permitAll()
        		.antMatchers( this.roleInitRrl ).hasAuthority(EAdminConstants.ROLE_INIT.getValue())
        		.antMatchers("/dashboard/**").hasAnyAuthority(EAdminConstants.ROLE_USER.getValue(), EAdminConstants.ROLE_ADMIN.getValue())
        		.antMatchers("/**").hasAuthority(EAdminConstants.ROLE_ADMIN.getValue())
        		.anyRequest().authenticated()
        );

		http   
            .formLogin((formLogin) -> {
            /* 권한이 필요한 요청은 해당 url로 리다이렉트 */
			    formLogin
			    .permitAll()
			    .loginPage("/login")
			    .loginProcessingUrl("/loginForm")
			    .usernameParameter("loginId")
			    .passwordParameter("password")
			    //.defaultSuccessUrl("/")	// loginSuccessHandler 에서 재정의 
			    .successHandler( loginSuccessHandler )
			    .failureHandler( customAuthFailureHandler );
            })
            .logout((logoutConfig) ->
            	logoutConfig
            	.logoutUrl("/logout")
            	.clearAuthentication(false)
            	.logoutSuccessUrl("/login")
            	.logoutSuccessHandler((request, response, authentication) -> {
            		// 로그아웃 세션은 CustomSessionExpiredStrategy.sessionDestroyed 에서 처리
            		response.sendRedirect("/login");
                })
            	.invalidateHttpSession(true) //세션 날리기
            );
            
            
         return http.build();
	}

    //  동시에 로그인한 세션들을 추적하고 관리한다.
    @Bean
    SessionRegistry sessionRegistry() {
		this.sessionRegistry = new SessionRegistryImpl(); 
	    return this.sessionRegistry;
	}
    
    protected DuplicateLoginFilter duplicatLoginFilter() throws Exception {
        return new DuplicateLoginFilter(sessionRegistry);
    }
    
}