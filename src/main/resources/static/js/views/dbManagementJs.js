/**
 * Script info
 * 1. file path		: /static/js/views/dbManagementJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: DB관리 스크립트 
 */
 
let g_adminList = []; 
 
/* 화면 로드 후 이벤트 */
window.addEventListener("load", function(){
	loadCommonCodes();
	getDBList();
	document.getElementById("dbTel").addEventListener("keyup", function() {
		this.value = inputPhoneNumberValidation(this.value);
	});
	document.getElementById("dbTel").addEventListener("blur", function() {
		this.value = inputPhoneNumberValidation(this.value);
	});
	
	document.querySelectorAll("#dbName, #dbTel").forEach(function(dom){
		dom.addEventListener("keyup", function() {
			checkDuplicate(this);
		});
		dom.addEventListener("blur", function() {
			checkDuplicate(this);
		});
	});
	
	g_adminList = getAdminList();

	document.getElementById("adminId").addEventListener("keyup", function(e) {
		findAdminAccount(e, this);
	});
	
});


let loadCommonCodes = function() {
		// 공통코드 호출 : 성별
	matchingCodeDetailToComponent({
		id: "dbGender"
		,masterCode : "GENDER"
		,valueCode: "code"
	});
	
	// 공통코드 호출 : 유입경로
	matchingCodeDetailToComponent({
		id: "dbRegPath"
		,masterCode : "DB_REGIST_PATH"
		,valueCode: "code"
	});
	
	// 공통코드 호출 : 지점(검색)
	matchingCodeDetailToComponent({
		id: "src_deptCode"
		,masterCode : "DEPARTMENT"
		,valueCode: "code"
		,all: true
	});
	
	// 공통코드 호출 : 지점
	matchingCodeDetailToComponent({
		id: "deptCode"
		,masterCode : "DEPARTMENT"
		,valueCode: "code"
	});
	
	// 공통코드 호출 : 컨택결과
	matchingCodeDetailToComponent({
		id: "setContactResult"
		,masterCode : "CONTACT_RESULT"
		,valueCode: "code"
	});	
	// 공통코드 호출 : 컨택결과
	matchingCodeDetailToComponent({
		id: "srcContactResult"
		,masterCode : "CONTACT_RESULT"
		,valueCode: "code"
		,all: true
	});
}


let setDBAttributeReadonly = function() {
	document.getElementsByName("db_attr").forEach(function(d) {
		d.setAttribute("readonly", "readonly");
	});
}

/* DB등록 팝업 */
let openDBRegistPopup = function() {
	document.getElementById("db_modalTitle").innerText = "DB 등록";
	openDBPopup("POST");
}


let openDBPopup = function(method) {
	if( typeof method == "undefined" ) {
		method = "POST";
	}
	method = method.toUpperCase();
	fn_modalOpen("dbRegistration");
	
	setSelectBoxValue("dbGender", "W");	// 여성 기본 선택
	
	// 수정일 때
	if( method == "PUT" ) {
		document.getElementById("dbName").setAttribute("readonly", "readonly");
	}
	
	document.querySelectorAll("div[button-type]").forEach(function(d){
		if( d.getAttribute("button-type") == method ) {
			d.removeAttribute("style");
		} else {
			d.setAttribute("style", "display:none;");
		}
	});
}

/* DB 목록조회 */
let getDBList = function(page) {
	if ( isNull(page) ) {
		page = 1;
	}
	
	let param = {
		page:page
	};
	
	param.srcDbName = document.getElementById("src_dbName").value;
	param.srcDeptCode = document.getElementById("src_deptCode").value;
	param.srcDbTel = document.getElementById("src_dbTel").value;

	;
	
	let url = "/db/get-list" + toQueryString(param);
	$.ajax({
		url: url,
		type: "get",
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			var tbody = document.getElementById("tbody_dbList");
			makeTable(tbody, responseData.resultMap);
			makePaging(responseData.page, "page_dbList", "getDBList");
		}
	});	
}

/* DB등록 */
let goRegistDB = function() {
	let data = {};
	document.getElementsByName("db_attr").forEach(function(d) {
    	data[d.id] = d.value;
	});
	data["adminId"] = document.getElementById("adminId").getAttribute("temp_adminId");
	data["dbSeq"] = null;
	let url = "/db/register";
	$.ajax({
		url: url,
		type: "post",
		data: JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			alertPopup(responseData.message);
			getDBList();
		}
	});	
}

/* DB 수정 */
let goUpdateDB = function() {
	let data = {};
	document.getElementsByName("db_attr").forEach(function(d) {
    	data[d.id] = d.value;
	});
	
	data["adminId"] = document.getElementById("adminId").getAttribute("temp_adminId");
	
	let url = "/db/update";
	$.ajax({
		url: url,
		type: "put",
		data: JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			alertPopup(responseData.message);
			getDBList();
		}
	});	
	
}

/* DB 삭제 */
let deleteDB = function() {
	const query = 'input[name="chk"]:checked';
    const selectedElements = document.querySelectorAll( query );
    
    // 체크박스 체크된 항목의 개수
    if( selectedElements.length == 0 ) {
		alertPopup( "삭제할 항목을 선택해주세요." );
		return false;	
	}

	
	let data = [];
	
	selectedElements.forEach(function(c) {
		let db = {};
		db.dbSeq = c.getAttribute("id").replace("chk_","");
		data.push(db);
	});
	
	let url = "/db/delete";
	$.ajax({
		url: url,
		type: "delete",
		data: JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			alertPopup(responseData.message);
			getDBList();
		}
	});	
}


let openDbDetail = function(db) {
	document.getElementById("db_modalTitle").innerText = "DB 상세";
	openDBPopup("PUT");
	document.getElementsByName("db_attr").forEach(function(d) {
    	for(key in db ) {
	    	if( d.id == key ) {
				if( d.id == "adminId" ) {
					var admin = g_adminList.filter(function(v){
						if ( v.adminId == db.adminId ) {
							return v;
						}
					})[0];
					if( !isNull(admin) && !isNull(admin.name) ) {
						d.value = admin.name;
						d.setAttribute("temp_adminId", admin.adminId);
					} 
				} else if( d.tagName.toUpperCase() == "SELECT" ) {
					setSelectBoxValue(d.id, db[key]);
				} else {
					d.value = db[key];
				}
			}
		}
	});
	
}

let makeTable = function(tbody, dbList) {
	while ( tbody.hasChildNodes() ) {
		tbody.removeChild(tbody.firstChild);
	}
	
	if( dbList != null ) {
		dbList.forEach(function(db){
			let tr = document.createElement("tr");
			
			let check = makeCheckBox(db.dbSeq, "chk_" );
			let dbName = document.createElement("td");
			let dbTel = document.createElement("td");
			
			var prefix = "td_";
			dbName.id = prefix + "dbName";
			dbTel.id = prefix + "dbTel";
			
			dbName.innerText = db.dbName;
			dbTel.innerText = db.dbTel;
			
			tr.appendChild(check);
			tr.setAttribute("change-modal-id", "dbRegistration");
			tr.addEventListener("click",function(event){
				openDbDetail(db);
			});
			
			check.querySelector("input").addEventListener("click", function(event){
				if( this.checked ) {
					loadDBMemo(db);
				}
			});
			
			tr.appendChild(dbName);
			tr.appendChild(dbTel);
			tbody.appendChild(tr);
		});
	}
}



/* 체크박스 만들기 */
let makeCheckBox = function( code , prefix ) {
	let check = document.createElement("td");
	check.setAttribute("class", "list_chk_box");
	check.setAttribute("onclick", "javascript:event.cancelBubble=true;");
	let checkBox = document.createElement("input");
	checkBox.setAttribute("type","checkbox");
	checkBox.setAttribute("class","chk");
	checkBox.setAttribute("id", prefix + code );
	checkBox.setAttribute("name","chk");
	let label = document.createElement("label");
	label.setAttribute("for", prefix + code );
	let span = document.createElement("span");
	label.appendChild(span);
	check.appendChild(checkBox);
	check.appendChild(label);
	return check;
}

let loadDBMemo = function(db) {
	clearChildNodes(document.getElementById("tbody_memoList"));
	document.getElementById("tbody_detail").querySelectorAll("tr > td").forEach(function(td){
		var td_id = td.id.replace("td_","");
		for(key in db) {
			if( td_id == key ) {
				td.innerText = db[key];
			}
		}
	});
	getListMemo();
}

let registMemo = function() {
	let data = {
		dbSeq :document.getElementById("tbody_detail").querySelector("#td_dbSeq").innerText
		, adminId : document.getElementById("tbody_detail").querySelector("#td_adminId").innerText
		, memoContent : document.getElementById("memoContent").value
		, memoResult : document.getElementById("setContactResult").value
	};
	
	if( isNull(data.memoContent.trim()) ) {
		alertPopup("작성된 메모내용이 없습니다.");
		return;
	}
	
	let url = "/db/registMemo";
	$.ajax({
		url: url,
		type: "post",
		data: JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			alertPopup(responseData.message);
			if( responseData.status == "success" ) {
				// 값 초기화
				document.getElementById("memoContent").value = "";
				setSelectBoxValue("memoContent", "C01");
				getListMemo();
			}
		}
	});		
}

let getListMemo = function() {
	let data = {
		srcDbSeq :document.getElementById("tbody_detail").querySelector("#td_dbSeq").innerText
		, srcContactResult : document.getElementById("srcContactResult").value
		, srcTo : document.getElementById("srcTo").value
		, srcFrom : document.getElementById("srcFrom").value
	};
	
	if( isNull(data.srcDbSeq) ) {
		alertPopup("선택된 DB가 없습니다.");
		return;
	}
	
	let divArea = document.getElementById("tbody_memoList");
	clearChildNodes(divArea);
		
	let url = "/db/get-list-memo" + toQueryString(data);
	$.ajax({
		url: url,
		type: "get",
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			if( responseData.status == "success" ) {
				setMemoList(responseData.resultMap);
			}
		}
	});
}

let setMemoList = function(memos) {
	memos.forEach(function(memo){
		var cloneTr = document.getElementById("tr_memoList").cloneNode(true);
		cloneTr.removeAttribute("style");
		cloneTr.querySelector("#td_memoRegistDate").innerText = memo.registDate;
		cloneTr.querySelector("#td_memoAdminId").innerText = memo.adminId;
		cloneTr.querySelector("#td_memoResult").innerText = memo.memoResult;
		
		var textAreaInTable = cloneTr.querySelector("#td_memoContent");
		var textArea = document.createElement("textarea");
		textArea.setAttribute("class", "tb_result textarea");
		textArea.setAttribute("readonly", "readonly");
		textArea.value = memo.memoContent;
		textAreaInTable.appendChild(textArea);
		document.getElementById("tbody_memoList").appendChild(cloneTr);
	});
}


let checkDuplicate = function(dom) {
	let srcStr = dom.value;
	let bifurcation = dom.id;
	let result = false;
	$.ajax({
		url: "/common/check-duplicate?srcStr=" + srcStr + "&bifurcation=" + bifurcation,
		type: "get",
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			if( responseData.status == "success" ) {
				result = responseData.resultMap
			} else {
				result = false;
			}
			chgServiceSel(dom, result);
		}
	});
}

  // 유효 값 확인
let chgServiceSel = function (dom, res){
	dom.setAttribute("duplicate", "Y");
	// 값 지웠을때
	if ( dom.value.length == 0 ) {
		$("#"+dom.id).removeAttr("style");
		dom.removeAttribute("duplicate");
    // 중복일 때
	} else if( !res ) {
		$("#"+dom.id).css("background-color","rgba(245, 0, 0, 0.22)");
		dom.setAttribute("duplicate", "Y");
	// 중복 아닐 때
	} else {
		$("#"+dom.id).css("background-color","rgba(0, 245, 75, 0.22)");
		dom.setAttribute("duplicate", "N");
	}
}

/* 어드민 목록 조회 */
let getAdminList = function(rows) {
	var result = [];
	var paramData = {
		rows : 99999	// limit 제한 없이 조회하기 위한 변수 입력
	};
	
	if( typeof rows != "undefined" ) {
		paramData.rows = rows;
	}
	
	$.ajax({
		url: "/common/admin-list",
		type: "get",
		data: paramData,
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( rst ){
			result = rst.resultMap;
		}
	});
	
	return result;	
}



/* 허가 URL 목록 등록 */
let findAdminAccount = function(e, dom) {
	// 검색어
	let value = dom.value.trim();

	let matchDataList = [];
	// 자동완성 필터링
	matchDataList = g_adminList.filter(function(admin) {
		if( value != "" && admin.name.indexOf(value) > -1 ) {
			return admin;
		}
	});
	
	switch (e.keyCode) {
		// UP KEY
		case 38:
			nowIndex = Math.max(nowIndex - 1, 0);
		break;
		
		// DOWN KEY
		case 40:
			nowIndex = Math.min(nowIndex + 1, matchDataList.length - 1);
		break;
		
		// ENTER KEY
		case 13:
			var adminName = matchDataList[nowIndex].name;
			var adminId = matchDataList[nowIndex].adminId;
			showListSetData(adminId, adminName);
			matchDataList = [];
			value = "";
			nowIndex = 0;
		break;
		  
		// 그외 다시 초기화
		default:
			nowIndex = 0;
		break;
	}
	
	showList(matchDataList, value, nowIndex);
}

let showList = function(data, value, nowIndex) {
	let sDiv = document.querySelector(".autocomplete");
	
	 // 정규식으로 변환
	const regex = new RegExp(`(${value})`, "g");
	
	sDiv.innerHTML = data.map(
		(admin,index) => `
		<div class='${nowIndex === index ? "active" : ""}'
		onclick='showListSetData("${admin.adminId}","${admin.name}")'>
			${admin.name.replace(regex, "<mark>$1</mark>")}
		</div>
		`
	).join("");
	if( data.length > 0 ) {
		sDiv.removeAttribute("style");
	} else {
		sDiv.setAttribute("style","display:none;");
	}
}

let showListSetData = function(adminId, adminName) {
	document.getElementById("adminId").value = adminName;
	document.getElementById("adminId").setAttribute("temp_adminId", adminId);
	showList([],"",0);
}

