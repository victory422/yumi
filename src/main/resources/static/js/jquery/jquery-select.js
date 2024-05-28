/**
 * V1.0.1
 * </> with <3 by Adrien
 * https://github.com/kaddyadriano/jquery-styled-select-box
 */
var arrSelect = new Array();
var arrhtml = new Array();

(function ($, d) {
    initSelectbox = function () {
        $("select.styled").styledSelect();
        $(d).on("click", function (e) {
            var el = $(e.target),
                isSelect = el.closest('.styled-select').length;
            var selectStyled = el.closest('.styled-select');
            if(!isSelect){
                $('.styled-select .options').hide();
                $('.styled-select').removeClass('open');
            }
            for(var i = 0; i < arrhtml.length; i++){
            	var html = arrhtml[i];
            	if(selectStyled[0] != null && selectStyled[0].id != html[0].id) {
            		$('.options', html).hide();
            		html.removeClass('open');
            	}
            }
        });
    };

    $.fn.refresh_JS = function(param){
    	for(var i = 0; i < arrSelect.length; i++){
    		var targetSelect = arrSelect[i];
        	if(param['0'].nextSibling.value === targetSelect['0'].value){
                opt = $("option[value='"+targetSelect.val()+"']", targetSelect),
                optTitle = opt.html();
                $('.selected-display', param).html(optTitle);
                $('.option', targetSelect).removeClass('selected');
                $('.option[data-value="'+targetSelect.val()+'"]', param).addClass('selected');
                break;
        	}else{
        		continue;
        	}
    	}
    }
    
    $.fn.styledSelect = function () {

        if (!this.length) return false;
        
        this.each(function (k, select) {
            select = $(select);
            var val = select.val(),
                selectedTitle = $('option[value="' + val + '"]').html(),
                options = $("option", select),
                html = "";
            html += '<div class="styled-select"><span class="selected-display">' + selectedTitle + '</span><span class="arrow-wrap"></span></span>';
            html += '<div class="options">';
            options.each(function (k, opt) {
                opt = $(opt);
                html +=
                    '<div class="option' + (opt.attr("value") == val ? " selected" : "") + '" data-value="' + opt.attr("value") + '">' + opt.html() + "</div>";
            });
            html += "</div>";
            html += "</div>";
            // htmlSelect - 커스텀 셀렉트 박스를 클릭하면 드롭다운되는 option메뉴
            var htmlSelect = $(html);
            htmlSelect.css({ width: Number(parseInt(select.css("width")) + 10) + "px" });
            select.hide().before(htmlSelect);
            // 커스텀 셀렉트 옵션 태크 클릭시 발생하는 이벤트함수
            $(".option", htmlSelect).on("click", function (e) {
                var opt = $(this),
                    optVal = opt.attr("data-value"),
                    optTitle = opt.html(),
                    val = select.val();
                // 현재 클릭된 값이 이전에 클릭된 값과 다를 경우
                if (optVal != val) {
                    $(".selected-display", htmlSelect).html(optTitle);
                    select.val(optVal).trigger("change");
                    // 선택한 셑렉트박스가 암호키 종류일때
                    // 현재 보여지는 styled-select 박스를 제거하고
                    // 다시 박스를 그리도록 초기화
                    if (optVal === "symmetricKey" || optVal === "keyPair") {
                        $(".styled-select").remove();
                        initSelectbox();
                    }
                }
                $(".option", htmlSelect).removeClass("selected");
                opt.addClass("selected");
                toggleOptions(htmlSelect);
            });
            $(".selected-display, .arrow-wrap", htmlSelect).on("click", function (e) {
                toggleOptions(htmlSelect);
            });
            $(".selected-display.open").on("click", function (e) {
                $(".styled-select").removeClass("open");
                // toggleOptions(htmlSelect);
            });
            refresh = function () {
                var val = select.val(),
                    opt = $("option[value='" + val + "']", select),
                    optTitle = opt.html();
                $(".selected-display", htmlSelect).html(optTitle);
                $(".option", htmlSelect).removeClass("selected");
                $('.option[data-value="' + val + '"]', htmlSelect).addClass("selected");
            };
            // 셀렉트 옵션(보이는거) 클릭시 보이거나 안보이도록 하는 함수
            toggleOptions = function (htmlSelect) {
                if (htmlSelect.hasClass("open")) {
                    $(".options", htmlSelect).hide();
                    htmlSelect.removeClass("open");
                } else {
                    htmlSelect.addClass("open");
                    $(".options", htmlSelect).show();
                }
            };
            //events
            select.on("refresh", function (e) {
                refresh();
            });
        });
    };
})(jQuery, document);
