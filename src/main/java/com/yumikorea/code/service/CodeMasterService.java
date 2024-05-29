package com.yumikorea.code.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.yumikorea.code.dto.request.CodeMasterRequestDto;
import com.yumikorea.code.dto.response.CodeMasterResponseDto;
import com.yumikorea.code.entity.CodeMaster;
import com.yumikorea.code.repository.CodeDetailRepositoryCustom;
import com.yumikorea.code.repository.CodeMasterRepository;
import com.yumikorea.code.repository.CodeMasterRepositoryCustom;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.mvc.dto.PageDto;
import com.yumikorea.common.utils.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeMasterService {

	private final CodeMasterRepository repository;
	private final CodeMasterRepositoryCustom repositoryCustom;
	private final CodeDetailRepositoryCustom detailRepositoryCustom;
	
	/* 목록 조회 */
	public Map<String, Object> getList(CodeMasterRequestDto requestDto) {
		Map<String, Object> retMap = new HashMap<>();

		Long totCnt = repositoryCustom.findAllCnt(requestDto);
		PageDto page = new PageDto(requestDto.getPage(), requestDto.getRows(), totCnt.intValue());

		List<CodeMasterResponseDto> list = null;
		if (totCnt > 0) {
			list = repositoryCustom.findAll(requestDto);
		}

		retMap.put(EAdminConstants.PAGE.getValue(), page);
		retMap.put("list", list);

		return retMap;
	}

	public Map<String, String> save(CodeMasterRequestDto requestDto) {
		Map<String, String> map = new HashMap<>();
		if (repository.save(requestDto.toSaveEntity()) != null) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_SUCCESS.getMessage());
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.REGISTER_FAIL.getMessage());
		}
		return map;
	}

	public Map<String, String> update(CodeMasterRequestDto requestDto) {
		Map<String, String> map = new HashMap<>();
		CodeMaster codeMaster = repository.findById(requestDto.getMastercode()).get();
		requestDto.setRegDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(codeMaster.getReg_date()));
		requestDto.setRegDateBy(codeMaster.getReg_date_by());
		
		if(repository.save(requestDto.toUpdateEntity()) != null) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_FAIL.getMessage());
		}
		return map;
	}

	@Transactional
	public Map<String, String> delete(String[] mastercodes) {
		Map<String, String> map = new HashMap<>();
		try {
			for (int i = 0; i < CommonUtil.getLength(mastercodes); i++) {
				// 자식레코드가 있으면 삭제할 수 없다.
				if( detailRepositoryCustom.findAll(mastercodes[i]).size() > 0 ) {
					map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
					map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.HAS_CHILD_CODE.getMessage());
					return map;
				}
				
				repository.deleteById(mastercodes[i]);
			}
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_SUCCESS.getMessage());
		} catch (IllegalArgumentException e) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_FAIL.getMessage());
		}
		return map;
	}

	public Map<String, Object> getCodeNames() {
		Map<String, Object> map = new HashMap<>();
		List<CodeMaster> mCodeList = repository.findAll();
		List<String> codeNames = new ArrayList<>();
		for( CodeMaster codeMaster : mCodeList ) {
			codeNames.add(codeMaster.getMaster_code());
		}
		map.put("codeNames", codeNames);
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		return map;
	}
}
