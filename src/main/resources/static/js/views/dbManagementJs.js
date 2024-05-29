/**
 * Script info
 * 1. file path		: /static/js/views/dbManagementJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: DB관리 스크립트 
 */
 
 
 
/* 화면 로드 후 이벤트 */
window.addEventListener("load", function(){
//	setDBAttributeReadonly();

	document.getElementById("dbTel").addEventListener("keyup", function() {
		this.value = inputPhoneNumberValidation(this.value);
	});
});


 /* 권한 목록 조회 */
let getListAuthority = function(pageNo) {
	let authorityName = document.getElementById("search_authorityName").value;
	var url = "/authority/getListAuthority?";
	if( authorityName ) url += "authorityName=" + authorityName;
	if( pageNo ) url += "&page=" + pageNo;
	
	$.ajax({
		url: url,
		type: "get",
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			initData();
			g_listAuthority = responseData.listAuthority;
			makeAuthorityTable(document.getElementById("authorityTable"), responseData.listAuthority);
			
			// makePaging :: paging_script.html 내 정의 
			// param : page Object, selector (div ID), function name 
			makePaging(responseData.page1, "page_authority", "getListAuthority");
			
//			document.getElementById("btn_updateUrl").setAttribute("style","display:none;");
			setDisplay(document.getElementById("btn_updateUrl"), false);
//			document.getElementById("btn_registUrl").setAttribute("style","display:none;");
			setDisplay(document.getElementById("btn_registUrl"), false);
//			document.getElementById("btn_deleteUrl").setAttribute("style","display:none;");
			setDisplay(document.getElementById("btn_deleteUrl"), false);
		}
	});
}


let setDBAttributeReadonly = function() {
	document.getElementsByName("db_attr").forEach(function(d) {
		d.setAttribute("readonly", "readonly");
	});
}

/* DB등록 팝업 */
let openDBRegistPopup = function() {
	fn_modalOpen("dbRegistration");
	document.getElementById("db_modalTitle").innerText = "DB 등록";
	// 공통코드 호출 : 성별
	matchingCodeDetailToComponent({
		id: "dbGender"
		,masterCode : "GENDER"
		,valueCode: "code"
	});
	
	// 공통코드 호출 : 지점
	matchingCodeDetailToComponent({
		id: "deptCode"
		,masterCode : "DEPARTMENT"
		,valueCode: "code"
	});
	
	
	setSelectBoxValue("dbGender", "W");	// 여성 기본 선택
	
	
	document.querySelectorAll("div[button-type]").forEach(function(d){
		if( d.getAttribute("button-type") == "POST" ) {
			d.removeAttribute("style");
		} else {
			d.setAttribute("style", "display:none;");
		}
	});
}

/* DB 목록조회 */
let getDBList = function(page) {
	if ( typeof page == "undefined" ) {
		page = 1;
	}
	let data = {};
	document.getElementsByName("db_attr").forEach(function(d) {
    	data[d.id] = d.value;
	});
	
	let url = "/db/list";
	$.ajax({
		url: url,
		type: "post",
		data: JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			console.log(responseData);
			alertPopup(responseData.message);
		}
	});	
}

/* DB등록 */
let goRegistDB = function() {
	let data = {};
	document.getElementsByName("db_attr").forEach(function(d) {
    	data[d.id] = d.value;
	});
	
	let url = "/db/register";
	$.ajax({
		url: url,
		type: "post",
		data: JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			console.log(responseData);
			alertPopup(responseData.message);
		}
	});	
}



/* DB 수정 */
let goUpdateDB = function() {
	
}
