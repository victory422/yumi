package com.yumikorea.admin.dto;

import java.util.Date;

import com.yumikorea.admin.entity.Admin;
import com.yumikorea.common.mvc.dto.BasicRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminRequestDto extends BasicRequestDto {

	private String adminId;
	private String name;
	private String password;
	private String email;
	
	private Date regDate;
	private Date modDate;
	private Date lastDate;
	private Date beforeLastDate;
	
	private String companyName;
	private String deptName;
	private String telNo;
	
	private String delId;
	private Date delDate;
	private String enableType;
	
	private int auth;
	private String userState;
	private int accessCount;
	
	private String srcAdminId;
	private String srcAdminName;
	
	private String newPw;
	private String pwChk;
	
	private String authorityId;
	
	@Builder
	public AdminRequestDto( String adminId, String name, String password, String email, Date regDate, Date modDate, 
			Date lastDate, Date beforeLastDate, String companyName, String deptName, String telNo, 
			String delId, Date delDate, String enableType, int auth, String userState, int accessCount
			, String srcAdminId, String srcAdminName, String newPw, String pwChk ) {
		
		this.adminId = adminId;
		this.name = name;
		this.password = password;
		this.email = email;
		this.regDate = regDate;
		this.modDate = modDate;
		this.lastDate = lastDate;
		this.beforeLastDate = beforeLastDate;
		this.companyName = companyName;
		this.deptName = deptName;
		this.telNo = telNo;
		this.delId = delId;
		this.delDate = delDate;
		this.enableType = enableType;
		this.auth = auth;
		this.userState = userState;
		this.accessCount = accessCount;
		
		this.srcAdminId = srcAdminId;
		this.srcAdminName = srcAdminName;
		
		this.newPw = newPw;
		this.pwChk = pwChk;
		
	}
	
	// 등록 dto -> entity
	public Admin toSaveEntity() {
		
//		try {
//			
//			email = email != null ? AesUtil.encrypt(email) : email;
//			name = name != null ? AesUtil.encrypt(name) : name;
//			telNo = telNo != null ? AesUtil.encrypt(telNo) : telNo;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return Admin.builder()
				.admin_id( adminId )
				.name( name )
				.password( password )
				.company_name( companyName )
				.dept_name( deptName )
				.e_mail( email )
				.auth( auth )
				.tel_no( telNo )
				.regDate( new Date() )
				.before_last_date( new Date() )
				.last_date( new Date() )
				.enable_type( "Y" )
				.user_state( "02" )
				.access_count( 0 )
				.build();
	}
	
	public AdminRequestDto toUpdateEntity() {
		
//		try {
//			email = email != null ? AesUtil.encrypt(email) : email;
//			name = name != null ? AesUtil.encrypt(name) : name;
//			telNo = telNo != null ? AesUtil.encrypt(telNo) : telNo;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return AdminRequestDto.builder()
				.adminId( adminId )
				.name( name )
				.companyName( companyName )
				.deptName( deptName )
				.telNo( telNo )
				.email( email )
				.modDate( new Date() )
				.build();
	}
	/*
	 * 상태변경 Dto to Entity
	 * User_state 
	 * 01: 정상
	 * 02: 초기
	 * 03: 잠김
	 * */
	public Admin toUpdateStEntity() {
		return Admin.builder()
				.admin_id( adminId )
				.password( password )
				.user_state( "02" )
				.access_count( 0 )
				.mod_date( new Date() )
				.build();
	}
	
	/* 비밀번호 변경 */
	public Admin toUpdatePwEntity() {
		return Admin.builder()
				.admin_id( adminId )
				.password( password )
				.mod_date( new Date() )
				.build();
	}
	
	/* 최초 로그인 비밀번호 변경*/
	public Admin toInitLoginEntity() {
		return Admin.builder()
				.admin_id( adminId )
				.password( password )
				.user_state( "01" )
				.mod_date( new Date() )
				.build();
	}
	
	/* 삭제 */
	public Admin toDeleteEntity() {
		return Admin.builder()
				.admin_id( adminId )
				.del_id( delId )
				.del_date( new Date() )
				.enable_type( "N" )
				.build();
	}

	@Override
	public String toString() {
		return "AdminRequestDto [adminId=" + adminId + ", name=" + name + ", password=" + password + ", email=" + email
				+ ", regDate=" + regDate + ", modDate=" + modDate + ", lastDate=" + lastDate + ", beforeLastDate="
				+ beforeLastDate + ", companyName=" + companyName + ", deptName=" + deptName + ", telNo=" + telNo
				+ ", delId=" + delId + ", delDate=" + delDate + ", enableType=" + enableType + ", auth=" + auth
				+ ", userState=" + userState + ", accessCount=" + accessCount + ", srcAdminId=" + srcAdminId
				+ ", srcAdminName=" + srcAdminName + ", newPw=" + newPw + ", pwChk=" + pwChk + "]";
	}
	
}
