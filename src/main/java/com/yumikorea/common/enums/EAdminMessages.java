package com.yumikorea.common.enums;

public enum EAdminMessages {
	ERROR_OCCURS("오류가 발생했습니다. 관리자에게 문의하세요."),
	
	ID_DUPLICATE("이미 존재하는 아이디입니다."),
	NAME_DUPLICATE("이미 존재하는 이름입니다."),

	CREATE_SUCCESS("생성에 성공했습니다."),
	CREATE_FAIL("생성에 실패했습니다. 관리자에게 문의하세요."),

	REGISTER_SUCCESS("등록에 성공했습니다."),
	REGISTER_FAIL("등록에 실패했습니다. 관리자에게 문의하세요."),

	DELETE_SUCCESS("삭제가 완료되었습니다."),
	DELETE_FAIL("삭제중 문제가 발생했습니다. 관리자에게 문의하세요."),

	WRONG_PARAMETER("입력한 파라미터에 문제가 발생했습니다."),

	UPDATE_SUCCESS("수정되었습니다."),
	UPDATE_FAIL("수정에 실패했습니다. 관리자에게 문의하세요."),

	UPDATE_STATE_SUCCESS("상태가 변경되었습니다."),
	UPDATE_STATE_FAIL("상태 변경에 실패했습니다. 관리자에게 문의하세요."),

	SESSION_INVALID("세션이 만료되었습니다."),

	UNDEFINED_ACCOUNT("아이디/비밀번호가 일치하지 않습니다."),
	DISABLED_ACCOUNT("계정이 비활성화 되었습니다. 관리자에게 문의하세요."),
	CREDENTIALS_ACCOUNT("비밀번호 유효기간이 만료 되었습니다. 관리자에게 문의하세요."),
	SESSION_AUTHENTICATION_ACCOUNT("타 사용자가 로그인중입니다. 타 사용자를 로그아웃시키고 로그인하시겠습니까?"),
	CANNOT_FIND_USER("사용자를 찾을 수 없습니다."),
	LOCKED_ACCOUNT("사용자 계정이 잠겨 있습니다. 관리자에게 문의하세요."),
	LOCKING_ACCOUNT("계정이 잠금처리되었습니다. 관리자에게 문의하세요."),
	EXCEPTION_ACCOUNT("알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요."),

	
	PASSWORD_CHANGE_SUCCESS("비밀번호가 정상적으로 변경되었습니다. 다시 로그인해주세요."),
	PASSWORD_CHANGE_FAIL("초기 비밀번호가 다릅니다. 다시 시도해주세요."),
	PASSWORD_CHANGE_ERROR("새 비밀번호가 기존 비밀번호와 동일합니다."),
	ILLEGAL_PASSWORD("비밀번호가 일치하지 않습니다. 다시 입력하세요."),
	
	PEM_DOWNLOAD_ERROR("파일 다운로드에 실패했습니다. 관리자에게 문의하세요."),
	
	HAS_CHILD_CODE("삭제에 실패했습니다. 공통코드의 하위 상세코드가 있습니다."),
	
	NOT_PERMISSION_URL("The user does not have valid permissions for the requested URI."),
	
	INIT_LOGIN("최초 로그인입니다. 초기 비밀번호 설정 페이지로 이동합니다."),
	
	WEB_AUDIT_DELETE_CNT("WebAudit 삭제 건 수 : "),
	PEM_DELETE_CNT("PEM File 삭제 건 수 : "),
	
	MENU_CREATE_FAIL_CODE_DUPLICATE("생성에 실패했습니다. 분류코드가 중복됩니다."),
	MENU_CREATE_FAIL_URL_DUPLICATE("생성에 실패했습니다. url이 중복됩니다."),
	
	NO_SEARCH_RESULT("조회된 값이 없습니다."),
	;
	
	
	
	private String message;
	
	private EAdminMessages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
