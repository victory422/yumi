package com.yumikorea.admin.dto;

import java.util.Date;

import com.yumikorea.admin.entity.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponseDto {

	private String adminId;
	private String name;
	private String password;
	private String email;
	private Date regDate;
	private Date lastDate;
	private int auth;
	private String userState;
	private String companyName;
	private String deptName;
	private String telNo;
	private int accessCnt;
	
	private String authVal;
	private String userStateVal;
	private String enableType;
	
	public AdminResponseDto( Admin admin ) {
		this.adminId = admin.getAdmin_id();
		this.password = admin.getPassword();
		
		this.lastDate = admin.getLast_date();
		this.regDate = admin.getRegDate();
		
		this.userState = admin.getUser_state();
		this.companyName = admin.getCompany_name() != null ? admin.getCompany_name() : "";
		this.deptName = admin.getDept_name() != null ? admin.getDept_name() : "";
		
		this.accessCnt = admin.getAccess_count();

		this.name = admin.getName();
		this.email = admin.getE_mail();
		this.telNo = admin.getTel_no();
		
		if( admin.getUser_state().equals( "01" ) ) {
			this.userState = "정상";
		} else if( admin.getUser_state().equals( "02" ) ) {
			this.userState = "초기";
		} else {
			this.userState = "잠김";
		}
		
		if( admin.getAuth() == 0 ) {
			this.authVal = "관리자";
		} else if( admin.getAuth() == 1 ) {
			this.authVal = "사용자";
		}
		
	}

	public AdminResponseDto(String adminId, String name, String password, String email, Date regDate, Date lastDate,
			int auth, String userState, String enableType, String companyName, String deptName, String telNo, int accessCnt) {
		super();
		this.adminId = adminId;
		this.password = password;
		this.regDate = regDate;
		this.lastDate = lastDate;
		this.auth = auth;
		this.userState = userState;
		this.enableType = enableType;
		this.companyName = companyName;
		this.deptName = deptName;
		this.accessCnt = accessCnt;
		
		this.name = name;
		this.email = email;
		this.telNo = telNo;
		
		this.authVal = auth == 0 ? "관리자" : "사용자";
		
		if( userState.equals("01") ) this.userStateVal = "정상";
		else if( userState.equals("02") )  this.userStateVal = "초기";
		else if( userState.equals("03") )  this.userStateVal = "잠김";
		
	}
	
	
}
