/**
 * Script info
 * 1. file path		: /static/js/views/adminJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 관리자 관리 스크립트 
 */

// 삭제
function goDelete(){
	confirmPopup("선택한 게시물을 삭제하시겠습니까?");
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
			alert( "삭제할 관리자를 선택해주세요." );
			return false;	
		} else {
			var arr = new Array( selectedElementsCnt );
			
			for( var i=0; i<selectedElementsCnt; i++ ) {
				arr[i] = selectedElements[i].getAttribute( "chkId" );
			}
			
			$.ajax({
				type 		: 'delete',
				url 		: '/admin/delete',
				data 		: { arr : arr },
				traditional : true,
				success		: function(data){
					if( data != null && data.status == "success" ) {
						successPopup(data.message);
					}
				}
			});
			
		}
		
	}
}

// 목록
function goList( page ){
	var srcAdminId = $("#srcAdminId").val();
	var srcAdminName = $("#srcAdminName").val();
	
	// 검색 조건이 있는 경우
	if( srcAdminId != null && srcAdminName != null ){
		location.href='/admin/list?page=' + page + '&srcAdminId=' + srcAdminId + '&srcAdminName=' + srcAdminName;
		
	} else {
		location.href='/admin/list?page=' + page;
	}	
}                   
// 목록

// 등록
function goRegistry(){
	// 필수값 : 이름, 아이디, 이메일
	
	if( $("#rAdminId").val() == null || $("#rAdminId").val() == "" ) {
		alert("아이디을(를) 입력하세요.");
		return false;
	} else if( idDuplChk ) {
		alert("아이디가 중복됩니다.");
		return false;
	} else if( $("#rName").val() == null || $("#rName").val() == "" ) {
		alert("이름을(를) 입력하세요.");
		return false;
//	} else if( $("#rTelNo").val() == null || $("#rTelNo").val() == "" ) {
//		alert("전화번호을(를) 입력하세요.");
//		return false;
	} else if( $("#rEmail").val() == null || $("#rEmail").val() == "" ) {
		alert("이메일을(를) 입력하세요.");
		return false;
	} else if( !isValidPhoneNumber($("#rTelNo").val(), $("#rTelNo")) ) {
		alert("전화번호가 올바르지 않습니다.");
		return false;
	}else if( !isValidEmail($("#rEmail").val(), $("#rEmail")) ) {
		alert("이메일의 값이 올바르지 않습니다.");
		return false;
	}
	
	
	let jsonData = {
		adminId	 	: $("#rAdminId").val(),
		name		: $("#rName").val(),
		companyName	: $("#rCompanyName").val(),
		deptName	: $("#rDeptName").val(),
		telNo		: $("#rTelNo").val(),
		email		: $("#rEmail").val(),
		auth		: 0,
		authorityId	: $("#rAuth").val()
	};
	
	$.ajax({
			url: "/admin/register",
			type: "post",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify(jsonData),
			success: function( data ){
				// 실패
				if(data.status == "fail"){
					alertPopup(data.message);
				// 성공인경우
				} else if(data.status == "success") {
					//alertPopup(data.message);
					$("#registerAdmin").css("display","none");
					$("#rAdminId").val("");
					$("#rName").val("");
					$("#rCompanyName").val("");
					$("#rDeptName").val("");
					$("#rTelNo").val("");
					$("#rEmail").val("");
					
					const popupHidden =  "<div id='adminPoparea' name='adminPoparea' class='js-open-modal btn'></div>"
					$("body").append(popupHidden);
					const appendthis =  ("<div class='change-modal-overlay js-modal-close-change'></div>");
					$("body").append(appendthis);
					$(".change-modal-overlay").fadeTo(100, 0.7);
					$('#checkInitPw').fadeIn($(this).data());
					
					$('#initPw').text(data.initPw);
					
					$(".modal-box-change").css({
					    top: ($(window).height() - $(".modal-box-change").outerHeight()) / 2,
					    left: ($(window).width() - $(".modal-box-change").outerWidth()) / 2
					});
					
				} else {
					alertPopup(data.message);
				}
			}
		})
}
// 등록

let openRegistPopup = function() {
	document.getElementById("announceContent").value = "";
	document.querySelector("#detailTitle").innerText = "공지사항 등록";
	
}
// 상세
let goDetail = function(admin){
	console.log("godetail");
	console.log(admin);
	document.querySelector("#detailTitle").innerText = "공지사항 수정";
	document.getElementById("announceContent").value = admin.announceContent;
}
// 상세

/* 모달 팝업 */
$(document).on("click", ".js-modal-next", function(e) {
	e.stopPropagation();
	$(".modal-box, .modal-overlay").fadeOut(function() {
		$(".modal-overlay").remove();
		$("#confirmpopup").remove();
		$("#confirmpopuparea").remove();
	});
	$(window).resize();
//	location.href = "/manager/delete?managerIds=" + $("#managerId").val();
});


$(document).ready(function() {
	// 모달 팝업 open
	fn_modalOpen("announceDetail");
	
     // modal popup close
     $(document).on("click", ".js-modal-close-change", function (e) {
		console.log("close popup");
     });
	
});
 
 
  // 유효 사용자 확인
let chgServiceSel = function (res){
	// 값 지웠을때
	if ( $("#rAdminId").val().length == 0 ){
		$("#rAdminId").removeAttr("style");
    // 유효한 사용자일때
	} else if( res ) {
		$("#rAdminId").css("background-color","rgba(245, 0, 0, 0.22)");
	// 유효하지 않은 사용자
	} else {
		$("#rAdminId").css("background-color","rgba(0, 245, 75, 0.22)");
	}
}

/* 어드민 목록 조회 */
let getAdminList = function(rows) {
	var result = [];
	var paramData = {
		rows : 9999	// limit 제한 없이 조회하기 위한 변수 입력
	};
	
	if( typeof rows != "undefined" ) {
		paramData.rows = rows;
	}
	
	$.ajax({
		url: "/admin/admin-list",
		type: "get",
		data: paramData,
		async: false,
		contentType: "application/json; charset=UTF-8",
		success: function( rst ){
			result = rst.list;
		}
	});
	
	return result;	
}

