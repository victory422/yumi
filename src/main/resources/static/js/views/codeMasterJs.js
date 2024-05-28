/**
 * Script info
 * 1. file path		: /static/js/views/codeMasterJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 공통코드 관리 스크립트 
 */
let g_codeNames = []; 
// 목록 조회
function goList( page ){
	var srcMasterCode = $("#srcMasterCode").val();
	
	// 검색 조건이 있는 경우
	if( srcMasterCode != null && srcMasterCode != null ){
		location.href='/code/list?page=' + page + '&srcMasterCode=' + srcMasterCode;
		
	} else {
		location.href='/code/list?page=' + page;
	}	
} 

// 등록
function goRegistry(){
	if( $("#regMcodeName").val() == null || $("#regMcodeName").val() == "" ) {
		alert("코드명을(를) 입력하세요.");
		return false;
	} else if( $("#regMcodeLen").val() == null || $("#regMcodeLen").val() == "" ) {
		alert("길이을(를) 입력하세요.");
		return false;
	}
	
	let requestData = {
		mastercode: $("#regMcodeName").val(),
		description: $("#regMcodeDesc").val(),
		length: $("#regMcodeLen").val()
	}
	
	$.ajax({
			url: "/code/register",
			type: "post",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify(requestData),
			success: function( data ){
				// 실패
				if(data.status == "fail"){
					alertPopup(data.message);
				// 성공인경우
				} else {
					alert(data.message);
					
					$("#registerMasterCode").css("display","none");
					$("#regMcodeName").val("");
					$("#regMcodeDesc").val("");
					$("#regMcodeLen").val("");
					
					location.reload();
				}
			}
		})
}

function goUpdate(){
	let requestData = {
		mastercode: $("#detMcodeName").val(),
		description: $("#detMcodeDesc").val(),
		length: $("#detMcodeLen").val()
	}
	
	$.ajax({
			url: "/code/update",
			type: "put",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify(requestData),
			success: function( data ){
				// 실패
				if(data.status == "fail"){
					alertPopup(data.message);
				// 성공인경우
				} else {
					alert(data.message);
					
					$("#updateMasterCode").css("display","none");
					$("#detMcodeName").val("");
					$("#detMcodeDesc").text("");
					$("#detMcodeLen").val("");
					
					location.reload();
				}
			}
		});
}

function goDetail(){
	const query = 'input[name="chk"]:checked';
    const selectedElements = document.querySelectorAll( query );
    
    // 체크박스 체크된 항목의 개수
    const selectedElementsCnt = selectedElements.length;
    
    if( selectedElementsCnt == 0 ) {
		alert( "수정할 코드를 선택해주세요." );
		return false;	
	} else if( selectedElementsCnt > 1){
		alert( "한 번에 한 개의 코드만 수정 가능합니다." );
		return false;
	} else {
		
		$('#detMcodeName').val('');
		$('#detMcodeLen').val('');
		$('#detMcodeDesc').text('');
		
		$('#detMcodeName').val(selectedElements[0].getAttribute("chkId"));
		$('#detMcodeLen').val(selectedElements[0].getAttribute("chkLen"));
		$('#detMcodeDesc').text(selectedElements[0].getAttribute("chkDesc"));
		
		cloneNodeSet.add('updateMasterCode');
		
	    const popupHidden =  "<div id='codePoparea' name='codePoparea' class='js-open-modal btn'></div>"
	    $("body").append(popupHidden);
	    const appendthis =  ("<div class='change-modal-overlay js-modal-close-change'></div>");
	    //e.preventDefault();
	    $("body").append(appendthis);
	    $(".change-modal-overlay").fadeTo(100, 0.7);
	    const modalBox = 'updateMasterCode';
	    //let copyNode = document.getElementById(modalBox).cloneNode(true);
	    lastPopupId = modalBox;
	    $('#'+ modalBox).fadeIn($(this).data());
	    $(".modal-box-change").css({
		    top: ($(window).height() - $(".modal-box-change").outerHeight()) / 2,
		    left: ($(window).width() - $(".modal-box-change").outerWidth()) / 2
	    });
	    
	}
	
}

function goDelete(){
	const query = 'input[name="chk"]:checked';
    const selectedElements = document.querySelectorAll( query );
    
    // 체크박스 체크된 항목의 개수
    const selectedElementsCnt = selectedElements.length;
    
    if( selectedElementsCnt == 0 ) {
		alert( "삭제할 코드를 선택해주세요." );
		return false;	
	} else {
		confirmPopup("선택한 코드를 삭제하시겠습니까?");
		document.querySelector( ".js-modal-next" ).onclick = function(e){
			e.stopPropagation();
			$(".modal-box, .modal-overlay").fadeOut(function () {
				$(".modal-overlay").remove();
			       $("#confirmpopup").remove();
			       $("#confirmpopuparea").remove();
			});
			$(window).resize();
			
			var arr = new Array( selectedElementsCnt );
				
			for( var i=0; i<selectedElementsCnt; i++ ) {
				arr[i] = selectedElements[i].getAttribute( "chkId" );
			}
			
			$.ajax({
				type 		: 'delete',
				url 		: '/code/delete',
				data 		: { idArr : arr },
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

$(document).ready(function() {
	// 모달 팝업 open
	fn_modalOpen("codePoparea");
	
	document.querySelector("a[change-modal-id=registerMasterCode]").addEventListener("click", function(){
		getCodeNames();
	});
	
	
	// 마스터코드 길이 입력 숫자만 가능
	$('#regMcodeLen').keyup(function(e){
		const regExp = /[^0-9]/g;
		const ele = e.target;
		if (regExp.test(ele.value)) {
			ele.value = ele.value.replace(regExp, '');
		}
	});
	
	// 마스터코드명 중복체크
	$('#regMcodeName').keyup(function(e){
		chkCharCode(e);
		var mcodeName = $('#regMcodeName').val();
		mcodeName = mcodeName.toUpperCase();
		
		this.value = mcodeName;
		for( var i = 0 ; i < g_codeNames.length; i ++ ) {
			if( g_codeNames[i] == mcodeName ) {
				checkDuplicateName(true);
				break;
			} else {
				checkDuplicateName(false);
			}
		}
		
	});
 });	     
 
 
 let getCodeNames = function() {
	$.ajax({
		url: "/code/get-code-names",
		type: "get",
		contentType: "application/json; charset=UTF-8",
		success: function( result ){
			if( result.status.toString() == "success" ){
				g_codeNames = result.codeNames;
			}
		}
	})
}

let checkDuplicateName = function(idDuplChk) {
	// 해당 ID로 등록된 user_info가 있을 때 (red)
	if( idDuplChk == true ){
		$('#regMcodeName').css("background-color","rgba(245, 0, 0, 0.22)");
	} else if ( $('#regMcodeName').val().length == 0 ){
		$('#regMcodeName').removeAttr("style");
	// 해당 ID로 등록된 user_info가 없을 때 (green)
	} else{
		$('#regMcodeName').css("background-color","rgba(0, 245, 75, 0.22)");					
	} 
}