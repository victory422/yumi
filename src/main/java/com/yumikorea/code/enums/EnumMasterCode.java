package com.yumikorea.code.enums;

public enum EnumMasterCode {
	
	OPERATION_CODE("OPERATION_CODE"),
	ADMIN_AUTH("ADMIN_AUTH"),
	RESULT_SF("RESULT_SF"),
	GENDER("GENDER"),
	DEPARTMENT("DEPARTMENT"),
	ADMIN_OPERATION_CODE("ADMIN_OPERATION_CODE"),
	DB_REGIST_PATH("DB_REGIST_PATH"),
	CONTACT_RESULT("CONTACT_RESULT")
	;
	
	private final String masterCodeValue;
	
	EnumMasterCode(String masterCodeValue){
		this.masterCodeValue = masterCodeValue;
	}

	public String getMasterCodeValue() {
		return masterCodeValue;
	}
	
}
