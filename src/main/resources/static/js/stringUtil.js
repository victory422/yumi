/**
 * 문자열 공백 체크
 */
String.prototype.isEmpty = function(){
	var result = false;
	
	if(typeof this == 'undefined' || this.length < 1 || this == null || this == '' || this == "null"){
		result = true;
	}
	return result;
};

/**
 * e-mail check
 */
String.prototype.chkEmail = function() {
    var result = false;
    if (this == null || this == "" || typeof this == "undefined") {
        return result;
    }

    var is_valid = this.match(/^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/);
    if (is_valid != null) {
        result = true;
    }
    
    if(!result){
    	is_valid = this.match(/^[a-zA-Z0-9._%-]+@(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/);
    	if (is_valid != null) {
            result = true;
        }
    }
    return result;
};

/**
 * 문자열 전화번호 형식 체크 
 * @return (boolean) 
 */
String.prototype.chkTelNo = function() {
    var result = false;
    if (this == null || this == "" || typeof this == "undefined") {
        return result;
    }

    var is_valid = this.match(/^\d{2,3}-\d{3,4}-\d{4}$/);
    if (is_valid != null) {
        result = true;
    }
    return result;
};

/**
 * 문자열 핸드폰번호 형식 체크 
 * @return (boolean) 
 */
String.prototype.chkMobileNo = function() {
    var result = false;
    if (this == null || this == "" || typeof this == "undefined") {
        return result;
    }

    var is_valid = this.match(/^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/);
    if (is_valid != null) {
        result = true;
    }
    return result;
};

/**
 * 문자열 주민번호 형식 체크 
 * @return (boolean) 
 */
String.prototype.chkSSNNo = function() {	
    var result = false;
    if (this == null || this == "" || typeof this == "undefined") {
        return result;
    }
    
    var jumin = this.replace(/-/g, '');
    if (jumin.length != 13) return false;

    var tval = jumin.charAt(0)*2 + jumin.charAt(1)*3 + jumin.charAt(2)*4
    + jumin.charAt(3)*5 + jumin.charAt(4)*6 + jumin.charAt(5)*7
    + jumin.charAt(6)*8+ jumin.charAt(7)*9 + jumin.charAt(8)*2
    + jumin.charAt(9)*3 + jumin.charAt(10)*4 + jumin.charAt(11)*5;

    var tval2 = 11- (tval % 11);
    var tval2 = tval2 % 10;
    return (jumin.charAt(12) == tval2 && (jumin.charAt(6) == "1" || jumin.charAt(6) == "2"));
  
};

/**
 * 특수문자있는지 확인
 */
String.prototype.chkSpecialChar = function() {
    var result = false;
    if (this == null || this == "" || typeof this == "undefined") {
        return result;
    }

    var is_valid = this.match(/[\'\",]/);
    if (is_valid != null) {
        result = true;
    }
    return result;
};


/** String 관련 Util 정의 */
var StringUtils = {
	/**
	 * @param {Object} str 
	 * @param {Object} fiiLength padding문자열길이
	 * @param {Object} fillChar padding문자열
	 StringUtils.defaultIfEmpty(elem.targtName,'')
	 */
	lpad: function(str, fiiLength, fillChar){
		if (typeof str == 'undefined' || str == null || str == '') {
			return '';
		}else{
			return new String(str).lpad(fiiLength, fillChar);	
		}
	},
	onlyNumber: function(str){
		if (typeof str == 'undefined' || str == null || str == '') {
			return '';
		}
		return new String(str).onlyNumber();
	},
	isEmpty: function(str){
		if (typeof str == 'undefined' || str == null || str == '') {
			return true;
		}
		return new String(str).isEmpty();
	},
	defaultIfEmpty: function(str, defaultStr){
		if (typeof str == 'undefined' || str == null || str == '') {
			return defaultStr;
		}
		return str;
	},
	replace: function(stringObj, regExp, repalceText){
		if (typeof stringObj == 'undefined' || stringObj == null || stringObj == '') {
			return '';
		}else if(typeof stringObj != 'string'){
			stringObj = new String(stringObj); 
		}
		return stringObj.replace(regExp, repalceText);
	}
};