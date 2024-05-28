package com.yumikorea;


import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.yumikorea.AdminApplication;
import com.yumikorea.admin.dto.AdminRequestDto;
import com.yumikorea.admin.repository.AdminRepositoryCustom;
import com.yumikorea.common.utils.PwHashUtils;

@SpringBootTest
@ContextConfiguration(classes = AdminApplication.class )
class AdminApplicationTests {
	/*
	 * build.gradle  dependencies에 아래 코드 추가
	 * testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	 * */
	@Autowired
	AdminRepositoryCustom adminRepositoryCustom;
	
	
	  @BeforeAll
	    static void setup() {
	        System.out.println("@BeforeAll - executes once before all test methods in this class");
	    }
	  
//	@Test
	void contextLoads() {
		System.out.println("test test @@@@@@@@@@@@@@@@@@@@@@@");
		AdminRequestDto dto = new AdminRequestDto();
		dto.setAdminId("yumi-korea");
		adminRepositoryCustom.findAdminDetailList(dto);
	}
	
	
	@Test
	void pwTest() throws NoSuchAlgorithmException {
		String newPw = PwHashUtils.getPwHash("yumi!@34", "yumi-korea");
		System.out.println("@@@@@@@@@@@" + newPw);
		
	}
	
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		PwHashUtils pwHashUtils = new PwHashUtils();
		String newPw = pwHashUtils.getPwHash("yumi!@34", "yumi-korea");
		System.out.println("@@@@@@@@@@@" + newPw);
	}

}
