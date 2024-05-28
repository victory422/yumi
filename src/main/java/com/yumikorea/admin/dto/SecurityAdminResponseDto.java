package com.yumikorea.admin.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.yumikorea.admin.entity.Admin;
import com.yumikorea.common.enums.EAdminConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "loginId")
public class SecurityAdminResponseDto implements UserDetails {
	private User user;
	private String loginId;
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
	private Boolean isEnabled;
	private String authVal;
	private String userStateVal;
	private String remoteAddr;
	private String serverName;
	@SuppressWarnings("unused")
	private Collection<GrantedAuthority> authorities;
	
	public SecurityAdminResponseDto( User user ) {
		this.user = user;
		this.loginId = user.getUsername();
	}
	
	public SecurityAdminResponseDto( Admin admin ) {
		this.loginId = admin.getAdmin_id();
		this.password = admin.getPassword();
		
		this.lastDate = admin.getLast_date();
		this.regDate = admin.getRegDate();
		
		this.userState = admin.getUser_state();
		
		this.companyName = admin.getCompany_name() != null ? admin.getCompany_name() : "";
		this.deptName = admin.getDept_name() != null ? admin.getDept_name() : "";
		
		this.accessCnt = admin.getAccess_count();

		this.name = admin.getName() != null ? admin.getName() : "";
		this.email = admin.getE_mail() != null ? admin.getE_mail() : "";
		this.telNo = admin.getTel_no() != null ? admin.getTel_no() : "";
		
		// 유저상태 01 정상(admin) 02 초기 , 03 잠김
		this.userState = admin.getUser_state();
		
		if( admin.getAuth() == 0 ) {
			this.auth = 0;
			this.authVal = "관리자";
			this.authorities = Arrays.asList(new SimpleGrantedAuthority("admin"));
		} else if( admin.getAuth() == 1 ) {
			this.auth = 1;
			this.authVal = "사용자";
			this.authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
		}
		
		this.isEnabled = false;
		
		if( EAdminConstants.STR_Y.getValue().equalsIgnoreCase(admin.getEnable_type()) ) {
			this.isEnabled = true;
			setIsEnabled(this.isEnabled);
		}
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return this.user.getAuthorities();
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}
	
	@Override
	public String getPassword() {
		if( this.user != null ) {
			return this.user.getPassword();
		} else {
			return this.password;
		}
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.user.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isEnabled();
	}
	
	public void setIsEnabled(Boolean isEnabled ) {
		this.isEnabled = isEnabled;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
