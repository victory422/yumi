package com.yumikorea.setting.dto.response;

import lombok.Getter;

@Getter
public class UserReponseDto {

	// 목록 : 아이디, 이름, 이메일, 서버코드, 서버명, 등록일, 상태
	// 상세 : 아이디, 이름, 직급, 부서, 서버명, 서버코드, 상태, 권한, 이메일
	
	// 아이디
	private String userId;
	
	// 이름
	private String userName;
	
	// 이메일
	private String email;
	
	// 서버코드
	private String servInfoSeqId;
	
	// 서버명
	private String servInfoName;
	
	// 등록일
	private String creDate;
	
	// 상태
	private String stateNm;
	
	// 직급
	private String jobLevel;
	
	// 부서
	private String departTeam;
	
	// 권한
	private String authNm;
	
	// 마지막 수정일
	private String lastChangeDate;
	
	// 접근횟수
	private String authFailCnt;

	
}
