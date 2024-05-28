package com.yumikorea.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@ConfigurationProperties
public class LoginController {
	
	/* 로그인페이지 이동 */
	@RequestMapping("/login")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("REQUIRES_AUTH", "1");
		return "common/index";
	}
}
