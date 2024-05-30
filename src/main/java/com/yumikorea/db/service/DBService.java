package com.yumikorea.db.service;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
		List<DBRequestDto> resultList = new ArrayList<>();
		Long totCnt = repositoryCustom.findAllCnt(dto);
		
		
		for(DBRequestDto result : repositoryCustom.findAll(dto) ) {
			result.setModifyDateString(new SimpleDateFormat("yy/MM/dd").format(result.getModifyDate()));
			resultList.add(result);
		}

		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totCnt.intValue());
		
		map.put(EAdminConstants.PAGE.getValue(), page);
		map.put(EAdminConstants.RESULT_MAP.getValue(), resultList);
		
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
	public Map<String, Object> delete(List<DBRequestDto> dtoList, String loginId ) {
		Map<String, Object> map = new HashMap<>();
		int len = CommonUtil.getLength(dtoList);
		for (int i = 0; i < len; i++) {
			repositoryCustom.deleteForUpdate(dtoList.get(i).getDbSeq());
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
