/**
 * Script info
 * 1. file path		: /static/js/views/authorityManagementJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 권한관리 스크립트 
 */

// 전역 변수 사용
let g_listAuthority = [];
let g_listAuthorityUrl = [];
let g_selectedAuthority = {};
let g_menuList = {};
let g_staticAll = "all";

let g_methodNames = {
	0 : "GET"
	,1 : "POST"
	,2 : "PUT"
	,3 : "DELETE"
	,4 : "UPLOAD"
	,5 : "DOWNLOAD"
};

let initData = function() {
	g_listAuthority = [];
	g_listAuthorityUrl = [];
	g_selectedAuthority = {};
	document.getElementById("selected_authorityId").value = "";
	document.getElementById("search_menuName").value = "";
	
	makeAuthorityUrlTable(document.getElementById("authorityUrlTable"), []);
	makePaging({}, "page_authorityUrl", "getListAuthorityUrl" );
	
}

/* 화면 로드 후 이벤트 */
window.addEventListener("load", function(){
	// 권한 목록 조회
	getListAuthority(1);
});

/* 권한 조회 후 테이블 그리기 */
let makeAuthorityTable = function(tbody, authoritys) {
	while ( tbody.hasChildNodes() ) {
		tbody.removeChild(tbody.firstChild);
	}
	
	if( authoritys != null ) {
		authoritys.forEach(function(authority){
			let tr = document.createElement("tr");
			let radio = makeRadioBox(authority.authorityId, "rdo_" );
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
			if (tbody.id == "authorityTable" ) {
				tr.appendChild(radio);
				tr.setAttribute("change-modal-id", "updateAuthority");
				tr.addEventListener("click",function(event){
//					event.stopPropagation(); // 이벤트 차단
					openAuthorityPopup(authority);
				});
			}
			tr.appendChild(id);
			tr.appendChild(name);
//			tr.appendChild(modyfyId);
//			tr.appendChild(modifyDate);
			tbody.appendChild(tr);
		});
	}
}

/* URL 조회 후 테이블 그리기 */
let makeAuthorityUrlTable = function(tbody, authorityUrls) {
	while ( tbody.hasChildNodes() ) {
		tbody.removeChild(tbody.firstChild);
	}
	
	if( authorityUrls != null ) {
		authorityUrls.forEach(function(authorityUrl){
			let tr = document.createElement("tr");
			let checkBox = makeCheckBox(authorityUrl.menuCode, "chk_" , "chk" );
			let authorityName = document.createElement("td");
			let menuName = document.createElement("td");
			var prefix = "td_";
			authorityName.id = prefix + "authorityId";
			authorityName.innerText = authorityUrl.authorityName;
			menuName.id = prefix + "menuCode";
			menuName.innerText = authorityUrl.menuName;
			checkBox.querySelector("input").setAttribute("onclick","fn_selectCheckBox(this)");
			tr.appendChild(checkBox);
			tr.appendChild(authorityName);
			tr.appendChild(menuName);
			setMethods(authorityUrl.methods, authorityUrl.menuCode, tr);
			tr.setAttribute("data-authorityId",authorityUrl.authorityId);
			tr.setAttribute("data-menuCode", authorityUrl.menuCode);
			tbody.appendChild(tr);
		});
	}
}

let fn_selectCheckBox = function(dom) {
	let tr = dom.parentElement.parentElement;
	tr.querySelectorAll("input.methods").forEach(function(d){
		d.checked = dom.checked;
	});
}

let setMethods = function(arr, menuCode, tr) {
	for( var i = 0 ; i < Object.keys(g_methodNames).length; i ++ ) {
		let mName = g_methodNames[i];
		let checkBox = makeCheckBox(mName, menuCode + "_" , "methods" );
		arr.forEach(function(method){
			if( method === mName ) {
				checkBox.querySelector("input").checked = true;
			}
		});
		checkBox.querySelector("input").addEventListener("click", function() {
			var thisTr = this.parentElement.parentElement;
			var firstCheckBox = thisTr.querySelector("input[name=chk]");
			firstCheckBox.checked = !isEqualsChecks(firstCheckBox);
		});
		tr.appendChild(checkBox);
	};
}


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


/* 권한 목록 등록, 수정 */
let openAuthorityPopup = function(data) {
	fn_modalOpen("updateAuthority");
	
	if( typeof data != "undefined" && data.authorityId != null && data.authorityId != "" ) {
		document.getElementById("new_authority_operation").value = "update"; 
		document.getElementById("new_authorityId").value = data.authorityId;
		document.getElementById("new_authorityId").setAttribute("readonly","readonly");
		document.getElementById("new_authorityName").value = data.authorityName;
		document.getElementById("new_authorityDesc").value = data.authorityDesc;
		document.getElementById("authority_modalTitle").innerText = "권한 상세";
		document.getElementById("authority_btn_save").innerText = "수정";
		document.getElementById("authority_btn_save").parentElement.setAttribute("button-type", "PUT");
	} else {
		document.getElementById("new_authority_operation").value = "regist";
		document.getElementById("new_authorityId").value = "";
		document.getElementById("new_authorityId").removeAttribute("readonly");
		document.getElementById("new_authorityName").value = "";
		document.getElementById("new_authorityDesc").value = "";
		document.getElementById("authority_modalTitle").innerText = "권한 등록";
		document.getElementById("authority_btn_save").innerText = "등록";
		document.getElementById("authority_btn_save").parentElement.setAttribute("button-type", "POST");
	}
	
	authorityButtonsController();
}

/* 권한 등록 , 수정 */
let goRegistAuthority = function() {
	let data = {
		authorityId : document.getElementById("new_authorityId").value
		,authorityName : document.getElementById("new_authorityName").value
		,authorityDesc : document.getElementById("new_authorityDesc").value
	};
	
	if( data.authorityId == "" || data.authorityName == "" ) {
		alertPopup("권한ID와 권한명은 필수값입니다.");
		return false;
	}
	
	let operation = document.getElementById("new_authority_operation").value;
	let url = "";
	let method = "";
	if( operation == "regist" ) {
		url = "/authority/registAuthority";
		method = "POST";
	} else if( operation == "update" ) {
		url = "/authority/updateAuthority";
		method = "PUT";
	}
	
	$.ajax({
		url :url,
		type: method,
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(data),
		success: function( data ){
			alertPopup( data.message );
			if( data.status == "success" ) {
				getListAuthority(1);
			}
		}
	});
}

/* 권한 삭제 */
let deleteAuthority = function() {
	let cnt = 0;
	let data = [];
	document.querySelectorAll(".rdo").forEach(function(v){
		if ( v.checked ) {
			cnt ++;
			data.push(v.getAttribute("data-id"));
		}
	});
	
	if( cnt == 0 ) {
		alertPopup("선택된 row가 없습니다.");
		return;
	}
	
	confirmPopup("선택한 권한을 삭제하시겠습니까?");
	
	document.querySelector( ".js-modal-next" ).onclick = function(e){
		e.stopPropagation();
		
		
		$.ajax({
			url: "/authority/deleteAuthority",
			type: "DELETE",
			data: JSON.stringify(data),
			contentType: "application/json; charset=UTF-8",
			success: function( data ){
				alertPopup( data.message );
				if( data.status == "success" ) {
					getListAuthority(1);
				}
			}
		});
	}
}

/* 메뉴 목록 조회 */
let getAuthorityMenulist = function(authorityId) {
	if( typeof authorityId == "undefined" ) {
		alertPopup("선택된 권한이 없습니다.");
		return;
	}
	var url = "/authority/getAuthorityMenulist?authorityId=" + authorityId;
	$.ajax({
		url: url,
		type: "get",
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			g_menuList = [];
			responseData.menuList.forEach(function(menu) {
				if( menu.menuDepth == 1 && menu.menuUrl == "#" ) {
					menu.menuName = menu.menuName + " (모든 하위 화면)";
					menu.operation = g_staticAll;
				} else {
					menu.operation = "";
				}
				g_menuList.push(menu);
			});
			var tempObj = {
				menuName : "모든 화면"
				,menuCode : ""
				,operation : g_staticAll
			};
			g_menuList.push(tempObj);
		}
	});
}

/* 허가 URL 목록 등록 */
let registMenuUrl = function() {
	let thisDom = document.querySelector("#btn_registUrl");
	thisDom.removeAttribute("change-modal-id");
	
	let cnt = 0;
	document.querySelectorAll(".rdo").forEach(function(v){
		if ( v.checked ) cnt ++;
	});
	if( cnt == 0 ) {
		alertPopup("선택된 row가 없습니다.");
		return;
	}
	
	thisDom.setAttribute("change-modal-id", "registMenuUrl");
	fn_modalOpen("registMenuUrl");
	
	getAuthorityMenulist(g_selectedAuthority.authorityId);
	
	showList([],"",0);
	
	document.getElementById("url_title").innerText = "선택된 권한 : " + g_selectedAuthority.authorityName;
	document.getElementById("url_autorityId").value = g_selectedAuthority.authorityId;
	document.getElementById("url_autorityName").value = g_selectedAuthority.authorityName;
	let nowIndex = 0;
	let searchMenuList = [];
	g_menuList.forEach(function(menu){
		searchMenuList.push(menu.menuName);
	});
	let menuSearch = document.getElementById("url_menuName");
	menuSearch.focus();
	menuSearch.addEventListener("keyup", function(e) {
		// 검색어
		let value = this.value.trim();
		
		let matchDataList = [];
		// 자동완성 필터링
		matchDataList = g_menuList.filter(function(menu) {
			if( value != "" && menu.menuName.indexOf(value) > -1 ) {
				return menu;
			}
		});
		
		// 이미 있는 권한은 제거
		matchDataList.forEach(function(m, index){
			g_listAuthorityUrl.forEach(function(u){
				if( u.menuCode == m.menuCode ) {
					matchDataList.splice(index,1);
				}
			});
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
				var menuName = matchDataList[nowIndex].menuName;
				var menuCode = matchDataList[nowIndex].menuCode;
				var operation = matchDataList[nowIndex].operation;
				showListSetData(menuCode, menuName, operation);
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
	});
}

let showList = function(data, value, nowIndex) {
	let sDiv = document.querySelector(".autocomplete");
	
	 // 정규식으로 변환
	const regex = new RegExp(`(${value})`, "g");
	
	sDiv.innerHTML = data.map(
		(menu,index) => `
		<div class='${nowIndex === index ? "active" : ""}'
		onclick='showListSetData("${menu.menuCode}","${menu.menuName}","${menu.operation}")'>
			${menu.menuName.replace(regex, "<mark>$1</mark>")}
		</div>
		`
	).join("");
	if( data.length > 0 ) {
		sDiv.removeAttribute("style");
	} else {
		sDiv.setAttribute("style","display:none;");
	}
}

let showListSetData = function(menuCode, menuName, operation) {
	document.getElementById("url_menuCode").value = menuCode;
	document.getElementById("url_menuName").value = menuName;
	document.getElementById("url_operation").value = operation;
	showList([],"",0);
}

/* 권한의 허가 URL 삭제 */
let deleteMenuUrl = function() {
	let checkedIndexArr = getCheckedIndexArray();
	if( !checkedIndexArr ) return;
	confirmPopup("선택한 메뉴의 권한을 삭제하시겠습니까?");
	document.querySelector( ".js-modal-next" ).onclick = function(e){
		e.stopPropagation();

		let deleteAuthorityUrls = [];
		checkedIndexArr.forEach(function(index){
			deleteAuthorityUrls.push(g_listAuthorityUrl[index]);
		});

		$.ajax({
			url :"/authority/deleteAuthorityUrl",
			type: "DELETE",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify(deleteAuthorityUrls),
			success: function( data ){
				alertPopup( data.message );	
				if( data.status == "success" ) {
					getListAuthorityUrl(1);
				}
			}
		});	
	}
}

/* 새로운 화면권한 등록 */
let registAuthorityUrl = function() {
	let data = {
		authorityId: document.getElementById("url_autorityId").value
		,menuCode: document.getElementById("url_menuCode").value
		,operation: document.getElementById("url_operation").value
		,methods: []
	};
	
	// 모든 권한 주입
	for( var i = 0 ; i < Object.keys(g_methodNames).length; i ++ ) {
		data.methods.push(g_methodNames[i]);
	}
	
	if ( typeof data.operation != "undefined" && data.operation == g_staticAll ) {
		data.menuUpperCode = data.menuCode;
	}
	
	$.ajax({
		url :"/authority/registAuthorityUrl",
		type: "POST",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(data),
		success: function( data ){
			alertPopup( data.message );	
			if( data.status == "success" ) {
				getListAuthorityUrl(1);
			}
		}
	});	
} 

/* 허가 URL 목록 저장 */
let updateMenuUrl = function() {
	let checkedIndexArr = getCheckedIndexArray();
	if( !checkedIndexArr ) return;
	let updateAuthorityUrls = [];
	checkedIndexArr.forEach(function(index){
		let tempObj = g_listAuthorityUrl[index];
		let updateMethods = [];
		document.querySelectorAll("#authorityUrlTable > tr")[index]
		.querySelectorAll("td > input[name=methods]").forEach(function(c){
			updateMethods.push(c.checked);
		});
		tempObj.methods = updateMethods;
		updateAuthorityUrls.push(tempObj);
	});
	
	$.ajax({
		url :"/authority/updateMethods",
		type: "PUT",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(convertAuthorityUrlVO(updateAuthorityUrls)),
		success: function( data ){
			alertPopup( data.message );	
		}
	});	
}

let convertAuthorityUrlVO = function(voArr) {
	let rstArr = [];
	voArr.forEach(function(vo){
		for(var i = 0 ; i < vo.methods.length; i ++ ) {
			var rst = {
				authorityId : vo.authorityId
				,menuCode : vo.menuCode
				,method : g_methodNames[i]
				,operation : "delete"
			};
		
			if( vo.methods[i] == true ) rst.operation = "insert";
			
			rstArr.push(rst);
		}
	});
	return rstArr;
}


/* 조회~다운로드 체크박스 변화여부 체크 */
let isEqualsChecks = function(paramDom) {
	let idx = getCheckedIndexArray(paramDom);
	let checkBoxs = document.querySelectorAll("#authorityUrlTable > tr")[idx].querySelectorAll(" td > input[name=methods]");
	let updateMethods = [];
	
	for( var i = 0 ; i < checkBoxs.length; i ++ ) {
		if( checkBoxs[i].checked ) updateMethods.push(g_methodNames[i]);
	}

	if( g_listAuthorityUrl[idx].methods.sort().toString() == updateMethods.sort().toString() ) {
		return true;
	} else {
		return false;
	}
}

 /* 권한에 허가된 URL 목록 조회 */
let getListAuthorityUrl = function(pageNo) {
	if( typeof pageNo == 'undefined') pageNo = 1;
	let authorityName = document.getElementById("selected_authorityId").value;
	let authorityId = document.getElementById("selected_authorityId").getAttribute("data-value");
	let menuName = document.getElementById("search_menuName").value;
	var url = "/authority/getListAuthorityUrl?";
	if( authorityId ) url += "authorityId=" + authorityId;
	if( authorityName ) url += "&authorityName=" + authorityName;
	if( menuName ) url += "&menuName=" + menuName;
	if( pageNo ) url += "&page=" + pageNo;
	
	$.ajax({
		url: url,
		type: "get",
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			g_listAuthorityUrl = responseData.listAuthorityUrl;
			makeAuthorityUrlTable(document.getElementById("authorityUrlTable"), g_listAuthorityUrl);
			
			document.getElementById("btn_registUrl").removeAttribute("style");
			
			if( g_listAuthorityUrl.length > 0 ) {
				setDisplay(document.getElementById("btn_updateUrl"), true);
				setDisplay(document.getElementById("btn_deleteUrl"), true);
			} else {
				setDisplay(document.getElementById("btn_updateUrl"), false);
				setDisplay(document.getElementById("btn_deleteUrl"), false);
			}
			
			// makePaging :: paging_script.html 내 정의 
			// param : page Object, selector (div ID), function name
			makePaging(responseData.page2, "page_authorityUrl", "getListAuthorityUrl" );
		}
	});
}


/* 체크박스 만들기 */
function makeRadioBox( authorityId , prefix ) {
	let radio = document.createElement("td");
	radio.setAttribute("class", "list_chk_box");
	radio.setAttribute("onclick", "event.cancelBubble=true;");
	let radioBtn = document.createElement("input");
	radioBtn.setAttribute("type","radio");
	radioBtn.setAttribute("class","rdo");
	radioBtn.setAttribute("id", prefix + authorityId );
	radioBtn.setAttribute("data-id", authorityId );
	radioBtn.setAttribute("name","sel");
	radioBtn.setAttribute("onclick","selectAuthorityRadio('"+authorityId+"');");
	let label = document.createElement("label");
	label.setAttribute("for", prefix + authorityId );
	let span = document.createElement("span");
	label.appendChild(span);
	radio.appendChild(radioBtn);
	radio.appendChild(label);

	return radio;
}

/* 좌측 권한목록 선택 시 이벤트 */
let selectAuthorityRadio = function(authorityId) {
	for( var i = 0 ; i < g_listAuthority.length; i ++ ) {
		var auth = g_listAuthority[i];
		if( auth.authorityId == authorityId ) {
			g_selectedAuthority = auth;
			break;
		}
	}
	
	document.getElementById("selected_authorityId").value = g_selectedAuthority.authorityName;
	document.getElementById("selected_authorityId").setAttribute("data-value", authorityId);
	
	getListAuthorityUrl(1);
	
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
	let label = document.createElement("label");
	label.setAttribute("for", prefix + code );
	let span = document.createElement("span");
	label.appendChild(span);
	check.appendChild(checkBox);
	check.appendChild(label);

	return check;
}


/* 목록에서 체크된 index 반환 */
let getCheckedIndexArray = function(paramDom) {
    let arr = [];
	const query = 'input[name="chk"]:checked';
    let selectedElements = [];
    if( typeof paramDom == "undefined" ) {
	    selectedElements = document.querySelectorAll( query );		
	} else {
	    selectedElements.push(paramDom);
	}
    
    if( selectedElements.length == 0 ) {
		alertPopup( "목록을 선택해주세요." );
		return false;
	} else {
		selectedElements.forEach(function(checked){
			var authorityId =  checked.parentElement.parentElement.getAttribute( "data-authorityId" );
			var menuCode =  checked.parentElement.parentElement.getAttribute( "data-menuCode" );
			
			for( var i=0; i<g_listAuthorityUrl.length; i++ ) {
				if( g_listAuthorityUrl[i].menuCode == menuCode 
					&& g_listAuthorityUrl[i].authorityId == authorityId ) {
					arr.push(i);
				}
			}
		});
	}
	return arr;
}
