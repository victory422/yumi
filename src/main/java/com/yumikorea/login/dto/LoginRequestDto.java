package com.yumikorea.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginRequestDto {
	
	private String loginId;
	private String loginPw;
	private String orgPw;
	private String newPw;
	private String newPwChk;
	private String sessionId;
	
	@Builder
	public LoginRequestDto( String loginId, String loginPw, String orgPw, String newPw, String newPwChk, String sessionId ) {
		this.loginId = loginId;
		this.loginPw = loginPw;
		this.orgPw = orgPw;
		this.newPw = newPw;
		this.newPwChk = newPwChk;
		this.sessionId = sessionId;
	}
	
	
	public boolean equals(Object obj) {
	    if (obj instanceof LoginRequestDto) {
	        return this.loginId.equals(((LoginRequestDto) obj).loginId);
	    }
	    return false;
	}
	
	public int hashCode() {
	    return this.loginId.hashCode();
	}

}
