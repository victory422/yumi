package com.yumikorea;


import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.yumikorea.admin.dto.AdminRequestDto;
import com.yumikorea.admin.repository.AdminRepositoryCustom;
import com.yumikorea.announce.dto.AnnounceRequestDto;
import com.yumikorea.announce.service.AnnounceService;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.utils.DateUtil;
import com.yumikorea.common.utils.PwHashUtils;
import com.yumikorea.db.dto.DBRequestDto;
import com.yumikorea.db.dto.MemoRequestDto;
import com.yumikorea.db.service.DBService;

@SpringBootTest
@ContextConfiguration(classes = AdminApplication.class )
class AdminApplicationTests {
	/*
	 * build.gradle  dependencies에 아래 코드 추가
	 * testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	 * */
	@Autowired
	AdminRepositoryCustom adminRepositoryCustom;
	
	@Autowired
	AnnounceService announceService;
	@Autowired
	DBService dbService;
	
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
	
	
//	@Test
	void pwTest() throws NoSuchAlgorithmException {
		String newPw = PwHashUtils.getPwHash("yumi!@34", "yumi-korea");
		System.out.println("@@@@@@@@@@@" + newPw);
		
	}
	
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		PwHashUtils pwHashUtils = new PwHashUtils();
		String newPw = pwHashUtils.getPwHash("yumi!@34", "yumi-korea");
		System.out.println("@@@@@@@@@@@" + newPw);
	}
	
	
//	@Test
	public void announceTest() {
//		AnnounceRequestDto dto = new AnnounceRequestDto();
//		dto.setAnnounceObject("object");
//		dto.setAnnounceContent("cont");
//		dto.setReigistDate(new Date());
//		service.register(dto, "yumi-korea");
		
		Map<String, Object> map = announceService.getList(new AnnounceRequestDto());
		List<AnnounceRequestDto> list = (List<AnnounceRequestDto>) map.get(EAdminConstants.RESULT_MAP.getValue());
		
		System.out.println("@@@@@@@@@@@@@@@@@@");
		for( AnnounceRequestDto dt : list ) {
			System.out.println(DateUtil.formatDate(dt.getReigistDate()));
		}
		System.out.println("@@@@@@@@@@@@@@@@@@");
		
		
	}
	
	@Test
	public void dbServiceTest() {
		
//		AnnounceRequestDto dto = new AnnounceRequestDto();
//		dto.setAnnounceObject("object");
//		dto.setAnnounceContent("cont");
//		dto.setReigistDate(new Date());
//		service.register(dto, "yumi-korea");
		MemoRequestDto dto = new MemoRequestDto();
		Map<String, Object> map = dbService.getListMemo(dto);
		List<MemoRequestDto> list = (List<MemoRequestDto>) map.get(EAdminConstants.RESULT_MAP.getValue());
		
		System.out.println("@@@@@@@@@@@@@@@@@@");
		for( MemoRequestDto dt : list ) {
			System.out.println(DateUtil.formatDate(dt.getRegistDate()));
		}
		System.out.println("@@@@@@@@@@@@@@@@@@");
		
		
	}

}
