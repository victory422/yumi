/*placeholder*/
$(document).ready(function(){
	"use strict";
    $('.placeholder').autoClear();
});


/* layout */
$( document ).ready(function() {
	"use strict";
	
	$('.nav-toggle').click(function(e) {
	  e.preventDefault();
	  $('#container').toggleClass('closeNav');
	  $('.nav-toggle').toggleClass('active');
	});
});

/*Datepicker*/
$(function() {
	"use strict";
	let defaultLang = "ko";
	$(".S_date, .E_date").otherDatepicker({
		type: "date",
		format: "yyyy-MM-dd",	// 날짜 형식 지정
		lang : defaultLang,
		//endDate:new Date(), // 오늘을 기준으로 조회 막는 부분 주석처리.
		onChange : function ( event ) {	//달력이 닫히고 선택된 날짜를 넘겨줌
			const eleClass = $(this).attr("class");	// (검색 시작일 )클릭한 input text tag의 class를 가져온다.
			if(eleClass.indexOf("S_date") >= 0) {	// class 가 시작일 경우
				$(this).attr("value",event.newDate.split("-")[0]+"-"+event.newDate.split("-")[1]+"-"+event.newDate.split("-")[2]);
				$(this).closest('div').parent().find('.more_checkbox').prop("checked", true);
				$(".E_date").otherDatepicker('destroy');
				$(".E_date").otherDatepicker({
					type:"date",
					format:"yyyy-MM-dd",
					lang : defaultLang,
					startDate: new Date(event.newDate.split("-")[0], event.newDate.split("-")[1]-1, event.newDate.split("-")[2]),
				});
			}else{
				$(".S_date").otherDatepicker('destroy');
				$(this).attr("value",event.newDate.split("-")[0]+"-"+event.newDate.split("-")[1]+"-"+event.newDate.split("-")[2]);
				$(this).closest('div').parent().find('.more_checkbox').prop("checked", true);
				$(".S_date").otherDatepicker({
					type:"date",
					format:"yyyy-MM-dd",
					lang : defaultLang,
					endDate: new Date(event.newDate.split("-")[0],event.newDate.split("-")[1]-1,event.newDate.split("-")[2]),
				});
			}
		},
		onShow:function(){
			const eleClass = $(this).attr("class");	// (검색 시작일 )클릭한 input text tag의 class를 가져온다.
			if(eleClass.indexOf("S_date") >= 0) {	// class 가 시작일 경우
				$(".E_date").otherDatepicker('hide');
			}else{
				$(".S_date").otherDatepicker('hide');
			}
		}
	});
});

$(function() {
	"use strict";
	$(".S_date, .E_date, .startDatePicker").attr( 'readOnly' , 'true' );
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
/*$(document).ready(function(e) {		
	"use strict";
	try {
		var pages = $("#pages").msDropdown({on:{change:function(data, ui) {
												var val = data.value;
												if(val!="")
													window.location = val;
											}}}).data("dd");

		var pagename = document.location.pathname.toString();
		pagename = pagename.split("/");
		pages.setIndexByValue(pagename[pagename.length-1]);
		$("#ver").html(msBeautify.version.msDropdown);
	} catch(e) {
		//console.log(e);	
	}
	
	$("#ver").html(msBeautify.version.msDropdown);
		
	//convert
	$(".select").msDropdown({roundedBorder:false});
	createByJson();
	$("#tech").data("dd");
});*/






/* add search box*/
$( document ).ready(function() {
	"use strict";
$(".search_more_btn").click(function () { $(".add_search_info_box").toggle(); });

});

/* helf box*/
$( document ).ready(function() {
	"use strict";
$(".help_btn").click(function () { $(".add_help_info_box").toggle(); });

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
