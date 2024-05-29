package com.yumikorea.db.service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.mvc.dto.PageDto;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.PwHashUtils;
import com.yumikorea.db.dto.DBRequestDto;
import com.yumikorea.db.repository.DBRepository;
import com.yumikorea.db.repository.DBRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DBService {

	private final DBRepository repository;
	private final DBRepositoryCustom repositoryCustom;

	/* 목록 조회 */
	public Map<String, Object> getList(DBRequestDto dto) {
		Map<String, Object> map = new HashMap<>();

		Long totCnt = repositoryCustom.findAllCnt(dto);
		map.put("list", repositoryCustom.findAll(dto));

		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totCnt.intValue());
		map.put(EAdminConstants.PAGE.getValue(), page);
		
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());

		return map;
	}

	/* 등록 */
	public Map<String, Object> register(DBRequestDto dto, String loginId) {
		
		Map<String, Object> map = new HashMap<>();
		Object res = null;
		
		dto.setModifyId(loginId);
		dto.setModifyDate(new Date());
		try {
			res  = repository.save(dto.toSaveEntity());
			
		} catch (SQLGrammarException e) {
			e.printStackTrace();
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), e.getMessage());
			return map;
		}
		
		System.out.println("@@@@@test@@@@@@@");
		System.out.println(res .toString());
		System.out.println("@@@@@test@@@@@@@");
		
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());

		return map;
	}


	/* 삭제 */
	public Map<String, Object> delete(String[] idArr, String loginId ) {
		Map<String, Object> map = new HashMap<>();
		
		int len = CommonUtil.getLength(idArr);
		for (int i = 0; i < len; i++) {
			repository.deleteById(null);
		}
		
		if( len == 0 ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NO_SEARCH_RESULT.getMessage());
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_SUCCESS.getMessage());
		}
		return map;
	}

	/* 상태 변경 */
	public Map<String, Object> updateSt(String[] idArr) {
		Map<String, Object> map = new HashMap<>();
		String initPw = "";
		// hmlee
		// #1. 비밀번호 초기화
		// #2. 관리자 상태 '초기'로 변경 & 초기 비밀번호 저장
		int len = CommonUtil.getLength(idArr);
		for (int i = 0; i < len; i++) {
			// random password 생성
				initPw = PwHashUtils.randomString(13);
			try {
				String newPw = PwHashUtils.getPwHash(initPw, idArr[i]);
//				repositoryCustom.updateDBUserStateInit(idArr[i], newPw);
			} catch (NoSuchAlgorithmException e) {
				map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
				map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_FAIL.getMessage());
				e.printStackTrace();
				return map;
			}
		}
		
		if( len == 0 ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NO_SEARCH_RESULT.getMessage());		
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());		
			map.put(EAdminConstants.PASSWORD.getValue(), initPw);
		}
		
		return map;
		
	}

	/* 내 정보 보기 */
	public Map<String, Object> getDetail(String announceId) {
		Map<String, Object> result = new HashMap<>();
//		DB announce = repository.findById(announceId).orElse(null);
//		DBResponseDto dto = new DBResponseDto(announce);
//		result.put(EAdminConstants.RESULT_MAP.getValue(), dto);
		result.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		return result;

	}

	/* 내 정보 수정 */
	public Map<String, Object> udpateMyInfo(DBRequestDto dto, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();

		if( true ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NO_SEARCH_RESULT.getMessage());		
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());		
		}
		
		return map;
	}

	// 로그인 - 비밀번호 변경
	public Map<String, Object> updateMyPw(DBRequestDto dto, String loginId, String sessionId) {
		Map<String, Object> map = new HashMap<>();

		if( true ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NO_SEARCH_RESULT.getMessage());		
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());		
		}
		
		
		return map;
	}

	public Map<String, Object> update(DBRequestDto dto) {
		Map<String, Object> map = new HashMap<>();

		if( true ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NO_SEARCH_RESULT.getMessage());		
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());		
		}
		
		
		return map;
	}

}
