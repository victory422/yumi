/**
 * Script info
 * 1. file path		: /static/js/views/adminAuthorityJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 관리자 관리 - 화면권한관리 스크립트 
 */
 
 
let g_myAuthoritys = {};
 
let registDeleteAuthorityUser = function() {
	let allCheckboxs = document.querySelectorAll("input[type=checkbox][name=auths]");
	
	let url = "/admin/reigstAuthorityUser";
	let data = [];
	
	allCheckboxs.forEach(function(chks) {
		var authority = {
			authorityId : chks.getAttribute("chkId")
			,adminId : document.getElementById("authority_adminId").value
			,operation : ""
		};
		
		if( chks.checked == true ) {
			authority.operation = "insert";
		} else {
			authority.operation = "delete";
		}
		
		data.push(authority);
	});
	
	$.ajax({
		url: url,
		type: "POST",
		data: JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( data ){
			if( data.status == "success" ){
				successPopup( data.message );	
			} else if( !data.message ) {
				alertPopup( data.message );
			} else {
				alertPopup( data.message );
			}
		}
	});
}
 
 
let openPopupAuthoritys = function(dom) {
	dom.removeAttribute("change-modal-id");
	
	const query = 'input[name="chk"]:checked';
    let selectedElements = document.querySelectorAll( query );
    
    // 체크박스 체크된 항목의 개수
    const selectedElementsCnt = selectedElements.length;
    
    if( selectedElementsCnt == 0 ) {
		alert( "화면권한을 변경할 항목을 선택해주세요." );
		return false;	
	} else if( selectedElementsCnt > 1 ) {
		alert( "권한설정은 하나의 계정만 선택가능합니다." );
		selectedElements = selectedElements[0];
	} else {
		selectedElements = selectedElements[0];
		var adminId = selectedElements.getAttribute("chkid");
		document.getElementById("authority_adminId").value = adminId;
		document.getElementById("auth_title").innerText = "선택된 관리자 : " + adminId;
		dom.setAttribute("change-modal-id","admin_authoritys");
		fn_modalOpen("admin_authoritys");
		g_myAuthoritys = getListAuthority(null, adminId);
		getListAuthority(1, null);
		
	}
}
 
/* 권한 목록 조회 */
let getListAuthority = function(pageNo, adminId) {
	let myAuthoritys = {};
	var url = "/admin/getListAuthority?";
	var flag = "";
	if( adminId != null && typeof adminId != "undefined") {
		flag = "myAuthority";
		url = "/admin/getListMyAuthority?adminId=" + adminId;
	} else {
		if( pageNo ) url += "page=" + pageNo;
	}
	
	$.ajax({
		url: url,
		type: "get",
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			
			if( flag == "myAuthority") {
				myAuthoritys = responseData;
			} else {
				makeAuthorityTable(document.getElementById("authorityTable"), responseData.listAuthority);
				// makePaging :: paging_script.html 내 정의 
				// param : page Object, selector (div ID), function name 
				makePaging(responseData.page1, "page_authority", "getListAuthority");
			}
		}
	});
	
	
	return myAuthoritys;
}


/* 권한 조회 후 테이블 그리기 */
let makeAuthorityTable = function(tbody, authoritys) {
	while ( tbody.hasChildNodes() ) {
		tbody.removeChild(tbody.firstChild);
	}
	
	if( authoritys != null ) {
		authoritys.forEach(function(authority){
			let tr = document.createElement("tr");
			let checkBox = makeCheckBox(authority.authorityId, "chk_" , "auths" );
			let id = document.createElement("td");
			let name = document.createElement("td");
			let modyfyId = document.createElement("td");
			let modifyDate = document.createElement("td");
			var prefix = "td_";
			id.id = prefix + "authorityId";
			name.id = prefix + "authorityName";
			modyfyId.id = prefix + "modifyId";
			modifyDate.id = prefix + "modifyDate";
			id.innerText = authority.authorityId;
			name.innerText = authority.authorityName;
			modyfyId.innerText = authority.modifyId;
			modifyDate.innerText = authority.modifyDate;
			tr.appendChild(checkBox);
			tr.addEventListener("click",function(event){
				event.stopPropagation(); // 이벤트 차단
			});
			tr.appendChild(id);
			tr.appendChild(name);
			tbody.appendChild(tr);
		});
	}
}


/* 체크박스 만들기 */
let makeCheckBox = function( code , prefix, classVal ) {
	let check = document.createElement("td");
	check.setAttribute("class", "list_chk_box");
	check.setAttribute("onclick", "event.cancelBubble=true;");
	let checkBox = document.createElement("input");
	checkBox.setAttribute("type","checkbox");
	checkBox.setAttribute("id", prefix + code );
	checkBox.setAttribute("class", classVal);
	checkBox.setAttribute("name", classVal);
	checkBox.setAttribute("chkId", code);
	let label = document.createElement("label");
	label.setAttribute("for", prefix + code );
	let span = document.createElement("span");
	label.appendChild(span);
	g_myAuthoritys.listAuthority.forEach(function(auth){
		if( code == auth ) {
			checkBox.checked = true;
		}
	});
	check.appendChild(checkBox);
	check.appendChild(label);

	return check;
}