package com.yumikorea.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.code.dto.request.CodeDetailRequestDto;
import com.yumikorea.code.dto.response.CodeDetailResponseDto;
import com.yumikorea.code.entity.CodeDetailPK;
import com.yumikorea.code.repository.CodeDetailRepository;
import com.yumikorea.code.repository.CodeDetailRepositoryCustom;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.utils.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeDetailService {

	private final CodeDetailRepository repository;
	private final CodeDetailRepositoryCustom repositoryCustom;

	// 목록 조회
	public List<CodeDetailResponseDto> getList(String masterCode) {
		return repositoryCustom.findAll(masterCode);
	}
	
	// 목록 조회
	public List<CodeDetailResponseDto> getList(CodeDetailRequestDto requestDto) {
		return repositoryCustom.findAll(requestDto);
	}
	
	/* 공통코드와 연계한 메뉴리스트 조회 */
	public String[] getListWithCodeDetail( String masterCode ) {
    	CodeDetailRequestDto codeDto = new CodeDetailRequestDto();
    	codeDto.setSrcMasterCode(masterCode);
    	codeDto.setSrcEnable(EAdminConstants.STR_Y.getValue());
    	List<String> rstList = repositoryCustom.getListWithCodeDetail(codeDto);
    	
    	String[] rst = new String[rstList.size()];
    	for( int i = 0 ; i < rstList.size() ; i ++ ) {
    		rst[i] = rstList.get(i);
    	}
		
		return rst;
	}
    
	// 신규 등록
	public Map<String, String> save(CodeDetailRequestDto requestDto) {
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

	// 수정
	public Map<String, String> update(CodeDetailRequestDto requestDto) {
		Map<String, String> map = new HashMap<>();
		long res = repositoryCustom.update(requestDto);
		if( res == 1) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_SUCCESS.getMessage());
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_FAIL.getMessage());
		}
		return map;
	}

	// 상태 수정
	public Map<String, String> updateState(CodeDetailRequestDto requestDto) {
		Map<String, String> map = new HashMap<>();
		try {
			repositoryCustom.updateStateTo(requestDto);
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_STATE_SUCCESS.getMessage());
		} catch (SQLGrammarException e) {	// Sparrow 취약점 조치
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.UPDATE_STATE_FAIL.getMessage());
		} 
		return map;
	}

	// 삭제
	public Map<String, String> delete(String[] ids) {
		Map<String, String> map = new HashMap<>();
		int idCnt = CommonUtil.getLength(ids);
		int failCnt = 0;
		for(int i=0; i<idCnt; i++) {
			try {
				String[] id = ids[i].split("-");
				CodeDetailPK pk = new CodeDetailPK(id[0], id[1]);
				repository.deleteById(pk);
			} catch(IllegalArgumentException  e) {
				failCnt++;
			}
		}
		
		if(failCnt > 0) {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), "전체 " + idCnt + "건 중 " + failCnt + "건 삭제 실패하였습니다.");
		} else {
			map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
			map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.DELETE_SUCCESS.getMessage());
		}
		
		return map;
	}
	
}