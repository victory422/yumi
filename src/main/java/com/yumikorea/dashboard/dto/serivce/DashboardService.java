package com.yumikorea.dashboard.dto.serivce;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yumikorea.code.dto.request.CodeDetailRequestDto;
import com.yumikorea.code.dto.response.CodeDetailResponseDto;
import com.yumikorea.common.utils.SysTemInfoUtils;
import com.yumikorea.dashboard.repository.DashBoardRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class DashboardService {

	private final DashBoardRepositoryCustom repositoryCustom;

	public Map<String, Object> getCpu() throws InterruptedException {
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("sData", new SysTemInfoUtils().getCPUProcess());
		return retMap;
	}

	public Map<String, Object> getMem() {
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("sData", new SysTemInfoUtils().getMemory());
		return retMap;
	}

	public Map<String, Object> getDriver() {
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("sData", new SysTemInfoUtils().getDiskSpace());
		return retMap;
	}

	public Map<String, Object> getSvrCurTime() {
		Map<String, Object> retMap = new HashMap<>();

		Date today = new Date();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");

		retMap.put("date", simpleDateFormat.format(today));
		retMap.put("hh", String.format("%02d", today.getHours()));
		retMap.put("mm", String.format("%02d", today.getMinutes()));
		retMap.put("ss", String.format("%02d", today.getSeconds()));

		return retMap;
	}


	
	private List<CodeDetailResponseDto> getCodeDetail(CodeDetailRequestDto dto) {
		return repositoryCustom.getCodeDetail(dto);
	}
	
	private Map<String,Object> getCodeDetailToMap(List<CodeDetailResponseDto> list) {
		Map<String,Object> map = new HashMap<String,Object>();
		for ( int i = 0; i < list.size(); i++ ) {
			map.put(list.get(i).getCode(), list.get(i).getDescription());
		}
		return map;
	}

}
