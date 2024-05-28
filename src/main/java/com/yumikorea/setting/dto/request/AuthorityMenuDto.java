package com.yumikorea.setting.dto.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yumikorea.common.mvc.dto.BasicRequestDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorityMenuDto extends BasicRequestDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String loginId;
	private String adminId;
	private String menuCode;
	private String menuName;
	private int menuDepth;
	private String menuUrl;
	private String method;
	private List<String> methods;
	private String menuUpperCode;
	private Boolean isGetChild;
	private String authorityId;
	private String authorityName;
	private String modifyId;
	private Date modifyDate;
	private String authorityDesc;
	private String operation;
	private String menuAttribute2;
}
