package com.yumikorea.setting.dto.request;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.yumikorea.common.mvc.dto.BasicRequestDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuDto extends BasicRequestDto implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String menuCode;
	private int menuDepth;
	private String menuName;
	private String menuUpperCode;
	private int menuOrder;
	private String menuUrl;
	private String menuUse;
	private String menuAuth;
	private String modifyId;
	private Date modifyDate;
	private String menuAttribute1;
	private String menuAttribute2;
	private String menuAttribute3;
	private Boolean isGetChild;
	private Boolean isSearchDownMenu;
	private String method;
	private Boolean thisScreen;
	private Set<String> menuCodes;
}
