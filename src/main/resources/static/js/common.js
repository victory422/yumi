/************************************** yumi korea Web Admin **************************************/
let lastPopupId = "";
let cloneNodeSet = new Set();
// 모달 팝업 open
function fn_modalOpen(popareaName){
	document.querySelectorAll("a[change-modal-id], tr[change-modal-id]").forEach(function(d){
		cloneNodeSet.add(document.getElementById(d.getAttribute("change-modal-id")).id);
	});
	
	$(document).on("click", "a[change-modal-id], tr[change-modal-id]", function(){
	    const popupHidden =  "<div id='" + popareaName +"' name='" + popareaName + "' class='js-open-modal btn'></div>"
	    $("body").append(popupHidden);
	    const appendthis =  ("<div class='change-modal-overlay js-modal-close-change'></div>");
	    //e.preventDefault();
	    $("body").append(appendthis);
	    $(".change-modal-overlay").fadeTo(100, 0.7);
	    const modalBox = $(this).attr('change-modal-id');
	    //let copyNode = document.getElementById(modalBox).cloneNode(true);
	    lastPopupId = modalBox;
	    $('#'+ modalBox).fadeIn($(this).data());
	    $(".modal-box-change").css({
		    top: ($(window).height() - $(".modal-box-change").outerHeight()) / 2,
		    left: ($(window).width() - $(".modal-box-change").outerWidth()) / 2
	    });
	});
	
	// modal popup - close
    $(document).on("click", ".js-modal-close-change", function (e) {
        e.stopPropagation();
        $(popareaName).remove();
		cloneNodeSet.forEach(function(node) {
			$(document.getElementById(node)).css("display","none");
			if( node == lastPopupId ) {
				var thisPopup = document.getElementById(lastPopupId); 
				// input text, email password, hidden 초기화
				thisPopup.querySelectorAll("input[type=text], input[type=email], input[type=password], input[type=hidden]").forEach(function(v){
				    v.value = "" ;
				    v.removeAttribute("style");
				});
				// selectBox 초기화
				thisPopup.querySelectorAll("select").forEach(function(v){
				    if (typeof v.children[0] != "undefined" ) {
					    var firstVal = v.children[0].value;
					    setSelectBoxValue(v.id, firstVal);
					}
				});
				
				document.querySelectorAll("#" + lastPopupId).forEach(function(v){
				    if( !v.hasChildNodes() ) v.remove();
				});
	
			}
		});
		// $(window).resize();
     });
     fn_messagePopupKeyEvent(popareaName);
}


function String2Hex(tmp) {
	var str = '';
	for (var i = 0; i < tmp.length; i++) {
		str += tmp[i].charCodeAt(0).toString(16);
	}
	return str;
}

// 삭제
function fn_delete(url){
	confirmPopup("선택한 항목을 삭제하시겠습니까?");
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
			alert( "삭제할 항목을 1개 이상 선택해주세요." );
			return false;	
		} else {
			var arr = new Array( selectedElementsCnt );
			
			for( var i=0; i<selectedElementsCnt; i++ ) {
				arr[i] = selectedElements[i].getAttribute( "chkId" );
			}
			
			$.ajax({
				type 		: 'post',
				url 		: url,
				data 		: { arr : arr },
				traditional : true,
				success		: function(data){
					if(data) {
						successPopup("선택한 항목을 삭제했습니다.");
					} else {
						alert("삭제 중 에러가 발생했습니다.");					
					}
				}
			});
			
		}
		
	}
}

function getCode(master_code){
	var ret;
	var data = { "master_code": master_code };
	$.ajax({
		url : 'code-detail',
 		dataType: "json",
		contentType: "application/json; charset=utf-8",
		type: "POST",
		async: false,
		data: JSON.stringify(data),
		success:function(data) {
			ret = data.data;
		},
		error: function(request, status, error) {
			alert("code : "+request.status+"\n"
					//+ "message:"+request.responseText
					+ "\n"+"error : "+error);
		}
	});
	
	return ret;
}

function getCodeList(){
	$.ajax({
		url : 'code-list',
 		dataType: "json",
		contentType: "application/json; charset=utf-8",
		type: "POST",
		async: false,
		success:function(data) {
			return data;
		},
		error: function(request, status, error) {
			alert("code : "+request.status+"\n"
					//+ "message:"+request.responseText
					+ "\n"+"error : "+error);
		}
	});
}

function makeSelBox( codeArr, selBoxId ){
	alert( "selBoxId >> " + selBoxId );
	
//	$("#" + selBoxId ).empty();
	$("#" + selBoxId ).append(
		$('<option value="" >선택</option>')
	);
//	
//	for( var i=0; i<codeArr.length; i++ ){
//		console.log(codeArr[i]["code"]);
//		$("#" + selBoxId ).append(
//			$('<option value="' + codeArr[i]["code"] + '" >' + codeArr[i]["description"] + '</option>')
//		);
//	}
	
}


/*
 * targetrl로 submit
 * @param targetUrl
 * @returns
 */
function goAjaxSubmit(targetUrl){
	loadingAjaxImage();
	document.actionForm.action = targetUrl;
	document.actionForm.submit();	
}

/**
 * load ajax image
 * @returns
 */
function loadingAjaxImage(){
	$("#ajaxLoadingImage").show();
}

/**
 * hide ajax image
 * @returns
 */
function hideAjaxImage(){
	$("#ajaxLoadingImage").hide();
}

/**
 * 
 * @param json
 * @param isSearchReset
 * @param reloadingUrl
 * @param failMsg
 * @returns
 */
function onAjaxSubmitSuccess(json, isSearchReset, reloadingUrl, failMsg){
	if(json.status == "success"){
		//검색 조건 초기화			
		if(isSearchReset){
			$("#p_MyPage").val("1");
			$('[name*=src_]').each(function(){
				$(this).val("");	
			});	
		}		
		goAjaxSubmit(reloadingUrl);
	}
	else{
		if(json.msg != null && !json.msg.isEmpty()){
			alert(json.msg);
		}
		else{
			alert(failMsg);
		}
		hideAjaxImage();
	}
}

    
// ajax 호출 시 세션만료일 경우 redirect  (LoginController에서 header값 주입) 
$(document).ajaxSuccess(function(event, request, settings) {
    if (request.getResponseHeader('REQUIRES_AUTH') === '1') {
		window.location = '/login';
    } else if (request.getResponseHeader('REQUIRES_AUTH') === '2') {
		window.location = '/invalidSession';
	}  else if (request.getResponseHeader('REQUIRES_AUTH') === '3') {
//			settings.success = null;
		settings.error(request, 403, null);	// common.js  ajaxSetup에 정의
	}
});

/**
 * AJAX 공통 Error 처리
 */
(function($) {
	$.ajaxSetup({
		beforeSend: function(xhr) {
			xhr.setRequestHeader("AJAX", true);
			let csrfToken = $("input[name=_csrf]").val();
			xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
		},
		error: function(request, status, error) {
			if(status == "408"){
				alert("Session TimeOut or Session invalid");
				let contextPath = $("#contextPath").val();
				let url = window.location.origin;
				if(typeof contextPath == "undefined" || contextPath == ""){
					document.location.href = url +  "/logout";
				}else {
					document.location.href = url + contextPath + "/logout";
				}
			}
			// The user does not have valid permissions for the requested URI
			else if(status == "403") {
				console.error(request.responseJSON);
				alert(request.responseJSON.message);
				location.reload();
			}
			else if(request.status == "400") {
				alertPopup("code : "+request.status+"<br/>"
						+ "error : "+"알 수 없는 오류가 발생하였습니다. <br/>관리자에게 문의해주세요.");
			}
			else {
				var message = "";
				
				try {
					if( request.responseJSON != null && request.responseJSON.message.cause != null ) {
						if( request.responseJSON.message.cause.errMsg != null ) {
							message = request.responseJSON.message.cause.errMsg;
						} else {
							message = request.responseJSON.message.cause.detailMessage;
						}
					} else if ( request.responseJSON.message != "" ) {
						message = request.responseJSON.message;
					}
					
					if( typeof message == "object" ) {
						messgae = message.detailMessage;
					} else if( typeof message == "string" && message.indexOf("JsonParseException") > -1 ) {
						message = "입력 문자열 형식 오류가 발생했습니다.";
					}
					
					if( typeof message == "undefined" ) {
						message = "알 수 없는 오류가 발생하였습니다. <br/>관리자에게 문의해주세요.";
					}
				
				} catch {
					message = "알 수 없는 오류가 발생하였습니다. <br/>관리자에게 문의해주세요.";
				}
				
				alertPopup("code : "+request.status+"<br/>"
						+ "error : "+message);
			}
		}
	});
})(jQuery);

/**
 * 화면 초기화
 * @returns
 */
function initContents(){
	setOnlyNum(); //숫자
	setAutoTelNo(); //전화번호
	setMask(); //mask
	
	$("select[id*=src_]").change( function(){
		goList();
	});
}

/**
 * 메뉴 활성화
 * @param superMenu
 * @param subMenu
 * @param menu
 * @returns
 */
function changeActiveMenu(superMenu, subMenu, menu){
	$("#" + superMenu).addClass("on");
	if(subMenu){
		$("#" + subMenu).addClass("on_content").css( "display", "block" );
		$("#" + menu).css( 'color', '#02c0ce' );
	}
}

/**
 * Checked valid FromDate, Todate 
 * @param fromId
 * @param toId
 * @returns
 */
function isValidDate(fromId, toId){	
	var fromVal = $("#"+fromId).val();
	var toVal = $("#"+toId).val();
	
	if(fromVal.isEmpty() || toVal.isEmpty()){
		return true;
	}	
	
	if(fromVal > toVal){
		alert("날짜를 확인하세요.");//날짜를 확인하세요.
		return false;
	}	
	return true;
}

/**
 * 업로드하려는 파일의 확장자 제한
 * @param objName
 * @returns
 */
function validUploadFile(objName){
	var result = true;
	var regEx = /\.sh|\.bat|\.exe$/;
	var obj = eval('$(\"input[name=\'' + objName +'\']")');
	$(obj).each(function(){
		if(regEx.test( $(this).val()  ) ){
			alert(canNotUploadFile)
			result = false;
			return false;
		}		
	});
	return result;
}

/**
 * 패스워드 제약사항 체크
 * 8자리 이상, 영문, 숫자, 특수문자 혼용
 * @param pwStr
 * @returns
 */
function matchRegPW(pwStr){	
	if(pwStr.length < 8 || pwStr.length > 20){
		return false;
	}
	//8자리 이상, 영문, 숫자, 특수문자 혼용 정규식.
	let regExp1 = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
	if(!pwStr.match(regExp1)){
	      return false;
	  }
	
	return true;
}

/**
 * 
 * @param plainText 암호화할 데이타
 * @param salt
 * @param iv
 * @param passPhrase
 * @returns
 */
function getEncAES(plainText, salt, iv, passPhrase) {
	var keySize = 128;
	var iterations = iterationCount = 10000;

	var aesUtil = new AesUtil(keySize, iterationCount);
	var encrypt = aesUtil.encrypt(salt, iv, passPhrase, plainText);

	return encrypt;
}

/**
 * 정수만 입력 가능
 * @returns
 */
function setOnlyNum(){
	$('.numberInt').each(function(){
		$(this).keyup(function(event){			
			event = event || window.event;
			var _val = $(this).val().trim();
			var RegNotNum  = /[^0-9]/g; 
			_val = _val.replace(RegNotNum,''); 
			$(this).attr("maxlength", 10);
			$(this).val( _val ) ;
		});
	});
}

/************* 전화번호 자동 hypen ******************/
function setAutoTelNo(){
	$('.telNo , .mobileTelNo').each(function(){
		$(this).keyup(function(event){			
			event = event || window.event;
			var _val = $(this).val().trim();
			$(this).attr("maxlength", 13);
			$(this).val( autoHypenPhone(_val) ) ;
		});
	});
}

function autoHypenPhone(str){
	var RegNotNum  = /[^0-9]/g; 
	var RegPhonNum = ""; 
	var DataForm   = ""; 
	if( str == "" || str == null ) return ""; 
	
	// delete not number
	str = str.replace(RegNotNum,''); 
	
	/* 4자리 이하일 경우 아무런 액션도 취하지 않음. */
	if( str.length < 4 ) return str; 

	/* 지역번호 02일 경우 10자리 이상입력 못하도록 제어함. */
	if(str.substring(0,2)=="02" && str.length > 10){
		str = str.substring(0, 10);
	} 

	if( str.length > 3 && str.length < 7 ) { 
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2"; 
			RegPhonNum = /([0-9]{2})([0-9]+)/; 
		} else {
			DataForm = "$1-$2"; 
			RegPhonNum = /([0-9]{3})([0-9]+)/; 
		}
	} else if(str.length == 7 ) {
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3"; 
			RegPhonNum = /([0-9]{2})([0-9]{3})([0-9]+)/; 
		} else {
			DataForm = "$1-$2"; 
			RegPhonNum = /([0-9]{3})([0-9]{4})/; 
		}
	} else if(str.length == 9 ) {
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3"; 
			RegPhonNum = /([0-9]{2})([0-9]{3})([0-9]+)/; 
		} else {
			DataForm = "$1-$2-$3"; 
			RegPhonNum = /([0-9]{3})([0-9]{3})([0-9]+)/; 
		}
	} else if(str.length == 10){ 
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3"; 
			RegPhonNum = /([0-9]{2})([0-9]{4})([0-9]+)/; 
		}else{
			DataForm = "$1-$2-$3"; 
			RegPhonNum = /([0-9]{3})([0-9]{3})([0-9]+)/;
		}
	} else if(str.length > 10){ 
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3"; 
			RegPhonNum = /([0-9]{2})([0-9]{4})([0-9]+)/; 
		}else{
			DataForm = "$1-$2-$3"; 
			RegPhonNum = /([0-9]{3})([0-9]{4})([0-9]+)/; 
		}
	} else {	 
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{2})([0-9]{3})([0-9]+)/; 
		}else{
			DataForm = "$1-$2-$3"; 
			RegPhonNum = /([0-9]{3})([0-9]{3})([0-9]+)/;
		}
	}
	while( RegPhonNum.test(str) ) {  
		str = str.replace(RegPhonNum, DataForm);  
	} 
	return str; 
}

/**
 * Mask, maxlength, size
 * @returns
 */
function setMask(){
	//이메일
	$(".email").each(function(){
		$(this).attr("maxlength", 50);
		$(this).attr("size", 40);
	});
	
	//전화번호
	$(".telNo, .mobileTelNo").each(function(){
		$(this).attr("maxlength", 13);
		$(this).attr("size", 16);
	});	
	
	$('input[type=text]').each(function(){
		if($(this).attr("mask")){
			$(this).mask($(this).attr("mask"));
		}
	});
}

/**
 * Change List Count(한페이지 게시글 개수) 
 * @returns
 */
function changeListCnt(){
	var boardCnt = $("#boardCNT").val();
	$("#p_BoardCntPerOneScreen").val(boardCnt);
	if($("#paging_cert").val() == 'Y'){
		goView($("#dn").val());
	}else{
		goList('1');
	}
}

/**
 * 패스워드 capslock
 */
function checkCapsLock(passwordInputId){
	/* 
    * Bind to capslockstate events and update display based on state 
    */
    $(window).bind("capsOn", function(event) {
        if ($("#"+passwordInputId +":focus").length > 0) {
            $("#capsWarning").show();
            $("#login_button").css("margin-top", "7px");
        }
    });
    $(window).bind("capsOff capsUnknown", function(event) {
        $("#capsWarning").hide();
        $("#login_button").css("margin-top", "16.8px");
    });
    $("#"+passwordInputId).bind("focusout", function(event) {
        $("#capsWarning").hide();
        $("#login_button").css("margin-top", "16.8px");
    });
    $("#"+passwordInputId).bind("focusin", function(event) {
        if ($(window).capslockstate("state") === true) {
            $("#capsWarning").show();
            $("#login_button").css("margin-top", "7px");
        }
    });

    /* 
    * Initialize the capslockstate plugin.
    * Monitoring is happening at the window level.
    */
    $(window).capslockstate();
}

$(function(){
	if($("#resultMsg").val() != "" && $("#resultMsg").val() != null){
	    alert($("#resultMsg").val());
	}
});




/* 20230719 추가 - dashboard */
/* layout */
$( document ).ready(function() {
	"use strict";
	
	$('.nav-toggle').click(function(e) {
	  e.preventDefault();
	  $('#container').toggleClass('closeNav');
	  $('.nav-toggle').toggleClass('active');
	});
});

/* resize_layout */
$(document).ready(function(){
	"use strict";
	$(window).resize(function (){
		 var width = window.outerWidth;
		 if (width < 1125) {
			$('body').addClass('mobileNav');
			$('#container').removeClass('closeNav');

			$('.nav-toggle').click(function(e) {
			  e.preventDefault();
			  $('.nav-toggle').toggleClass('m_active');
			}); 
		 }
		 else if(width > 1125){
		 	$('body').removeClass('mobileNav');
			$( '.menu' ).hover(function() {
				$( '.menu' ).toggleClass('menu_hv');
			});
		 }
		 });
	}); 





/*Datepicker*/
  $(function() {
	"use strict";
    $(".S_date, .E_date").datepicker();
  });

  $(function() {
	"use strict";
	  $(".S_date, .E_date").attr( 'readOnly' , 'true' );
  });



/* 탭 메뉴*/ 
$(function () {
	"use strict";
    $(".tab_content").hide();
    $(".tab_content:first").show();

    $(".tabs ul li").click(function () {
        $(".tabs ul li").removeClass("active");
        $(this).addClass("active");
        $(".tab_content").hide();
        var activeTab = $(this).find("a").attr("href"); 
        $(activeTab).fadeIn();
		return false;
    });
});

/*alert popup*/
$(function(){
	"use strict";
	var appendthis =  ("<div class='modal-overlay js-modal-close'></div>");
	$('a[data-modal-id]').click(function(e) {
		e.preventDefault();
    $("body").append(appendthis);
    $(".modal-overlay").fadeTo(500, 0.7);
    //$(".js-modalbox").fadeIn(500);
		var modalBox = $(this).attr('data-modal-id');
		$('#'+modalBox).fadeIn($(this).data());
	});  
	$(".js-modal-close, .modal-overlay").click(function() {
		$(".modal-box, .modal-overlay").fadeOut(500, function() {
			$(".modal-overlay").remove();
		});
	});
	$(window).resize(function() {
		$(".modal-box").css({
			top: ($(window).height() - $(".modal-box").outerHeight()) / 3,
			left: ($(window).width() - $(".modal-box").outerWidth()) / 2
		});
	});
	$(window).resize();
});



/*select*/
$(document).ready(function(){
	"use strict";
	$("select").styledSelect();
});




/*placeholder*/
$(document).ready(function(){
	"use strict";
    $('.placeholder').autoClear();
});


/* add search box*/
$( document ).ready(function() {
	"use strict";
$(".search_more_btn").click(function () { $(".add_search_info_box").toggle(); });

});

/* add box*/
$( document ).ready(function() {
	"use strict";
$(".view_btn").click(function () { $(".view_info_box").toggle(); });

});





/*alert popup02*/
$(function(){
	"use strict";
	var appendthis =  ("<div class='modal-overlay js-modal-close'></div>");
	$('a[data-modal-id]').click(function(e) {
		e.preventDefault();
    $("body").append(appendthis);
    $(".modal-overlay").fadeTo(500, 0.7);
    //$(".js-modalbox").fadeIn(500);
		var modalBox = $(this).attr('data-modal-id');
		$('#'+modalBox).fadeIn($(this).data());
	});  
	$(".js-modal-close, .modal-overlay").click(function() {
		$(".modal-box_a, .modal-overlay").fadeOut(500, function() {
			$(".modal-overlay").remove();
		});
	});
	$(window).resize(function() {
		$(".modal-box_a").css({
			top: ($(window).height() - $(".modal-box_a").outerHeight()) / 2,
			left: ($(window).width() - $(".modal-box_a").outerWidth()) / 2
		});
	});
	$(window).resize();
});




/*항상 떠 있는 팝업 스크립트
$(function(){
	"use strict";
	$(".js-modal-close, .modal-overlay").click(function() {
		$(".modal-box-a, .modal-overlay").fadeOut(500, function() {
			$(".modal-overlay").remove();
		});
	});
	$(window).resize(function() {
			$(".modal-box-a").css({
				top: ($(window).height() - $(".modal-box-a").outerHeight()) / 2,
				left: ($(window).width() - $(".modal-box-a").outerWidth()) / 2
			});
		});
});

/*LNB Navigation */
( function( $ ) {
	"use strict";
	$( document ).ready(function() {
	$('.LNB_navi > ul > li > a').click(function() {
	  $('.LNB_navi li').removeClass('active');
	  $(this).closest('li').addClass('active');	
	  var checkElement = $(this).next();

	  if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
		$('.LNB_navi ul ul:visible').slideUp('normal');
		$(".has_sub_show").attr('id', '0');
		$(".has_sub_show").children('ul').slideUp();
		checkElement.slideDown('normal');
	  }

	});
	});
})( jQuery );


/* table */
	$(function(){
	"use strict";
		$('.tablesorter').tablesorter({
			usNumberFormat : false,
			sortReset      : true,
			sortRestart    : true
		});
	});


/* div 높이 같게 */
$(document).ready(function(){
	"use strict";
	$('.graph_wrap_con_wrap').each(function(){

	var highestBox = 0;
	$('.layout').each(function(){

	if($(this).height() > highestBox)
	highestBox = $(this).height();
	});

	$('.layout').height(highestBox);

});
});

/* ie8 */
/* check */
$(document).ready(function() { 
	"use strict";
	$("input:checkbox").on('click', function() { 
		if ( $(this).prop('checked') ) { 
			$(this).parent().addClass("selected"); 
		} else {
			$(this).parent().removeClass("selected"); 
		} 
	}); 
});
		
		
$(document).ready(function(){
	"use strict";
	var _designRadio = $('.designRadio');
	var _iLabel = $('.iLabel');
	$(_iLabel).click(function(){
		var _thisRadio = $(this).parent().find('> .designRadio');
		var _value = $(this).parent().find('>input').val();
		$(_designRadio).children().removeClass('checked');
		$(_thisRadio).children().addClass('checked');
		console.log(_value);
	});
	$(_designRadio).click(function(){
		var _value = $(this).parent().find('>input').val();
		$(_designRadio).children().removeClass('checked');
		$(this).children().addClass('checked');
		console.log(_value);
	});
});



/*table add*/
   $(document).ready(function(){
	$(".add").click(function(){
    $(".addtbl01:first").clone(true).appendTo("#data_tbl")
     .find('input[type="text"]').val('').end()
		
	});
  $('.delete').click(function(){
    $(this).parents(".addtbl01").remove();
 });
}); 

/*table add*/
   $(document).ready(function(){
	$(".add02").click(function(){
    $(".addtbl02:first").clone(true).appendTo("#data_tbl")
     .find('input[type="text"]').val('').end()
  });
  $('.delete02').click(function(){
    $(this).parents(".addtbl02").remove();
 });
}); 

	
