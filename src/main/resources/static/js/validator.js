var isUTF8 = true;
var hanByte = 2;
if(isUTF8){
	hanByte = 3;
}


/* require class가 적용되어 있는 경우 값이 있는지 검사 */
function checkRequire(){
	var result = true;
	$(".require").each(function(){
		if($(this)[0].tagName && $(this)[0].tagName.toLowerCase() == "select"){
			if(!$(this).val()){
				alert($(this).attr("title") + "을(를) 선택하세요.");
				$(this).focus();			
				result = false;			
				return false;
			}
			
			if($(this).children("option:selected").val().isEmpty() ){
				alert($(this).attr("title") + "을(를) 선택하세요.");
				$(this).focus();			
				result = false;			
				return false;
			}
		}
		else if($(this)[0].tagName && $(this)[0].tagName.toLowerCase() == "textarea"){
			if($(this).val().isEmpty() ){
				alert($(this).attr("title") + "을(를) 입력하세요.");
				$(this).focus();			
				result = false;			
				return false;
			}
		}
		else if($(this).attr("type") && ( $(this).attr("type").toLowerCase() == "text" || $(this).attr("type").toLowerCase() == "password")){
			if($(this).val().isEmpty()){
				if($(this).attr("title") ){
					alert($(this).attr("title") + "을(를) 입력하세요.");
				}
				else{
					alert($(this).attr("name") + "을(를) 입력하세요.");
				}
				$(this).focus();			
				result = false;			
				return false;
			}
		}
	});		
	return result;
}

/**
 * 이메일 형식에 맞느지 검사
 * @returns
 */
function checkEmail(){
	var result = true;
	$('.email').each(function(){ 
		if($(this).val().isEmpty()){
			return true;//continue;
		}
		if(!$(this).val().chkEmail()){
			if($(this).attr("title")){
				alert($(this).attr("title") + "을(를) 확인하세요.");
			}
			else{
				alert($(this).attr("name") + "을(를) 확인하세요.");
			}
			$(this).focus();
			result = false;
			return false;//break;
		}
	});	
	
	return result;
}

/**
 * check tel no
 * @returns
 */
function checkTelNo(){
	var result = true;
	$('.telNo').each(function(){ 
		if($(this).val().isEmpty()){
			return true;//continue;
		}
		if(!$(this).val().chkTelNo()){
			if($(this).attr("title")){
				alert($(this).attr("title") + "을(를) 확인하세요.");
			}
			else{
				alert($(this).attr("name") + "을(를) 확인하세요.");
			}
			$(this).focus();
			result = false;
			return false;//break;
		}
	});	
	
	return result;
}

/**
 * 핸드폰번호 체크
 * @returns
 */
function checkMobileTelNo(){
	var result = true;
	$('.mobileTelNo').each(function(){ 
		if($(this).val().isEmpty()){
			return true;//continue;
		}
		if(!$(this).val().chkMobileNo()){
			if($(this).attr("title")){
				alert($(this).attr("title") + "을(를) 확인하세요.");
			}
			else{
				alert($(this).attr("name") + "을(를) 확인하세요.");
			}
			$(this).focus();
			result = false;
			return false;//break;
		}
	});	
	
	return result;
}


/**
 * number, date를 제외한 input 태그의 maxlength 설정되어 있는지 검사
 * @returns {Boolean}
 */
function checkMaxlengthOnInput(){
	var result = true;
	$('input[type="text"]').each(function(){
		if( !($(this).attr("class"))  
				|| ($(this).attr("class")						
						&& $(this).attr("class").indexOf("editable") < 0 
						&& $(this).attr("class").indexOf("number") < 0
						&& $(this).attr("class").indexOf("S_date") < 0 
						&& $(this).attr("class").indexOf("E_date") < 0 
						&& $(this).attr("class").indexOf("noCheckMaxLen") < 0 ) ){		
			
			if( !$(this).attr("maxlength") ){
				alert($(this).attr("name") + "의 maxlength를 설정하세요.");
				$(this).focus();			
				result = false;			
				return false;
			}			
		}
	});	
	
	return result;
}

/**
 * textarea의 maxlength 설정되어 있는지 검사, maxlength값에 따라 입력한 데이타의 길이 검사 
 * @returns {Boolean}
 */
function checkMaxLengthOnTextarea(){
	var result = true;
	var length = 0;
	$('textarea').each(function(){
		if( !$(this).attr("maxlength") ){
			if(lang == 'korea'){
				alert($(this).attr("name") + "의 maxlength를 설정하세요.");
			}else if(lang == 'english'){
				alert("Please set maxlength of "+$(this).attr("name")+".");
			}
			$(this).focus();			
			result = false;			
			return false;
		}		
		if( !($(this).val().isEmpty()) ){				
			/*if(isUTF8){
				length = $(this).val().bytesUTF8();
			}
			else{
				length = $(this).val().bytes();
			}*/				
			if($(this).attr("maxlength") < length ){
				alert( $(this).attr("maxlength")+"까지 입력 가능"  );
				$(this).focus();			
				result = false;			
				return false;
			}
		}		
	});	
	
	return result;
}


/**
 * input text, textarea의 특수문자 제거
 */
function removeFormat(){
	$('input[type="text"], textarea').each(function(){
		if($(this).val()){
			if($(this).attr("class") && $(this).attr('class').indexOf("numberFloat") >= 0){
				$(this).val( $(this).val().replace(/,/g, '') );	
			}			
			else if($(this).attr("class") && $(this).attr('class').indexOf("numberInt") >= 0){			
				$(this).val( $(this).val().replace(/,/g, '') );					
			}
		}
		else{
			if( $(this).attr('class') != undefined ){
				if($(this).val() == "" && ($(this).attr('class').indexOf("numberFloat") >= 0 || $(this).attr('class').indexOf("numberInt") >= 0 ) ){					
					$(this).val("0");
				}
			}		
		}
	});
}

function checkValidator(){
	if(!checkRequire()){
		return false;
	}
	
	/*if(!checkEmail()){
		return false;
	}
	if(!checkTelNo()){
		return false;
	}
	if(!checkMobileTelNo()){
		return false;
	}
	if(!checkSSN()){
		return false;
	}
	
	if(!checkMaxlengthOnInput()){
		return false;
	}
	
	if(!checkMaxLengthOnTextarea()){
		return false;
	}*/
	
	removeFormat();
	
	return true;
}


// 일자검색  validation
window.addEventListener('DOMContentLoaded', function() {
	// 조회조건으로 들어오는 등록일자 두 개 
	let iptDate = document.querySelectorAll("form .sch_tbl_input_date input");
	if( iptDate && iptDate.length == 2 ) {
		for( var i = 0 ; i < iptDate.length; i++) {
			iptDate[i].setAttribute("maxlength", 10);
			
			if( !isDate(iptDate[i].value) ) {
				// 조회버튼 비활성화
//				disabledSearch(true);
			}
			
			iptDate[i].addEventListener("keyup", function(e){
				this.value = inputDateValidation(this.value);
				this.value = this.value.replaceAll("-","");
				if( this.value.length == 8 ) {
					if( this.getAttribute("class").indexOf("E_date_month") > -1 ) {
						checkParamDate(iptDate, 1);
					} else {
						checkParamDate(iptDate, 0);
					}
				} else if ( this.value.length  > 8 ) {
					this.value = this.value.substring(0,8);
				}
			});
			iptDate[i].addEventListener("focus", function(e){
				this.value = this.value.replaceAll("-","");
				this.value = this.value.replaceAll(" ","");
			});
//			iptDate[i].addEventListener("focusout", function(e){
//				if( this.value == "" ) {
//					if( this.getAttribute("class").indexOf("E_date_month") > -1 ) {
//						this.value = iptDate[0].value;
//					} else {
//						this.value = iptDate[1].value;
//					}
//				}
//				
//				// 두 날짜가 모두 빈 값일 경우 (보통은 그런 경우가 없음)
//				if( this.value == "" ) iptDate[0].value = iptDate[1].value = (new Date()).toISOString().substring(0,10);
//				
//				var val = this.value.replaceAll("-","");
//				val = val.substr(0,4) + "-" + val.substr(4,2) + "-" + val.substr(6,2);
//				if( isDate(val) ) {
//					// 조회버튼 활성화
//					disabledSearch(false);
//					this.value = val;
//				} else {
//					alertPopup("날짜형식이 어긋납니다.");
//				}
//			});	
		}
	}
	
});

// validation 체크 전에 enter로 조회 막도록 처리
let disabledSearch = function(bool) {
	let dom = document.querySelector("form > .searchWrap > .btn_area > button");
	if ( bool ) {
		dom.setAttribute("disabled", "disabled");
	} else {
		dom.removeAttribute("disabled");
	}
}


let checkParamDate = function(iptDate, num) {
	disabledSearch(true);
	var fromDt = toDate(iptDate[0].value);
	var toDt = toDate(iptDate[1].value);
	
	if ( iptDate[0].value == "" || iptDate[1].value == "" ) {
		disabledSearch(false);
		return false;
	}
	if( !isDate(fromDt) ) {
		alertPopup("날짜형식이 어긋납니다.");
		iptDate[0].value = "";
		disabledSearch(false);
		return false;
	}
	
	if( !isDate(toDt) ) {
		alertPopup("날짜형식이 어긋납니다.");
		iptDate[1].value = "";
		disabledSearch(false);
		return false;
	}
	
	if ( fromDt != "" && toDt != "" && fromDt > toDt ) {
		alertPopup("from날짜는 to날짜보다 클 수 없습니다.");
		fromDt = toDt;
	}
	
//	if ( addMonthDate(new Date(toDt), -1 ) > new Date(fromDt) ) {
//		alertPopup("조회날짜는 한 달 이상이 될 수 없습니다.");
//		if( num == 0 ) {	// 일자 중 from일자를 수정했을 때
//			toDt = addMonthDate(new Date(fromDt), 1 );
//		} else {
//			fromDt = addMonthDate(new Date(toDt), -1 );
//		}
//	}
	
	if( fromDt != "" && toDt == "" ) {
		toDt = fromDt;
	}
	
	if( toDt != "" && fromDt == "" ) {
		fromDt = toDt;
	}
	
	// 날짜가 하루 줄어들어서 표기됨.. 처리 필요
	fromDt = new Date(fromDt.setDate(fromDt.getDate()+1));
	toDt = new Date(toDt.setDate(toDt.getDate()+1));
	iptDate[0].value = fromDt.toISOString().substring(0,10);
	iptDate[1].value = toDt.toISOString().substring(0,10);
	disabledSearch(false);
}

function isDate(date) {
	if( date != "" ) {
		let nDate = new Date(date);
		
		if( nDate == "Invalid Date" ) {
			return false;
		}
		if( date.length > 10 ) {
			return false;
		}
	} else {
		return false;
	}
	return true;
}

// yyyymmdd를 Date로 받기
function toDate(date) {
	var datetmp = date.replace(/-/g,'');			// - 는 모두 제거
     
	var y = parseInt(datetmp.substr(0, 4));
	var m = parseInt(datetmp.substr(4, 2));
	var d = parseInt(datetmp.substr(6, 2));
  
	return new Date(y, m-1, d);
}


function isValidEmail(email, dom) {
    // 정규식 패턴: 이메일 주소 형식
    var regex = /^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    var rst = regex.test(email);
    
    if( dom != null ) {
	    if( rst ) {
			$('#' + dom.id).css("background-color","rgba(0, 245, 75, 0.22)");
		} else {
			$('#' + dom.id).css("background-color","rgba(245, 0, 0, 0.22)");
		} 
	}
	return rst;
}


function isValidPhoneNumber(phoneNumber, dom) {
	if( phoneNumber == "" ) return true;
    // 정규식 패턴: 숫자 0-9, 대시(-)
    var regex = /^[\d\-\s]{10,20}$/;
    var rst = regex.test(phoneNumber);
    
    if( dom != null ) {
	    if( rst ) {
			$('#' + dom.id).css("background-color","rgba(0, 245, 75, 0.22)");
		} else {
			$('#' + dom.id).css("background-color","rgba(245, 0, 0, 0.22)");
		} 
	}
	return rst;    
}


function passwordsEqual(password1, password2) {
    return password1 === password2;
}

function inputPhoneNumberValidation(inputValue) {
    // Remove any non-digit characters using a regular expression
    let cleanedValue = inputValue.replace(/\D/g, '');

    // Format the cleaned value as XXX-XXX-XXXX
    let formattedValue = cleanedValue.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');

    // Update the input value with the formatted value
    return formattedValue;
}

function inputDateValidation(inputValue) {
    // Remove any non-digit characters using a regular expression
    let cleanedValue = inputValue.replace(/\D/g, '');

    // Format the cleaned value as XXX-XXX-XXXX
    let formattedValue = cleanedValue.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');

    // Update the input value with the formatted value
    return formattedValue;
}


function isNull(val) {
	let result = false;
	
	if( typeof val == "undefined" || val == "" ) {
		result = true;
	}
	
	return result;
}