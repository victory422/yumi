/**
 * Script info
 * 1. file path		: /static/js/views/commonJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 공통 스크립트 
 */

/* 비밀번호 유효성 체크 */
function checkPassword(password){
	// true: 규칙O, false: 규칙X
	var result = true;
	var pattern = /^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\d~!@#$%^&*()_+=]{8,20}$/;
	
	if(!password.match(pattern)){
		result = false;
	}
	return result;
}

/* 체크박스 전체 선택 클릭 이벤트 */
function allChecked(documentId){
	if ( typeof documentId == 'undefined' ) {
		documentId = 'allCheckBox';
	}
	// 전체 체크박스 버튼
	const allCheckBox = document.getElementById( documentId );
	
	// 전체 체크박스 버튼 체크 여부
	const is_checked = allCheckBox.checked;
	
	// 전체 체크박스 제외한 모든 체크박스
	if( is_checked ) {
		// 체크박스 전체 체크
		document.querySelectorAll( ".chk" ).forEach( function( v, i ){
			v.checked = true;
		});
	} else {
		// 체크박스 전체 해제
		document.querySelectorAll( ".chk" ).forEach( function( v, i ){
			v.checked = false;
		});
	}
}

/* 자식 체크박스 클릭 이벤트 */
function chkClicked() {
	
	// 체크박스 전체 개수
	const allCount = document.querySelectorAll( ".chk" ).length;
	
	// 체크된 체크박스 전체 개수
	const query = 'input[name="chk"]:checked';
	const selectedElements = document.querySelectorAll( query );
	const selectedElementsCnt = selectedElements.length;
	
	const allCheckbox = document.getElementById( 'allCheckBox' );
	
	// 체크박스 전체 개수 == 체크된 체크박스 전체 개수 > 전체 체크박스 체크
	if( allCount == selectedElementsCnt ){
		allCheckbox.checked = true; 
	// 체크박스 전체 개수 != 체크된 체크박스 전체 개수 > 전체 체크박스 해제
	} else {
		allCheckbox.checked = false;
	}
}

/* 로그아웃 변수 */
let isLogout = false;

/* 로그아웃 */
function logout(){
	confirmPopup("로그아웃 하시겠습니까?");
	isLogout = true;
	
	if(isLogout){
		document.querySelector( ".js-modal-next" ).onclick = function(e){
			e.stopPropagation();
			$(".modal-box, .modal-overlay").fadeOut(function () {
				$(".modal-overlay").remove();
			       $("#confirmpopup").remove();
			       $("#confirmpopuparea").remove();
			});
			$(window).resize();
			alert( "로그아웃 되었습니다." );
			location.href = '/logout';
		}
	}
}

/* 내 정보 조회 */
function getMyInfo(){
	$.ajax({
		url: "/common/myInfo",
		type: "get",
		contentType: "application/json; charset=UTF-8",
		success: function( rst ){
			if( rst.status == "success" ) {
				var data = rst.resultMap;
				$("#myInfoId").val(data.adminId);
				$("#myInfoName").val(data.name);
				$("#myInfoEmail").val(data.email);
				$("#myInfoCompanyName").val(data.companyName);
				$("#myInfoDeptName").val(data.deptName);
				$("#myInfoTelNo").val(data.telNo);
				
				$("#myInfoRegDate").text(setDateFormat(data.regDate));
				$("#myInfoLastDate").text(setDateFormat(data.lastDate));
				$("#myInfoAuth").text(data.authVal);
				$("#myInfoAuthScreen").text(rst.listAuthorityName.join(", "));
			} else if( data.status == "fail" ) {
				alertPopup(data.message);
			}
			
		}
	});
}

/* 내 정보 수정 */
let goUpdateMyInfo = function(){

	let jsonData ={
		adminId	 	: $("#myInfoId").val(),
		name		: $("#myInfoName").val(),
		companyName	: $("#myInfoCompanyName").val(),
		deptName	: $("#myInfoDeptName").val(),
		telNo		: $("#myInfoTelNo").val(),
		email		: $("#myInfoEmail").val(),
		password	: encodePassword($("#myInfoId").val(), $("#myInfoPassword").val(), getLoginInfo().sessionId)
	}
	
	$.ajax({
		url :"/common/updateMyInfo",
		type: "put",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(jsonData),
		success: function( data ){
			if( data.status == "success" ){
				successPopup( "수정이 완료되었습니다." );	
			} else if( !data.message ) {
				//location.href = '/login';
				alertPopup( data.message );
			} else {
				alertPopup( data.message );
			}
		}
	});
}

/* 내 비밀번호 변경 */
let goUpdateMyPw = function() {
	let loginInfo = getLoginInfo();
	let loginId = loginInfo.loginId;
	let sessionId = loginInfo.sessionId;
	let password = document.getElementById("password").value;
	// 유효성검사
	if( $("#password").val() == "" ){
		alert( "현재 비밀번호를 입력하세요." );
		return;	
	}
	if( $("#newPw").val() == "" ){
		alert( "새 비밀번호를 입력하세요." );
		return;	
	}
	if( $("#pwChk").val() == "" ){
		alert( "비밀번호 확인을 입력하세요." );
		return;	
	}
	if( $("#newPw").val() != $("#pwChk").val() ){
		alert( "비밀번호 확인값이 일치하지 않습니다." );
		return;	
	}
	if(!checkPassword($("#newPw").val())){
		alert("비밀번호는 숫자, 영문 대소문자, 특수문자의 조합(2가지 이상) 8~20자로 입력하세요.");
		return false;
	}
	
	let jsonData ={
		password 	: encodePassword(loginId, password, sessionId)
		,newPw		: encodePasswordToDB(loginId, $("#newPw").val())
	}
	
	$.ajax({
		url :"/common/updateMyPw",
		type: "put",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(jsonData),
		success: function( data ){
			if( data.status == "success" ){
				alert( "비밀번호가 변경되었습니다. 재로그인해주시기 바랍니다." );	
				location.href = data.redirectUrl;
			} else {
				alert( data.message );
			}
		}
	});
}

/* 날짜 포맷 설정 */
function setDateFormat(date){
	var d = new Date(date);
	var month = ((d.getMonth() + 1) < 10) ? "0" + (d.getMonth() + 1) : (d.getMonth() + 1);
	var day = (d.getDate() < 10) ? "0" + d.getDate() : d.getDate();
	var hour = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();
	var min = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
	var sec = (d.getSeconds() < 10) ? "0" + d.getSeconds() : d.getSeconds();
	
	var result = d.getFullYear() + "." + month + "." + day + " " + hour + ":" + min + ":" + sec;
	if( result.indexOf("NaN") > -1 ) result = ""; 
	return result;
}

$(document).ready(function() {
     // modal popup
     $(document).on("click", "a[change-modal-id]", function(){
         const popupHidden =  "<div id='myInfoPoparea' name='myInfoPoparea' class='js-open-modal btn'></div>"
         $("body").append(popupHidden);
         const appendthis =  ("<div class='change-modal-overlay js-modal-close-change'></div>");
         //e.preventDefault();
         $("body").append(appendthis);
         $(".change-modal-overlay").fadeTo(100, 0.7);
         const modalBox = $(this).attr('change-modal-id');
         $('#'+ modalBox).fadeIn($(this).data());
         $(".modal-box-change").css({
             top: ($(window).height() - $(".modal-box-change").outerHeight()) / 2,
             left: ($(window).width() - $(".modal-box-change").outerWidth()) / 2
         });
     });
     
     // modal popup - close
     $( document ).on( "click", ".js-modal-close-change", function ( e ) {
         e.stopPropagation();
         $( "#getMyInfo" ).css( "display", "none" );		// 등록 팝업
         $( "#updateMyPw" ).css( "display", "none" );		// 등록 팝업
         $( "myInfoPoparea" ).remove();
         
         $( "#myInfoId" ).val( "" );
         $( "#myInfoName" ).val( "" );
         $( "#myInfoCompanyName" ).val( "" );
         $( "#myInfoDeptName" ).val( "" );
         $( "#myInfoTelNo" ).val( "" );
         $( "#myInfoEmail" ).val( "" );
         $( "#myInfoPassword" ).val( "" );
         
         $( "#pw" ).val( "" );
         $( "#newPw" ).val( "" );
         $( "#pwChk" ).val( "" );
         
         $(".js-modal-close-change.change-modal-overlay").remove();
		 $("#myInfoPoparea").remove();
     });
     
     
     $(".btn-calender").on("click", function (event) {
        $(".S_date_month, .E_date_month").datepicker({
            onClose: function () {
                $(".S_date_month, .E_date_month").datepicker("destroy");
            },
            onSelect: function(e) {
				if( e != '' ) {
					let iptDate = document.querySelectorAll("form .sch_tbl_input_date input");
					if( iptDate.length == 2 ) {
						if( this.getAttribute("class").indexOf("E_date_month") > -1 ) {
							checkParamDate(iptDate, 1);		
						} else {
							checkParamDate(iptDate, 0);
						}
					}
				}
			}
        });
        $(this).prev().focus();
    });
    
    // 메뉴 toggle
    $('.nav-toggle').click(function(e) {
        e.preventDefault();
        $('#container').toggleClass('closeNav');
        $('.nav-toggle').toggleClass('active');
    });
    
    // 페이지 로드후 pathname 저장
    // /를 기준으로 나눠 배열로 저장
    // 그중 첫번째 값에따라서 해당 메뉴에 "on"class 부여
    // 페이지 추가시에 if 조건추가 필요
    if ( document.querySelectorAll("#lnb > ul > li").length > 0 && document.querySelectorAll("#lnb > ul > li")[0].id != "" ) {
	    var link =  window.location.pathname;
	    let str = link.split("/");
	    str.shift()
	    if(str != []){
	        //  관리자 관리
	        if(str[0]==="admin"){
	            $("#M020").addClass("on")
	        }
	        // 암호키 관리
	        if(str[0] === "policy" || str[0]=== "symmetrickey" || str[0] === "keypair" || str[0] === "splitkey" || str[0] === "cert" ){
	            $("#M030").addClass("on")
	        }
	        // 감사로그
	        if(str[0] === "audit"){
	            $("#M040").addClass("on")
	        }
	        // 통계 추가
	        
	        // 환경설정
	        if(str[0] === "service" || str[0]=== "user"){
	            $("#M060").addClass("on")
	        }
	    }
	}
        
    // 세션체크 inteval 실행
    setTimeOffsetBetweenServerAndClient();
    fn_sessionCheckInterval();
    
    
    /* 검색어, 조회버튼 매핑 */
    makeSearchKeyEvent();
    
});	// document.ready END

function sleep(ms) {
    const wakeUpTime = Date.now() + ms;
    while (Date.now() < wakeUpTime) {}
}

/* 셀렉트박스 동적 선택*/
function setSelectBoxValue(id, val, option) {
	let elemts = document.getElementById(id);
	let ops = elemts.previousElementSibling.querySelectorAll("div .option");
	
	if( typeof option == 'undefined' ) option = ""; 
	
	for(var i = 0 ; i < ops.length ; i++ ) {
		var innerVal = val + option;
		if( ops[i].getAttribute("data-value") == innerVal ) {
			ops[i].click();
		}
	}
	elemts.previousElementSibling.setAttribute("class","styled-select");
}


function nullCheck(val) {
	if( val == null || val == "undefined" ) {
		return "";
	} else {
		return val;
	}
}


let addMonthDate = function (date, n){
	return new Date(date.setMonth(date.getMonth() + n ));
}
			

let _formatDate = function(date) {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based
  const day = date.getDate().toString().padStart(2, '0');

  return year + "-" + month+ "-" +day;
}


// 활성화상태 아닌 키 폐기할 수 없도록 수정
let checkState = function(doms) {
	let boolean = false;
	
	for( var i = 0 ; i < doms.length; i++ ) {
		var d = doms[i];
		if ( d.getAttribute("chkval").toString() != "2" ) {
			boolean = true;
			break;
		}
	}
	
	if( boolean ) {
		alertPopup("활성화상태가 아닌 키는 폐기(삭제)할 수 없습니다.");
		return true;
	} else {
		return false;
	}
}

// 패스워드 파라미터 암호화로직
function encodePassword(id, pw, token ) {
	if( id != null && id != "" ) {
		id = id.trim();
	}
	var hashedPw = CryptoJS.SHA256( pw );
	
	var encHex = CryptoJS.enc.Hex.parse( hashedPw + String2Hex( id ) );
	
	var sha256pw = 	CryptoJS.SHA256( encHex ) ;
	
	return Base64.encode( CryptoJS.SHA256( token + sha256pw ).toString() );
}


function encodePasswordToDB(id, pw) {

	var hashedPw = CryptoJS.SHA256( pw );
	
	var encHex = CryptoJS.enc.Hex.parse( hashedPw + String2Hex( id ) );
	
	var sha256pw = 	CryptoJS.SHA256( encHex ) ;
	
	return sha256pw.toString();}


//쿠키 저장하는 함수
function set_cookie(name, value, unixTime) {
    var date = new Date();
    date.setTime(date.getTime() + unixTime);
    document.cookie = encodeURIComponent(name) + '=' + encodeURIComponent(value) + ';expires=' + date.toUTCString() + ';path=/';
}

//쿠키 값 가져오는 함수
function get_cookie(name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

//쿠키 삭제하는 함수
function delete_cookie(name) {
    document.cookie = encodeURIComponent(name) + '=; expires=Thu, 01 JAN 1999 00:00:10 GMT';
}


/* 사용자 세션체크 interval 설정*/
function fn_sessionCheckInterval() {
	let sec = 5;	// 반복할 시간 설정
	let isExpired = isSessionExpired(-5*1000);	//세션만료예정시간을 5초 앞당겨서 검사
	
	let exceptionUrl = [
		"/login"
		,"/common/invalidSession"
		,"/common/initPw"
		,"/loginForm"
	];
	
	let exception = false;
	if( exceptionUrl.indexOf(location.pathname) > -1 ) {
		exception = true;
	}
	
	if(isExpired === true && !exception ){
		delete_cookie("sessionExpiry");
		delete_cookie("clientTimeOffset");
		delete_cookie("latestTouch");
		
		alert('세션이 만료되었습니다. 다시 로그인해주세요.');
		
		location.href = "/logout";
		
	}else{
		setTimeout('fn_sessionCheckInterval()', sec * 1000);	//티이머 반복
	}
}

/* 사용자 세션체크 */
function setTimeOffsetBetweenServerAndClient() {
	let latestTouch = get_cookie('latestTouch');
	latestTouch = latestTouch==null ? null : Math.abs(latestTouch);
	let clientTime = (new Date()).getTime();
	let clientTimeOffset = clientTime - latestTouch;
	set_cookie('clientTimeOffset', clientTimeOffset);
}

/* 사용자 세션체크 */
function isSessionExpired(offset) {
	let sessionExpiry = Math.abs(get_cookie('sessionExpiry'));
	let timeOffset = Math.abs(get_cookie('clientTimeOffset'));
	let localTime = (new Date()).getTime();
	set_cookie('remainTime', (sessionExpiry - (localTime - timeOffset)));
//	console.log(localTime - timeOffset, " > " , sessionExpiry-(offset||0));
	return (localTime - timeOffset) > (sessionExpiry-(offset||0));
}



// loginId, userName, sessionId(JSESSIONID) 가져오는 로직. 동기화처리되어있다.
let getLoginInfo = function() {
	let loginInfo = {};
	$.ajax({
		url: "/common/getLoginInfo",
		type: "get",
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( data ){
			loginInfo = {
				sessionId : data.sessionId
				,loginId : data.loginId
				,userName : data.userName
			} 
		}
	});
	
	return loginInfo;
}


/* 메뉴 목록 조회 */
let getMenuList = function() {
	let menus = [];
	$.ajax({
		url: "/common/getMenuList",
		type: "get",
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( data ){
			menus = data;
		}
	});
	
	return menus;
}


/* 현재 화면 권한 조회 */
let getScreenAuthorityList = function() {
	var link =  window.location.pathname;
	var thisMenuCode = "";
	global_menuArray.forEach(function(menu){
		if( link === menu.menuUrl ) {
			thisMenuCode = menu.menuCode;
		}
	});
	if (thisMenuCode == "" ) thisMenuCode = "dashboard";
	let authList = [];
	$.ajax({
		url: "/common/getScreenAuthorityList/" + thisMenuCode,
		type: "get",
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( data ){
			authList = data;
		}
	});
	
	return authList;
}

/* 화면권한에 따른 버튼 제어 */
let authorityButtonsController = function() {
	let btnNames = {
		"등록" : "POST",
		"변경" : "PUT",
		"해제" : "PUT",
		"삭제" : "DELETE",
		"생성" : "POST",
		"수정" : "PUT",
		"저장" : "POST",
		"설정" : "PUT",
		"다운로드" : "DOWNLOAD"
	}
	
	let btnArr = document.querySelectorAll(".btn_area_top > a, .pop_btn_one");
	let btnNamesArr = [];
	btnArr.forEach(function(btn) {
		btnNamesArr.push(btn.firstElementChild.innerText);
		var btnType = btn.getAttribute("button-type");
		if ( btnType == null || btnType == "" ) {
			for( key in btnNames ) {
				if( btn.firstElementChild.innerText.indexOf(key) > -1 ) {
					btnType = btnNames[key];
					btn.setAttribute("button-type", btnType);
				}
			}
		}
		
		// global_authorityList 갖은 권한에 없으면 버튼 안보임
		if( !(global_authorityList.indexOf(btnType) > -1) ) {
			btn.setAttribute("style","display:none;");
		} else {
			btn.removeAttribute("style");
		}
	});
//	console.log(btnNamesArr.join("\t"));
}

/* button display 동적 제어 - 제어 후 권한 설정을 호출하기 위함 */
let setDisplay = function(dom, bool, type) {
	if( typeof type == "undefined" ) {
		if( !bool ) {
			type = "none;";
		} else {
			type = "inline-block;";
		}
	}
	dom.setAttribute("style","display:"+type);
	
	authorityButtonsController();
}


/* 검색어, 조회버튼 매핑 */
let makeSearchKeyEvent = function() {
	// 검색 eky 이벤트
	let searchWrap = document.querySelectorAll(".searchWrap");
	searchWrap.forEach(function(wrap){
		let searchBtn = wrap.querySelector("div.btn_area > button");
		let inputs = wrap.querySelectorAll("input[type=text]");
		inputs.forEach(function(d) {
			if( d.getAttribute("readonly") == null ) {
				d.addEventListener("keyup",function(e){
					// enter
					if ( e.keyCode == 13 ) {
						searchBtn.click();
					} 
				});
			}
		});
	});
}

/* 공통코드 호출 */
let getCodeDetail = function(val1, val2) {
	let rst = [];
	let url = "/common/getCodeDetail?";
	if( val1 ) url += "srcMasterCode=" + val1;
	if( val2 ) url += "&srcCode=" + val2;
	
	$.ajax({
		url: url,
		type: "get",
		async:false,
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			rst = responseData;
		}
	});
	
	return rst;
}

/* matchingCodeDetailToComponent(options) 컴포넌트와 공통코드 매칭 
options :: EX )
	var options_mode = {
		id: "regPolChgSel"
		,name: "regPolChgSel"
		,class: "styled"					// option
		,masterCode : "BLOCK_CIPHER_MODE"
		,code : "0"							// option :: 단건 조회할 때 사용
		,valueCode : "description"	// option :: <option value="">에 필요한 값이 description인지, value01인지 등에 쓰임
		,sufix : "L"						// option :: <option value="">에 추가로 코드를 붙일 때 사용 
		,all: false							// option :: <option value="all"> 사용여부
		,style: "width:290px;"
	};
*/
let matchingCodeDetailToComponent = function( options ) {
	
	if( typeof options == 'undefined' ) return;

	let selectBox = document.querySelector("#"+options.id);
	if( selectBox == null ) return;
	
	if( typeof options.class == "undefined" ||  options.class == "" ) options.class = "styled";
	
	if ( isNull(options.all) ) options.all = false;
	
	if( !options.all ) {
		while ( selectBox.hasChildNodes() ) {
			selectBox.removeChild(selectBox.firstChild);
		}
	} else {
		if( selectBox.querySelector("option[value=all]") == null ) {
			var op = document.createElement("option");
			op.setAttribute("value", "all");
			op.setAttribute("class", "selectBox");
			op.innerText = "전체";
			selectBox.appendChild(op);
		}
	}
	
	let keys = Object.keys(options);
	
	for( var i = 0 ; i < keys.length; i ++ ) {
		if( keys[i] != "data" ) selectBox.setAttribute(keys[i], options[keys[i]]);
	}

	let dataArr = getCodeDetail(options.masterCode, options.code);
	
	for(var i = 0 ; i < dataArr.length ; i++ ) {
		let desc = dataArr[i].description;
		let value01 = dataArr[i].value01;
		
		if( options.valueCode != null && typeof options.valueCode != "undefined" ) {
			value01 = dataArr[i][options.valueCode];
		}
		
		if( options.sufix != null && typeof options.sufix != "undefined" ) {
			value01 = value01 + options.sufix;
		}
		
		
		let opt = document.createElement("option");
		if( value01 == null || typeof value01 == "undefined" ) {
			opt.setAttribute("value", desc);
		} else {
			opt.setAttribute("value", value01);
		}
		opt.innerText = desc;
		selectBox.appendChild(opt);
	}
	
	selectBox.previousElementSibling.remove();
	$("select#"+ options.id).styledSelect();

	if( selectBox.querySelector("option") != null ) {
		selectBox.querySelector("option").click();
	}
	
}


// UTF-8 방식 byte 구하는 코드 (3byte)
let getByteLengthOfString = function(s,b,i,c){
    for(b=i=0;c=s.charCodeAt(i++);b+=c>>11?3:c>>7?2:1);
    return b;
};

// byte 구하여 target에 입력 (키 입력 시 사용되고 있음)
let setByteLengthOfString = function(s,target){
	var dom = target;
	if( typeof target == "string") {
		dom = document.getElementById(target); 
	}
	if( dom.tagName == "input" ) {
		dom.value = getByteLengthOfString(s);
	} else {
		dom.innerText = getByteLengthOfString(s);	
	}
    
};

/* 사용자 목록 조회 */
let getUserList = function(rows) {
	var result = [];
	var paramData = {
		rows : 9999	// limit 제한 없이 조회하기 위한 변수 입력
	};
	
	if( typeof rows != "undefined" ) {
		paramData.rows = rows;
	}
	
	$.ajax({
		url: "/common/user-list",
		type: "get",
		data: paramData,
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( rst ){
			if( rst.status == "success"  ) {
				result = rst.resultMap;
			} else {
				alertPopup(rst.message);
			}
		}
	});
	
	return result;	
}

/* 사용자-서비스 목록 조회 */
let getServiceByServerInfoSeqId = function(servInfoSeqId) {
	var result = [];
	
	if( typeof rows != "undefined" ) {
		paramData.rows = rows;
	}
	
	$.ajax({
		url: "/common/service-by-server-info?servInfoSeqId=" + servInfoSeqId,
		type: "get",
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( rst ){
			if( rst.status == "success" ) {
				result = rst.resultMap;
			} else {
				alertPopup(rst.message);
			}
		}
	});
	
	return result;	
}

// 한글 입력 체크하는 코드
// 사용 예 :: <input type="text" onkeyup="chkCharCode(event)" /> 
let chkCharCode = function (event) {
	const regExp = /[^0-9a-zA-Z-\-\_]/g;
	const ele = event.target;
	ele.addEventListener("blur", function(e){
		chkCharCode(e);
	});
	if (regExp.test(ele.value)) {
		ele.value = ele.value.replace(regExp, '');
		toastOn("해당 컴포넌트에는 영문,숫자만 입력할 수 없습니다.");
	}
};

let chkCharCodeForEmail = function (event) {
	const regExp = /[^0-9a-zA-Z-\-\_\@\.]/g;
	const ele = event.target;
	ele.addEventListener("blur", function(e){
		chkCharCodeForEmail(e);
	});
	if (regExp.test(ele.value)) {
		ele.value = ele.value.replace(regExp, '');
		toastOn("해당 컴포넌트에는 영문,숫자만 입력할 수 없습니다.");
	}
};


//1. 토스트 메시지, 버튼요소를 변수에 대입
//2. 토스트 메시지 노출-사라짐 함수 작성
function toastOn(message){
	let toastMessage = document.getElementById('toast_message');
	if( toastMessage == null ) {
		let div = document.createElement("div");
		div.setAttribute("id","toast_message");
		document.body.appendChild(div);
		toastMessage = document.getElementById('toast_message');
	}
	
	toastMessage.innerText = message;
    toastMessage.classList.add('active');
    setTimeout(function(){
        toastMessage.classList.remove('active');
    },1000);
}