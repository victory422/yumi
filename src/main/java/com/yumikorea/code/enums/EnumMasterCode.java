package com.yumikorea.code.enums;

public enum EnumMasterCode {
	
	OPERATION_CODE("OPERATION_CODE"),
	SYMMETRICKEY_ALGORITHM("SYMMETRICKEY_ALGORITHM"),
	KEYPAIR_ALGORITHM("KEYPAIR_ALGORITHM"),
	KEY_STATE("KEY_STATE"),
	KEY_TYPE("KEY_TYPE"),
	ADMIN_AUTH("ADMIN_AUTH"),
	RESULT_SF("RESULT_SF"),
	USAGE_MASK("USAGE_MASK"),
	ADMIN_OPERATION_CODE("ADMIN_OPERATION_CODE")
	;
	
	private final String masterCodeValue;
	
	EnumMasterCode(String masterCodeValue){
		this.masterCodeValue = masterCodeValue;
	}

	public String getMasterCodeValue() {
		return masterCodeValue;
	}
	
}
