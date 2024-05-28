/**
 * Script info
 * 1. file path		: /static/js/views/userJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 사용자 관리 스크립트 
 */

var deleteUrl = "";
let idDuplChk = false;	// ID 중복체크 전역변수
let g_userList = [];
/* 삭제 */
function goDelete(){
	confirmPopup("선택한 사용자를 삭제하시겠습니까?");
	document.querySelector( ".js-modal-next" ).onclick = function(e){
		e.stopPropagation();
		$(".modal-box, .modal-overlay").fadeOut(function () {
			$(".modal-overlay").remove();
		       $("#confirmpopup").remove();
		       $("#confirmpopuparea").remove();
		});
		$(window).resize();
		
		const query = 'input[name="chk"]:checked';
	    const selectedElements = document.querySelectorAll( query );
	    
	    // 체크박스 체크된 항목의 개수
	    const selectedElementsCnt = selectedElements.length;
	    
	    if( selectedElementsCnt == 0 ) {
			alertPopup( "삭제할 사용자를 선택해주세요." );
			return false;	
		} else {
			var arr = new Array( selectedElementsCnt );
			
			for( var i=0; i<selectedElementsCnt; i++ ) {
				arr[i] = selectedElements[i].getAttribute( "userId" );
			}
			
			$.ajax({
				type 		: 'delete',
				url 		: '/user/delete',
				data 		: { arr : arr },
				traditional : true,
				success		: function(data){
					if(data.status == "success"){
						successPopup(data.message);
					} else {
						alertPopup(data.message);
					}
				}
			});
			
		}
		
	}
}

/* 목록 */
function goList(page) {
	let srcUserId = document.querySelector("#srcUserId").value;
	let uri = '/user/list?page=' + page ;
	if ( srcUserId != null && srcUserId != "" ) {
		uri += "&srcUserId=" + srcUserId; 
	} 
	
	location.href= uri;
}

/* 검색 조건 유효성 검사 */
function check(){
	if( $( "#srcUserId" ).val() == "" && $( "#srcUserName" ).val() == "" ){
		alertPopup( "검색 조건을 입력하세요." );
		return false;
	} 
}
/* 상태 변경 */
function goUpdateSt(){
	confirmPopup("선택한 사용자의 상태를 변경하시겠습니까?");
			
	document.querySelector( ".js-modal-next" ).onclick = function(e){
		e.stopPropagation();
		$(".modal-box, .modal-overlay").fadeOut(function () {
			$(".modal-overlay").remove();
		       $("#confirmpopup").remove();
		       $("#confirmpopuparea").remove();
		});
		
		$(window).resize();
		
		const query = 'input[name="chk"]:checked';
	    const selectedElements = document.querySelectorAll( query );
	    
	    // 체크박스 체크된 항목의 개수
	    const selectedElementsCnt = selectedElements.length;
	    
	    if( selectedElementsCnt == 0 ) {
			alertPopup( "사용자를 선택해주세요." );
			return false;	
		} else {
			
			var arr = new Array( selectedElementsCnt );
			
			for( var i=0; i<selectedElementsCnt; i++ ) {
				if(selectedElements[i].getAttribute( "userState" ) != "잠김"){
					alertPopup( "'잠김'상태인 사용자의 상태만 변경할 수 있습니다." );
					return false;
				}
				
				arr[i] = selectedElements[i].getAttribute( "userId" );
			}
			
			$.ajax({
				type 		: 'put',
				url 		: '/user/updateSt',
				data 		: { arr : arr },
				traditional : true,
				success		: function(data){
					if(data.status == "success"){
						successPopup(data.message);
					} else {
						alertPopup(data.message);
					}
				}
			});
			
		}
		
	}
}

/* 수정 */
function goUpdate() {
	var newpw = $("#detUserNewPw").val();
	var newpwChk = $("#detUserNewPwChk").val();
	
	
	// validations start
	if( newpw != null && newpw != newpwChk ) {
		alertPopup("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		return false;
	}
	
	if(!checkPassword(newpw)){
		alert("비밀번호는 숫자, 영문 대소문자, 특수문자의 조합(2가지 이상) 8~20자로 입력하세요.");
		return false;
	}
	
	let requiredValues = { detUserName:"이름", detUserLv:"직급", detUserDept:"부서", detUserEmail:"이메일" };
	
	for( var key in requiredValues ) {
		var dom = document.getElementById(key);
		var val = "";
		if( dom != null ) val = dom.value;
		
		if( val.trim() == "" ) {
			alertPopup(requiredValues[key] + "을(를) 입력해주세요.");
			return false;
		}
	}
	
	if ( !isValidEmail(document.querySelector("#detUserEmail").value, document.querySelector("#detUserEmail")) ) {
		alertPopup("이메일 형식이 올바르지 않습니다.");
		return false;
	}
	
	//validations end
	
	let reqData = {
		userId	 	: $("#detUserId").val(),
		userName	: $("#detUserName").val(),
		pw			: newpw,
		jobLevel	: $("#detUserLv").val(),
		departTeam	: $("#detUserDept").val(),
		email		: $("#detUserEmail").val()
	};
	
	$.ajax({
		url: "/user/update",
		type: "put",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(reqData),
		success: function(result){
			// 수정 성공
			if(result.status == "success"){
				alertPopup("수정에 성공했습니다.");
				$("#updateUser").css("display","none");
				$("#detUserId").val("");
				$("#detUserName").val("");
				$("#detUserEmail").val("");
				$("#detUserNewPw").val("");
				$("#detUserNewPwChk").val("");
				$("#detUserLv").val("");
				$("#detUserDept").val("");
				$("#detServerName").val("");
				$("#detServerCode").val("");
				$("#detUserState").val("");
				$("#detUserAuth").val("");
				successPopup("수정에 성공했습니다.");
			// 수정 실패
			} else {
				alertPopup(result.message);
			}
			
		}
	});
}

function changeSelect() {	// css 때문에 동작X
	var serviceSelect = document.getElementById("regServiceName");
	var sc = serviceSelect.options[serviceSelect.selectedIndex];
	$("#regScCode").val(sc.getAttribute("scCode"));
	$("#regSvName").val(sc.getAttribute("svName"));
	$("#regSvCode").val(sc.getAttribute("svCode"));
}

/* 등록 */
function goRegistry(){
	let reqData = {
		userId	 	: $("#regUserId").val(),
		userName	: $("#regUserName").val(),
		pw			: $("#regPw").val(),
		jobLevel	: $("#regJobLevel").val(),
		departTeam	: $("#regDepartTeam").val(),
		svCode		: $("#regSvCode").val(),
		email		: $("#regEmail").val()
	};
	
	if( !reqData.userId || reqData.userId == "" ) {
		alertPopup("ID를 입력하세요.");
		return false;
	} else if( !reqData.userName || reqData.userName == "" ) {
		alertPopup("이름을 입력하세요.");
		return false;
	} else if( !reqData.pw || reqData.pw == "" ) {
		alertPopup("비밀번호를 입력하세요.");
		return false;
	} else if ( !passwordsEqual(reqData.pw, document.querySelector("#regPwChk").value) ) {
		alertPopup("두 패스워드가 일치하지 않습니다.");
		return false;
	} else if( !reqData.jobLevel || reqData.jobLevel == "" ) {
		alertPopup("직급을 입력하세요.");
		return false;
	} else if( !reqData.departTeam || reqData.departTeam == "" ) {
		alertPopup("부서를 입력하세요.");
		return false;
	} else if ( idDuplChk ) {
		alertPopup("ID가 중복됩니다.");
		return false;
	} else if ( !isValidEmail(reqData.email, document.querySelector("#regEmail")) ) {
		alertPopup("이메일 형식이 올바르지 않습니다.");
		return false;
	}

	if(!checkPassword(reqData.pw)){
		alert("비밀번호는 숫자, 영문 대소문자, 특수문자의 조합(2가지 이상) 8~20자로 입력하세요.");
		return false;
	}
		
	$.ajax({
			url: "/user/register",
			type: "post",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify(reqData),
			success: function(result){
			// 성공
			if(result.status == "success"){
					$("#registerUser").css("display","none");
					$("#regUserId").val("");
					$("#regUserName").val("");
					$("#regPw").val("");
					$("#regJobLevel").val("");
					$("#regDepartTeam").val("");
					$("#regSvCode").val("");
					$("#regEmail").val("");
					
					successPopup("사용자가 등록되었습니다.");
				// 수정 실패
				} else {
					alertPopup(result.message);
				}
			}
//			,error: function(err) {
//				if( err.responseJSON.message.indexOf("Message Validation Error : ") > -1 ) {
//					alertPopup( err.responseJSON.message ); 
//				}
//				
//				if( err.responseJSON.message.indexOf("server_info is invalid.") > -1 ) {
//					alertPopup( "서비스명 정보가 올바르지 않습니다." ) ;
//					return fasle;
//				}
//			}
			
		});
}

/* 상세 조회 팝업*/
function goDetail(user){
	$("#detUserId").val(user.userId);
	$("#detUserName").val(user.userName);
	$("#detUserLv").val(user.jobLevel);
	$("#detUserDept").val(user.departTeam);
	$("#detServerName").val(user.servInfoName);
	$("#detServerCode").val(user.servInfoSeqId);
	$("#detUserState").val(user.stateNm);
	$("#detUserAuth").val(user.authNm);
	$("#detUserEmail").val(user.email);
}

/* 모달 팝업 */
$(document).on("click", ".js-modal-next", function(e) {
	e.stopPropagation();
	$(".modal-box, .modal-overlay").fadeOut(function() {
		$(".modal-overlay").remove();
		$("#confirmpopup").remove();
		$("#confirmpopuparea").remove();
	});
	$(window).resize();
});


$(document).ready(function() {
	// 모달 팝업 open
	fn_modalOpen("userPoparea");
	
	document.querySelector("a[change-modal-id=registerUser]").addEventListener("click",function(){
		if( g_userList.length == 0 ) {
			g_userList = getUserList();
		}
	});
	
	document.querySelector("#regUserId").addEventListener("keyup", function(e) {
		var res = false;
		idDuplChk = false;
		for(var i = 0 ; i < g_userList.length; i ++ ) {
			if( g_userList[i].userId == this.value ) {
				res = true;
				idDuplChk = true;
				break;
			}
		}
		chgServiceSel(res);
	});
	
	document.querySelectorAll("#regEmail, #detUserEmail").forEach(function(dom){
		dom.addEventListener("keyup", function() {
			isValidEmail(this.value, this);
		});
	});
	
     // modal popup - close
     $(document).on("click", ".js-modal-close-change", function (e) {
         e.stopPropagation();
         $("#registerUser").css("display","none");
         $("#updateUser").css("display","none");
         $("userPoparea").remove();
         
         // 등록
         $("#reg_user_id").val("");
         $("#reg_name").val("");
         $("#reg_company_name").val("");
         $("#reg_dept_name").val("");
         $("#reg_tel_no").val("");
         $("#reg_e_mail").val("");
         $("#reg_auth").val("");
         
         // 수정
     });
     
	

 });
 
 
 // 유효 사용자 확인
let chgServiceSel = function (res){
	// 값 지웠을때
	if ( $("#regUserId").val().length == 0 ){
		$("#regUserId").removeAttr("style");
    // 유효한 사용자일때
	} else if( res ) {
		$("#regUserId").css("background-color","rgba(245, 0, 0, 0.22)");
	// 유효하지 않은 사용자
	} else {
		$("#regUserId").css("background-color","rgba(0, 245, 75, 0.22)");
	}
}

