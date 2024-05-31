package com.yumikorea.common.enums;

public enum EAdminConstants {
	LOGIN_ID("loginId"),
	
	STATUS("status"),
	SUCCESS("success"),
	FAIL("fail"),
	MESSAGE("message"),

	REDIRECT_URL("redirectUrl"),

	SESSION_DUPLICATION("session_duplication"),
	CONFIRM_LOGOUT("confirmLogout"),

	SESSION_INFO("session_info"),
	SESSION_ID("sessionId"),

	ROLE_INIT("ROLE_INIT"),
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_USER("ROLE_USER"),
	INIT("INIT"),
	ADMIN("ADMIN"),
	USER("USER"),

	LOGIN_FAIL_MAP("loginFailMap"),

	USER_NAME("userName"),
	PASSWORD("password"),


	// Quartz scheduler constant
	JOB_INTERVAL("jobInterval"),
	FOLER_PATH("folderPath"),
	STAND_DATE("standDate"),
	PARAM_MAP("paramMap"),
	SERVICE("service"),
	UN_USED("unUsed"),
	UN_USED_INT("-1"),
	FALSE("false"),
	PASS("pass"),
	SPACE(" "),
	STR_Y("Y"),
	STR_N("N"),
	STR_YN("YN"),

	// date format
	SIMPLE_DATE_FORMAT("yyyy-MM-dd"),

	// menu manage
	IS_MENU_CHANGED("isMenuChanged"),
	MENU_LIST("menuList"),
	MENU_JSON_PATH("static/json/menuList.json"),
	MENU_NAME("menuName"),


	INSERT("insert"),
	UPDATE("update"),
	DELETE("delete"),
	HASH("#"),

	RESPONSE_BODY("responseBody"),

	AUTHORITY_URL_LIST("authorityUrlList"),
	IS_AUTHORITY_URL_LIST_CHANGE("isAuthorityUrlListChange"),

	HTTP_METHOD_GET("GET"),
	HTTP_METHOD_POST("POST"),
	HTTP_METHOD_PUT("PUT"),
	HTTP_METHOD_DELETE("DELETE"),
	HTTP_METHOD_UPLOAD("UPLOAD"),
	HTTP_METHOD_DOWNLOAD("DOWNLOAD"),

	HOME("home"),

	RESULT_MAP("resultMap"),

	COMMON("common"),

	ALL("all"),

	SUPER_ACCOUNT("yumi-korea"),

	REQUESTED_URI("requestedUri"),

	MAX_ORDER("maxOrder"),
	PAGE("page"),
	
	ERROR_MESSAGE("errorMessage"),
	RESULT_CODE("resultCode"),
	;
	
	
	private String value;
	
	private EAdminConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
