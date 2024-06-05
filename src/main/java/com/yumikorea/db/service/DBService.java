package com.yumikorea.db.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.mvc.dto.PageDto;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.db.dto.DBRequestDto;
import com.yumikorea.db.dto.MemoRequestDto;
import com.yumikorea.db.repository.DBRepository;
import com.yumikorea.db.repository.DBRepositoryCustom;
import com.yumikorea.db.repository.MemoRepository;
import com.yumikorea.db.repository.MemoRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DBService {

	private final DBRepository dbRepository;
	private final DBRepositoryCustom dbRepositoryCustom;
	private final MemoRepository memoRepository;
	private final MemoRepositoryCustom memoRepositoryCustom;

	/* 목록 조회 */
	public Map<String, Object> getList(DBRequestDto dto) {
		Map<String, Object> map = new HashMap<>();
//		List<DBRequestDto> resultList = new ArrayList<>();
		Long totCnt = dbRepositoryCustom.findAllCnt(dto);
		List<DBRequestDto> resultList = dbRepositoryCustom.findAll(dto);
		
		
//		for(DBRequestDto result : repositoryCustom.findAll(dto) ) {
//			result.setModifyDate(DateUtil.formatDate(result.getModifyDate()));
//		}

		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totCnt.intValue());
		
		map.put(EAdminConstants.PAGE.getValue(), page);
		map.put(EAdminConstants.RESULT_MAP.getValue(), resultList);
		
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());

		return map;
	}

	/* 등록 */
	public Map<String, Object> register(DBRequestDto dto ) {
		Map<String, Object> map = new HashMap<>();
		
		if( !this.checkDuplicate(dto.getDbName(), "dbName") ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NAME_DUPLICATE.getMessage());
			return map;
		}
		
		if( !this.checkDuplicate(dto.getDbTel(), "dbTel") ) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.TEL_DUPLICATE.getMessage());
			return map;
		}
		
		dto.setModifyDate(new Date());
		try {
			dbRepository.save(dto.toSaveEntity());
		} catch (SQLGrammarException e) {
			e.printStackTrace();
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), e.getMessage());
			return map;
		}
		
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());

		return map;
	}



	public Map<String, Object> update(DBRequestDto dto) {
		Map<String, Object> map = new HashMap<>();
		
		dto.setModifyDate(new Date());
		dbRepository.save(dto.toSaveEntity());
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());		
		
		return map;
	}
	

	public Map<String, Object> registMemo(MemoRequestDto dto) {
		Map<String, Object> map = new HashMap<>();
		dto.setRegistDate(new Date());
		try {
			memoRepository.save(dto.toSaveEntity());
		} catch (SQLGrammarException e) {
			e.printStackTrace();
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), e.getMessage());
			return map;
		}
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());

		return map;
	}

	

	/* 메모 목록 조회 */
	public Map<String, Object> getListMemo(MemoRequestDto dto) {
		Map<String, Object> map = new HashMap<>();
		List<MemoRequestDto> resultList = memoRepositoryCustom.findAll(dto);
		map.put(EAdminConstants.RESULT_MAP.getValue(), resultList);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());

		return map;
	}

	

	/* 삭제 */
	public Map<String, Object> delete(List<DBRequestDto> dtoList, String loginId ) {
		Map<String, Object> map = new HashMap<>();
		int len = CommonUtil.getLength(dtoList);
		for (int i = 0; i < len; i++) {
			dbRepositoryCustom.deleteForUpdate(dtoList.get(i).getDbSeq());
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



	public Boolean checkDuplicate(String srcStr, String bifurcation) {
		Boolean rst = false;
		long res = dbRepositoryCustom.checkDuplicate(srcStr, bifurcation);
		
		if( res > 0 ) {
			rst = false;
		} else {
			rst = true;
		}
		
		return rst;
	}
}
