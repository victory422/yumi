package com.yumikorea.admin.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.admin.dto.AdminRequestDto;
import com.yumikorea.admin.dto.AdminResponseDto;
import com.yumikorea.admin.entity.Admin;
import com.yumikorea.admin.repository.AdminRepository;
import com.yumikorea.admin.repository.AdminRepositoryCustom;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.mvc.dto.PageDto;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.PwHashUtils;
import com.yumikorea.setting.entity.AuthorityUserEntity;
import com.yumikorea.setting.repository.AuthorityRepository;
import com.yumikorea.setting.repository.AuthorityUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

	private final AdminRepository repository;
	private final AdminRepositoryCustom repositoryCustom;
	private final AuthorityRepository authorityRepository;
	private final AuthorityUserRepository authorityUserRepository;

	/* 목록 조회 */
	public Map<String, Object> getList(AdminRequestDto dto) {
		Map<String, Object> map = new HashMap<>();

		Long totCnt = repositoryCustom.findAllCnt(dto);
		map.put(EAdminConstants.RESULT_MAP.getValue(), repositoryCustom.findAll(dto));
		map.put("authorityList", authorityRepository.findAll());
		map.put(EAdminConstants.PAGE.getValue(), new PageDto(dto.getPage(), dto.getRows(), totCnt.intValue()));
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());

		return map;
	}

	/* 등록 */
	public Map<String, String> register(AdminRequestDto dto, String loginId ) {
		Map<String, String> map = new HashMap<>();
		String initPw = "";
		String checkPassword = "";
		
		try {
			initPw = PwHashUtils.randomString(13);
			checkPassword = PwHashUtils.getPwHash(initPw, dto.getAdminId());
			dto.setPassword(checkPassword);
		} catch (NoSuchAlgorithmException e) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), e.getMessage());
			e.printStackTrace();
			return map;
		}
	
		try {
			repository.save(dto.toSaveEntity());
			AuthorityUserEntity authorityUserEntity = new AuthorityUserEntity();
			authorityUserEntity.setAdminId(dto.getAdminId());
			authorityUserEntity.setAuthorityId(dto.getAuthorityId());
			authorityUserEntity.setModifyDate(new Date());
			authorityUserEntity.setModifyId(loginId);
			authorityUserRepository.save(authorityUserEntity);
		} catch (Exception e) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), e.getMessage());
			e.printStackTrace();
			return map;
		}
		
		map.put("initPw", initPw);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());
		
		return map;
	}


	/* 삭제 */
	public Map<String, Object> delete(String[] idArr, String loginId ) {
		Map<String, Object> map = new HashMap<>();
		
		int len = CommonUtil.getLength(idArr);
		for (int i = 0; i < len; i++) {
			repositoryCustom.deleteAdminById(idArr[i], loginId);
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
				repositoryCustom.updateAdminUserStateInit(idArr[i], newPw);
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
	public Map<String, Object> getDetail(String adminId) {
		Map<String, Object> result = new HashMap<>();
		Admin admin = repository.findById(adminId).orElse(null);
		AdminResponseDto dto = new AdminResponseDto(admin);
		result.put(EAdminConstants.RESULT_MAP.getValue(), dto);
		result.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		return result;

	}

	/* 내 정보 수정 */
	public Map<String, String> udpateMyInfo(AdminRequestDto dto, HttpServletRequest request) throws Exception {
		Map<String, String> result = new HashMap<>();
		String sessionId = request.getSession().getId();

		Admin dbAdmin = repository.findById(dto.getAdminId()).get();
		String chkDbpw = CommonUtil.encodePassword(dbAdmin.getPassword(), sessionId);
		System.out.println("check pw @@@@ " + chkDbpw);
		// 올바른 비밀번호를 입력한 경우
		if (dto.getPassword().equals(chkDbpw)) {
			repositoryCustom.updateAdmin(dto.toUpdateEntity());
			result.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			String newUserName = dto.getName() != null ? dto.getName() : "";
			request.getSession().setAttribute(EAdminConstants.USER_NAME.getValue(), newUserName);

		// 새 비밀번호가 기존 비밀번호와 똑같은 경우
		} else if (dto.getPassword().equals(dto.getNewPw())) {
			result.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.PASSWORD_CHANGE_ERROR.getMessage());
			result.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());

		// 비밀번호 틀린 경우
		} else {
			result.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			result.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.ILLEGAL_PASSWORD.getMessage());
		}

		return result;
	}

	// 로그인 - 비밀번호 변경
	public Map<String, String> updateMyPw(AdminRequestDto dto, String loginId, String sessionId) {
		Map<String, String> result = new HashMap<>();

		Admin dbAdmin = repository.findById(loginId).get();

		String chkDbpw = CommonUtil.encodePassword(dbAdmin.getPassword(), sessionId);

		// 현재 비밀번호 == DB 비밀번호
		if (dto.getPassword().equals(chkDbpw)) {
			repositoryCustom.updatePassword(loginId, dto.getNewPw());
			result.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			result.put("redirectUrl", "/logout");

			// 현재 비밀번호 != DB 비밀번호
		} else {
			result.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			result.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.ILLEGAL_PASSWORD.getMessage());
		}
		
		return result;
	}

}
