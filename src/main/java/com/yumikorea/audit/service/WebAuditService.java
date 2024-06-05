package com.yumikorea.audit.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.audit.dto.request.WebAuditRequestDto;
import com.yumikorea.audit.dto.response.WebAuditResponseDto;
import com.yumikorea.audit.repository.WebAuditRepository;
import com.yumikorea.audit.repository.WebAuditRepositoryCustom;
import com.yumikorea.code.enums.EnumMasterCode;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.mvc.dto.PageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WebAuditService {

	private final WebAuditRepositoryCustom repositoryCustom;
	private final WebAuditRepository repository;

	/* 목록 조회 */
	public Map<String, Object> getList(WebAuditRequestDto dto) {
		Map<String, Object> retMap = new HashMap<>();
		
		Long totCnt = repositoryCustom.findAllCnt(dto);
		List<WebAuditResponseDto> list = repositoryCustom.findAll(dto);
		
		PageDto page = new PageDto(dto.getPage(), dto.getRows(), totCnt.intValue());
		
		retMap.put(EAdminConstants.RESULT_MAP.getValue(), list);
		retMap.put(EAdminConstants.PAGE.getValue(), page);
		retMap.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
		
		return retMap;
	}
	
	/* quartz 스케쥴러 */
	public long deleteFromScheduler( WebAuditRequestDto dto ) {
		try {
			repositoryCustom.deleteWebAudit(dto);
			return repositoryCustom.deleteWebAuditCount(dto);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void insertAuthAudit( HttpServletRequest request, Map<String, Object> auditMap, String args ) {
		String loginId = (String) request.getSession().getAttribute(EAdminConstants.LOGIN_ID.getValue());
		String menuName = (String) request.getSession().getAttribute(EAdminConstants.MENU_NAME.getValue());
		String status = (String) auditMap.get(EnumMasterCode.RESULT_SF.getMasterCodeValue());
		WebAuditRequestDto requestDto = new WebAuditRequestDto();
		requestDto.setAdmin_id( loginId );
		requestDto.setReg_date( new Date() );
		requestDto.setUrl( request.getRequestURI() );
		requestDto.setAdmin_ip( request.getRemoteAddr() );
		requestDto.setServer_ip( request.getServerName() );
		requestDto.setResult( status );
		requestDto.setMenu_code( menuName );
		
		String errorMessage = (String) auditMap.get(EAdminConstants.ERROR_MESSAGE.getValue());
		if( errorMessage != null ) {
			requestDto.setError_msg(errorMessage);
		}
		insertAuthAudit(requestDto, args);
	}

	public void insertAuthAudit(WebAuditRequestDto requestDto, String args ) {
		String reqMsg = "";
		
		reqMsg = new StringBuilder( "HTTP-Req-Param: " )
				.append( String.format("adminId={%s}, ", requestDto.getAdmin_id()) )
				.append( String.format("requestURI={%s}\n", requestDto.getUrl()) )
				.append( String.format("adminIP={%s}, ", requestDto.getAdmin_ip()) )
				.append( String.format("serverIP={%s}\n", requestDto.getServer_ip()) )
				.append( String.format("result={%s}, ", requestDto.getResult()) )
				.append( String.format("menuCode={%s}\n", requestDto.getMenu_code()) )
				.append( String.format("parameters={%s}\n", args ) )
				.toString();
		
		
		if (  reqMsg.length() >= 1020 ) {
			requestDto.setRequest_msg(reqMsg.substring(0, reqMsg.lastIndexOf("parameters=")) + "요청 파람이 너무 큽니다.");
		} else {
			requestDto.setRequest_msg(reqMsg);
		}
		
		repository.save( requestDto.setInsertEntity(requestDto) );
	}
	
	
//	private String toStringObjectArray(Object[] args) {
//		String result = "";
//		System.out.println("@@ ::: @@1 " + args.length);
//		for(Object arg : args ) {
//			if( arg != null ) {
//				if( arg.getClass().getName().contains("Ljava.lang.String") ) {
//					result += toStringObjectArray((String[]) arg) + ", ";
//				} else {
//					result += arg.toString() + ", ";
//				}
//			}
//			System.out.println("@@ ::: @@2 " + result);
//			
//		}
//		if( result.contains(",") )
//		result = result.substring(0, result.lastIndexOf(", "));
//		System.out.println("@@ ::: @@3 " + result);
//		return result;
//	}

}
