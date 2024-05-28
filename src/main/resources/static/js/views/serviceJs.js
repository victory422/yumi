/**
 * 		Script info
 *		1. file path	: /static/js/views/serviceJs.js
 *		2. project name	: yumi korea Admin
 * 		3. description	: 서비스 관리 스크립트 
 */
 
/* 목록 */
function goList(page) {
	let srcScName = document.querySelector("#srcScName").value;
	let srcSvCode = document.querySelector("#srcSvCode").value;
	let srcSvName = document.querySelector("#srcSvName").value;
	let srcScCreator = document.querySelector("#srcScCreator").value;
	let srcFrom = document.querySelector("#srcFrom").value;
	let srcTo = document.querySelector("#srcTo").value;
	let queryStr = "";
	
	if( srcSvCode.trim() != "" & typeof srcSvCode != "number" ) {
		alertPopup("서버코드는 숫자만 입력 가능합니다.");
		return;
	}
	
	
	if( (srcScName != null && srcScName != "") ||
		(srcSvCode != null && srcSvCode != "") ||
		(srcSvName != null && srcSvName != "") ||
		(srcScCreator != null && srcScCreator != "") ||
		(srcFrom != null && srcFrom != "") ||
		(srcTo != null && srcTo != "")
	 ) {
		 queryStr += "&srcScName=" + srcScName + "&srcSvCode=" + srcSvCode + "&srcSvName=" + srcSvName + "&srcScCreator=" + srcScCreator;
		 queryStr += "&srcFrom=" + srcFrom + "&srcTo=" + srcTo;
	 }
	
	let uri = '/service/list?page=' + page + queryStr;
	
//	location.href= uri;
	
	//location.href='/service/list?page=' + page ;
}

/* 삭제 */
function goDelete(){
	
	confirmPopup("선택한 서비스를 삭제하시겠습니까?");
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
			alert( "삭제할 서비스를 선택해주세요." );
			return false;	
		} else {
			var arr = new Array( selectedElementsCnt );
			
			for( var i=0; i<selectedElementsCnt; i++ ) {
				arr[i] = selectedElements[i].getAttribute( "svCode" ) 
					+ "-" + selectedElements[i].getAttribute( "scCode" );
			}
			
			$.ajax({
				type 		: 'delete',
				url 		: '/service/delete',
				data 		: { arr : arr },
				traditional : true,
				success		: function(data){
					if(data.status == "success"){
						successPopup(data.message);
					} else {
						alert(data.message);
					}
				}
			});
			
		}
		
	}
	
	
	
}

/* 등록 */
function goRegistry() {
	
	if( $("#regSvName").val() == "" ){
		alertPopup("서버 이름을 입력하세요.");
	}
	if( $("#regScName").val() == "" ){
		alertPopup("서비스 이름을 입력하세요.");
	}
	if( $("#regScDesc").val() == "" ){
		alertPopup("서비스 설명을 입력하세요.");
	}
	
	let jsonData = {
		scName	 	: $("#regScName").val(),
		svName		: $("#regSvName").val(),
		description	: $("#regScDesc").val()
	};
	
	// 빈 값 보낼 시 auth에서 validation 체크할 때 오류
	/*
	if( $("#regScDesc").val() == "" ) {
		jsonData.description = " ";
	}
	*/
	
	$.ajax({
		url: "/service/register",
		type: "post",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(jsonData),
		success: function( data ){
			
			if ( data.status == "success" ) {
				$("#registerService").css("display","none");
				$("#regScName").val("");
				$("#regSvName").val("");
				$("#regScDesc").val("");
				
				successPopup("서비스가 등록되었습니다.");
			} else {
				alertPopup(data.message);
			}
		}
	})
}

/* 추가 등록 유효성 검사 */
function goRegistry2Chk() {
	const query = 'input[name="chk"]:checked';
    const selectedElements = document.querySelectorAll( query );
    
    // 체크박스 체크된 항목의 개수
    const selectedElementsCnt = selectedElements.length;
    if(selectedElementsCnt == 0){
		alertPopup("서버를 먼저 선택해야합니다.");
		return false;
	} else if(selectedElementsCnt > 1){
		alertPopup("한개만 선택가능합니다.");
		return false;
	} else { 
	    var svCode = selectedElements[0].getAttribute( "svCode" );
	    var svName = selectedElements[0].getAttribute( "svName" );
	    console.log("서버코드: " + svCode);
	    console.log("서버이름: " + svName);
	    
		const popupHidden =  "<div id='servicePoparea' name='adminPoparea' class='js-open-modal btn'></div>"
         $("body").append(popupHidden);
         const appendthis =  ("<div class='change-modal-overlay js-modal-close-change'></div>");
         //e.preventDefault();
         $("body").append(appendthis);
         $(".change-modal-overlay").fadeTo(100, 0.7);
         $('#registerService2').fadeIn($(this).data());

         $(".modal-box-change").css({
             top: ($(window).height() - $(".modal-box-change").outerHeight()) / 2,
             left: ($(window).width() - $(".modal-box-change").outerWidth()) / 2
         });
         
         $("#regSvName2").val(svName);
         $("#regSvCode2").val(svCode);
	}
}

/* 추가 등록 */
function goRegistry2() {
	let jsonData = {
		scName		: $("#regScName2").val(),
		svCode		: $("#regSvCode2").val(),
		description	: $("#regScDesc2").val()
	};
	
	$.ajax({
		url: "/service/register",
		type: "post",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(jsonData),
		success: function(data){
			if( data.status == "success" ) {
				$("#registerService2").css("display","none");
				$("#regScName2").val("");
				$("#regSvCode2").val("");
				$("#regScDesc2").val("");
				
				location.reload();
			} else {
				alertPopup(data.message);
			}
		}
	})
}

// 화면 전역변수. 서비스명을 수정했는지 체크한다.
let global_serviceName = "";

// 상세
function goDetail(svc) {
	fn_modalOpen("updateService");
	// 화면 전역변수. 서비스명을 수정했는지 체크한다. 
	global_serviceName = svc.scName;

	$.ajax({
		type		: 'get',
		url			: '/service/detail',
		data		: { "svCode" : svc.svSeqId, "scCode" : svc.scCode },
		dataType	: 'json',
		success		: function(response){
			console.log(response);
			if( response != null && response.status == "success" )
			$("#detSvcDesc").text(response.resultMap);
		}
			
	});
	
	$("#detSvcName").val(svc.scName);
	$("#detScCode").val(svc.scCode);
	$("#detSvCode").val(svc.svSeqId);
	$("#detSvName").text(svc.svName);
}

function goUpdate() {
	var svcName = $("#detSvcName").val();
	if( svcName == null || svcName == "" ){
		alert("서비스명을 입력하세요.");
		return false;
	}
	
	let reqData = {
		svCode	 	: $("#detSvCode").val(),
		scCode	 	: $("#detScCode").val(),
		description	: $("#detSvcDesc").val(),
		//scName	 	: $("#detSvcName").val()
	};
	
	// 서비스명을 수정했을 때 (글로벌변수와 현 데이터가 다르다)
	if( global_serviceName != svcName ) {
		reqData.scName = $("#detSvcName").val(); 
	}
	
	$.ajax({
		url: "/service/update",
		type: "put",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(reqData),
		success: function(result){
			// 수정 성공
			if(result.status == "success" ){
				
				$("#updateService").css("display","none");
				$("#detSvcName").val("");
				$("#detSvcDesc").val("");
				
				successPopup(result.message);
			// 수정 실패
			} else {
				alertPopup(result.message);	
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
});


$(document).ready(function() {
//	fn_modalOpen("servicePoparea");
	
     // modal popup close
     $(document).on("click", ".js-modal-close-change", function (e) {
         e.stopPropagation();
         $("#registerService").css("display","none");
         $("#registerService2").css("display","none");
         $("#updateService").css("display","none");
         $("servicePoparea").remove();
         
         // 등록
         $("#regServerId").val("");
         $("#regServerName").val("");
         $("#regServerDesc").val("");

         // 수정
     });
     
     // 서버코드는 숫자만 입력 가능
	document.querySelector("#srcSvCode").addEventListener("keyup", function(){
		this.value = this.value.replace(/\D/g, '');
	});
 });


