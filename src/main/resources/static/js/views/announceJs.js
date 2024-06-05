/**
 * Script info
 * 1. file path		: /static/js/views/adminJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 관리자 관리 스크립트 
 */

// 삭제
function goDelete() {
	confirmPopup("선택한 게시물을 삭제하시겠습니까?");
	document.querySelector( ".js-modal-next" ).onclick = function(e){
		e.stopPropagation();
		$(".modal-box, .modal-overlay").fadeOut(function () {
			$(".modal-overlay").remove();
		       $("#confirmpopup").remove();
		       $("#confirmpopuparea").remove();
		});
		$(window).resize();
		
		$.ajax({
			type 		: 'delete',
			url 		: "/announce/delete?announceId=" + document.getElementById("pop_announceId").value,
			traditional : true,
			success		: function(data){
				if( data != null && data.status == "success" ) {
					successPopup(data.message);
				}
			}
		});
	}
}

// 목록
function goList( page ){
	if( isNull(page) ) {
		page = 1;
	}
	let param = {
		srcAdminId : $("#srcAdminId").val()
		,srcAnnounceObject : $("#srcAnnounceObject").val()
		,page : page
	}
	
	let url = "/announce/list" + toQueryString(param)
	console.log(url)	
//	location.href=url;
	
}

// 등록팝업
let openRegistPopup = function() {
	document.getElementById("detailTitle").innerText = "공지사항 등록";
	document.getElementById("pop_announceObject").value = "";
	document.getElementById("pop_announceContent").value = "";
	setDetailBtns(true);
}


// 상세
let goDetail = function(announce) {
	console.log(announce);
	document.getElementById("detailTitle").innerText = "공지사항 수정";
	document.getElementById("pop_announceId").value = announce.announceId;
	document.getElementById("pop_announceObject").value = announce.announceObject;
	document.getElementById("pop_announceContent").value = announce.announceContent;
	
	if( announce.adminId != getLoginInfo().loginId ) {
		// 조회 수 증가
		addCount(announce.announceId);
		// 버튼 컨트롤러
		setDetailBtns(false);
	} else {
		// 버튼 컨트롤러
		setDetailBtns(null, true);
	}
}


let goRegist = function() {
	let jsonData = {
		announceObject : document.getElementById("pop_announceObject").value
		,announceContent : document.getElementById("pop_announceContent").value
	};
	
	$.ajax({
		url: "/announce/register",
		type: "post",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(jsonData),
		success: function( data ){
			if(data.status == "success") {
				successPopup(data.message);
			} else {
				alertPopup(data.message);
			}
		}
	});
}


let goUpdate = function() {
	let jsonData = {
		announceId : document.getElementById("pop_announceId").value
		, announceObject : document.getElementById("pop_announceObject").value
		, announceContent : document.getElementById("pop_announceContent").value
	};
	
	$.ajax({
		url: "/announce/update",
		type: "put",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(jsonData),
		success: function( data ){
			if(data.status == "success") {
				successPopup(data.message);
			} else {
				alertPopup(data.message);
			}
		}
	});
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
 
 

let addCount = function(announceId) {
	$.ajax({
		type 		: 'get'
		,url 		: "/announce/add-count?announceId=" + announceId
		,success	: function(data){
		}
	});	
}

// 버튼 컨트롤러
let setDetailBtns = function(bool, registerIsMe) {
	document.querySelector("#pop_delete").setAttribute("style", "display:none;");
	document.querySelector("#pop_update").setAttribute("style", "display:none;");
	document.querySelector("#pop_regist").setAttribute("style", "display:none;");
	document.getElementById("pop_announceObject").setAttribute("readonly", "readonly");	
	document.getElementById("pop_announceContent").setAttribute("readonly", "readonly");
	
	// 등록자 수정일 때
	if ( registerIsMe ) {
		document.querySelector("#pop_delete").setAttribute("style", "display:inline-block;");
		document.querySelector("#pop_update").setAttribute("style", "display:inline-block;");
		document.getElementById("pop_announceContent").removeAttribute("readonly");
	} else {
		// 등록일 때
		if( bool ) {
			document.getElementById("pop_announceObject").removeAttribute("readonly");	
			document.getElementById("pop_announceContent").removeAttribute("readonly");
			document.querySelector("#pop_regist").setAttribute("style", "display:inline-block;");
			document.querySelector("#pop_cancle").setAttribute("style", "display:inline-block;");
		// 수정일 때
		} else {
			document.querySelector("#pop_cancle").setAttribute("style", "display:inline-block;");
		}
	}
}