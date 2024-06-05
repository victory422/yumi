package com.yumikorea.announce.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.announce.dto.AnnounceRequestDto;
import com.yumikorea.announce.entity.AnnounceEntity;
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
	public Map<String, Object> register(AnnounceRequestDto dto ) {
		Map<String, Object> map = new HashMap<>();
		
		dto.setReigistDate(new Date());
		dto.setModifyDate(new Date());
		dto.setDeleteYn(EAdminConstants.STR_N.getValue());
		
		repository.save(dto.toSaveEntity());
	
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());

		return map;
	}


	/* 삭제 */
	public Map<String, Object> delete(int announceId ) {
		Map<String, Object> map = new HashMap<>();
		
		long rst = repositoryCustom.deleteAnnounceById(announceId);
		if( rst > 0 ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_SUCCESS.getMessage());
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NO_SEARCH_RESULT.getMessage());
		}
		
		return map;
	}



	/* 공지사항 수정 */
	public Map<String, Object> update(AnnounceRequestDto dto) {
		Map<String, Object> map = new HashMap<>();
		
		dto.setModifyDate(new Date());
		
		long rst = repositoryCustom.update(dto);
		
		if( rst > 0 ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NO_SEARCH_RESULT.getMessage());
		}
		
		return map;
	}

	// 조회수 증가
	public Map<String, Object> addCount(int announceId) {
		Map<String, Object> map = new HashMap<>();
		repositoryCustom.addCount(announceId);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
		return null;
	}

}
