package com.yumikorea.announce.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.announce.dto.AnnounceRequestDto;
import com.yumikorea.announce.repository.AnnounceRepository;
import com.yumikorea.announce.repository.AnnounceRepositoryCustom;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.mvc.dto.PageDto;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.PwHashUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnounceService {

	private final AnnounceRepository repository;
	private final AnnounceRepositoryCustom repositoryCustom;

	/* 목록 조회 */
	public Map<String, Object> getList(AnnounceRequestDto dto) {
		Map<String, Object> map = new HashMap<>();

		Long totCnt = repositoryCustom.findAllCnt(dto);
		map.put(EAdminConstants.RESULT_MAP.getValue(), repositoryCustom.findAll(dto));

		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totCnt.intValue());
		map.put(EAdminConstants.PAGE.getValue(), page);

		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());
		
		return map;
	}

	/* 등록 */
	public Map<String, Object> register(AnnounceRequestDto dto, String loginId) {
		Map<String, Object> map = new HashMap<>();
		dto.setAdminId(loginId);
		repository.save(dto.toSaveEntity());
	
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());

		return map;
	}


	/* 삭제 */
	public Map<String, Object> delete(String[] idArr, String loginId ) {
		Map<String, Object> map = new HashMap<>();
		
		int len = CommonUtil.getLength(idArr);
		for (int i = 0; i < len; i++) {
//			repositoryCustom.deleteAnnounceById(idArr[i], loginId);
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
//				repositoryCustom.updateAnnounceUserStateInit(idArr[i], newPw);
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
//		Announce announce = repository.findById(announceId).orElse(null);
//		AnnounceResponseDto dto = new AnnounceResponseDto(announce);
//		result.put(EAdminConstants.RESULT_MAP.getValue(), dto);
		result.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		return result;

	}

	/* 내 정보 수정 */
	public Map<String, Object> udpateMyInfo(AnnounceRequestDto dto, HttpServletRequest request) throws Exception {
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
	public Map<String, Object> updateMyPw(AnnounceRequestDto dto, String loginId, String sessionId) {
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

	public Map<String, Object> update(AnnounceRequestDto dto) {
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
