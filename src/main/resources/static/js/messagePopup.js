$( function() {
    "use strict";

    $(window).resize(function () {
        $(".modal-box, .modal-box-al").css({
            top: ($(window).height() - $(".modal-box, .modal-box-al").outerHeight()) / 2,
            left: ($(window).width() - $(".modal-box, .modal-box-al").outerWidth()) / 2
        });
    });

    //alert 취소 클릭시 팝업창 닫기
    $(document).on("click", ".js-modal-close, .modal-overlay", function () {
        $(".modal-box, .modal-box-al, .modal-overlay").fadeOut(function () {
            $(".modal-overlay").remove();
            $("#alertpopup").remove();
            $("#alertpopuparea").remove();
            //existPop = false;
        });
    });

    if($("#resultMsg").val() != "" && $("#resultMsg").val() != null){
        alertPopup($("#resultMsg").val());
    }

    // 엔터로 팝업 발생 후 엔터 시 팝업 제거
    $('body').keypress(function(e){
        if(e.keyCode !== 13) return;
        if($("#alertpopup").length){
            $(".modal-box, .modal-box-al, .modal-overlay").fadeOut(function () {
                $(".modal-overlay").remove();
                $("#alertpopup").remove();
                $("#alertpopuparea").remove();
            });
        }
    });

    // 클릭으로 팝업 발생 후 엔터 시 팝업 제거
    $('#popup').on('click', function(e){
        if($("#alertpopup").length === 1){
            e.preventDefault();
            $(".modal-box, .modal-box-al, .modal-overlay").fadeOut(500, function () {
                $(".modal-overlay").remove();
                $("#alertpopup").remove();
                $("#alertpopuparea").remove();
            });
        }
    });

    //confirm 취소 클릭시 팝업창 닫기
    $(document).on("click", ".js-modal-close-confirm", function (e) {
        e.stopPropagation();
        $(".modal-box, .modal-box-al, .modal-overlay").fadeOut(function (){
        	isLogout = false;
            $(".modal-overlay").remove();
            $("#confirmpopup").remove();
            $("#confirmpopuparea").remove();
        });
    });

    //confirm 확인 클릭 시, 각 페이지에 맞게 진행(각 페이지에 구현)
    /*
    $(document).on("click", ".js-modal-next", function () {
        e.stopPropagation();
        $(".modal-box, .modal-box-al, .modal-overlay").fadeOut(function () {
            $(".modal-overlay").remove();
            $("#confirmpopuparea").remove();
            $("#confirmpopuparea").remove();
        });
        //ex) 삭제

        $(window).resize();
    });
    */
});

function alertPopup(text){

    var popupHidden =  ("<a id='alertpopuparea' name='alertpopuparea' class='js-open-modal btn'></a>");
    $("body").append(popupHidden);
    var appendthis =  "<div class='modal-overlay js-modal-close'></div>"
    var popup      = "<div id='alertpopup' class='modal-box-al'>"
        + "<div class='modal_header'></div>"
        + "<div class='modal_body'>"
        + text
        + "</div>"
        + "<div class='modal_footer'>"
        + "<div class='modal_footer_box'>"
        + "<div class='pop_btn_one width_100' button-type='COMMON'><a class='js-modal-close'>확인</a>"
        + "</div></div></div></div>";
    $("body").append(popup);
    $("body").append(appendthis);
    $(".modal-overlay").fadeTo(500, 0.7);

    var modalBox = "alertpopup";
    $('#'+ modalBox).fadeIn($("#alertpopuparea").data());

    $(window).resize();
    fn_messagePopupKeyEvent("alertpopup");
}

function confirmPopup(text){

    var popupHidden =  ("<a id='confirmpopuparea' name='confirmpopuparea' class='js-open-modal btn'></a>");
    $("body").append(popupHidden);
    var appendthis =  "<div class='modal-overlay js-modal-close-confrim'></div>"
    var popup      = "<div id='confirmpopup' class='modal-box-al'>"
        + "<div class='modal_header'></div>"
        + "<div class='modal_body'>"
        + text
        + "</div>"
        + "<div class='modal_footer'>"
        + "<div class='modal_footer_box'>"
        + "<div class='pop_btn_one width_100' button-type='COMMON'><a class='js-modal-next'>확인</a></div>"
        + "<div class='pop_btn_cancle width_100'><a class='js-modal-close-confirm'>취소</a></div>"
        + "</div></div></div>";
    $("body").append(popup);
    $("body").append(appendthis);
    $(".modal-overlay").fadeTo(500, 0.7);

    var modalBox = "confirmpopup";
    $('#'+ modalBox).fadeIn($("#confirmpopuparea").data());

    $(window).resize();
    fn_messagePopupKeyEvent("confirmpopup");
}

//성공팝업 닫기 후 처리 
//true : 페이지 리로딩
//false : 목록페이지로 이동
let isReload = true;
let moveURL = "";
function successPopup(text, url, reload){
	isReload = true;
	moveURL = "";
	if ( typeof url != "undefined" && url.length > 0 ) moveURL = url;
	if ( typeof reload != "undefined" && reload == false ) isReload = reload;
    var popupHidden =  ("<a id='successpopuparea' name='successpopuparea' class='js-open-modal btn'></a>");
    $("body").append(popupHidden);
    var appendthis =  "<div class='success-modal-overlay js-modal-close-success'></div>"
    var popup      = "<div id='successpopup' class='modal-box-al'>"
        + "<div class='modal_header'></div>"
        + "<div class='modal_body'>"
        + text
        + "</div>"
        + "<div class='modal_footer'>"
        + "<div class='modal_footer_box'>"
        + "<div class='pop_btn_one width_100' button-type='COMMON'><a class='js-modal-close-success'>확인</a>"
        + "</div></div></div></div>";
    $("body").append(popup);
    $("body").append(appendthis);
    $(".success-modal-overlay").fadeTo(500, 0.7);

    var modalBox = "successpopup";
    $('#'+ modalBox).fadeIn($("#successpopuparea").data());

    $(window).resize();
    
    fn_messagePopupKeyEvent("successpopup");
}

//success 취소 클릭시 팝업창 닫기
$(document).on("click", ".js-modal-close-success, .success-modal-overlay", function () {
    $(".modal-box-al, .success-modal-overlay").fadeOut(function () {
        $(".success-modal-overlay").remove();
        $("#successpopup").remove();
        $("#successpopuparea").remove();
        if(isReload){
        	if( moveURL == "" ) {
				location.reload();
			} else {
				location.href = moveURL;
			}
        }else{
        	goList();
        }
    });
});


/* messagePopup event 처리 추가 */
let fn_messagePopupKeyEvent = function (domId) {
	let dom = document.getElementById(domId);
	/* modal 팝업창 닫기 이벤트 */
    window.addEventListener("keyup", function(e) {
		if( dom == null ) {
			if( document.querySelector(".modal-box-change") == null ) {
				window.removeEventListener("keyup");
				return;
			} else {
				dom = document.querySelector(".odal-box-change");
			}
		}
		var tempArr = [];
		
		switch ( e.keyCode ) {
			case 27 : // esc
				tempArr.push(document.querySelector("div.pop_btn_cancle > a"));
				tempArr.push(document.querySelector("div.modal_footer > div > div.pop_btn_cancle.js-modal-close-change > a"));
				tempArr.push(document.querySelector("#confirmpopup > div.modal_footer > div > div.pop_btn_cancle > a"));
				tempArr.push(document.querySelector("#alertpopup > div > div > .pop_btn_one > .js-modal-close"));
				tempArr.push(document.querySelector("#successpopup > div.modal_footer > div > div > a"));
			break;
			
			case 13 : // enter
//				tempArr.push(document.querySelector(".pop_btn_one.js-modal-close"));
				tempArr.push(document.querySelector(".pop_btn_one.js-modal-close-success"));
				tempArr.push(document.querySelector("#confirmpopup > div.modal_footer > div > div.pop_btn_one > a.js-modal-next"));
				tempArr.push(document.querySelector("#alertpopup > div > div > .pop_btn_one > .js-modal-close"));
				tempArr.push(document.querySelector("#successpopup > div.modal_footer > div > div > a"));
			break;
			
			case 32 : // space key
				tempArr.push(document.querySelector(".pop_btn_one.js-modal-close-success"));
				tempArr.push(document.querySelector(".pop_btn_one > .js-modal-next"));
				tempArr.push(document.querySelector("#alertpopup > div > div > .pop_btn_one > .js-modal-close"));
				tempArr.push(document.querySelector("#successpopup > div.modal_footer > div > div > a"));
			break;
		}
		closePopup(tempArr);
	});
}

/* modal 팝업창 닫기 이벤트 */
function closePopup( domArr ) {
	domArr.forEach(function(d){
		if( d != null ) d.click();
	});
}
