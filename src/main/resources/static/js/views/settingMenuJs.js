/**
 * Script info
 * 1. file path		: /static/js/views/menuJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 메뉴관리 스크립트 
 */
 
/* 조회 파라미터 */
let g_nowMenu = {};
let g_maxOrder = 0;

 /* 메뉴 목록 조회 */
let getSettingMenuList = function(page, fn_callback, paramData) {
	var param = {};
	
	if( typeof paramData != "undefined" ) {
		param = paramData;
	} else {
		param = g_nowMenu;
	}
	
	var menuCode = document.getElementById("thisMenuCode").value;
	
	if( !param.menuDepth ) param.menuDepth = document.getElementById("thisMenuDepth").value;
	
	var rows = false;
	if( page ) {
		param.page = page;
	}else {
		param.menuDepth = 2;
		rows = 999;
	}
	
	var url = "/menu/getSettingMenulist?menuDepth=";
	if( param.menuDepth ) url += param.menuDepth;
	if( param.menuCode ) url += "&menuCode=" + param.menuCode;
	if( param.isSearchDownMenu ) url += "&isSearchDownMenu=" + param.isSearchDownMenu; 
	if( param.menuUpperCode ) url += "&menuUpperCode=" + menuCode; 
	if( param.menuUse ) url += "&menuUse=" + param.menuUse; 
	if( page ) url += "&page=" + page; 
	if( rows ) url += "&rows=" + rows; 
	
	$.ajax({
		url: url,
		type: "get",
		async : false,
		contentType: "application/json; charset=UTF-8",
		success: function( responseData ){
			
			if( document.querySelector("#pagenation_area_squar") != null ) {
				convertId(document.querySelector("#pagenation_area_squar"));
				convertId = function() {};
			}
			
			if( typeof fn_callback === "function" ) {
				fn_callback(responseData.resultMap);
			} else {
				var tbody = document.getElementById("tbody");
				makeMenuTable(tbody, responseData.resultMap);
				makePaging(responseData.page, "page_menuList", "getSettingMenuList");
				g_maxOrder = responseData.maxOrder; 
			}
			
			if( Number(param.menuDepth) == 1 ) {
				document.getElementById("thisMenuName").innerText = "home"; 
				document.getElementById("thisMenuCode").value = "home";
				document.getElementById("thisMenuDepth").value =  "1";
			}
			
			
		}
	});
}


/* 화면 로드 후 이벤트 */
window.addEventListener("load", function(){
	g_nowMenu = {
		menuDepth : 1
		,menuUpperCode : "home"
		,menuCode : null
		,menuUse : null
		,isSearchDownMenu : null
	};
	document.getElementById("thisMenuName").innerText = "home"; 
	document.getElementById("thisMenuCode").value = "home";
	document.getElementById("thisMenuDepth").value =  "1";
	getSettingMenuList(1);
});


/* 메뉴 조회 후 테이블 그리기 */
let makeMenuTable = function(tbody, menus) {
	while ( tbody.hasChildNodes() ) {
		tbody.removeChild(tbody.firstChild);
	}
	
	if( menus != null ) {
		menus.forEach(function(menu){
			let tr = document.createElement("tr");
			let check = makeCheckBox(menu.menuCode, "chk_" );
			let code = document.createElement("td");
			let name = document.createElement("td");
			let order = document.createElement("td");
			let url = document.createElement("td");
			let isGetChild = document.createElement("td");
			let use = document.createElement("td");
			let attribute1 = document.createElement("td");
			var prefix = "td_";
			code.id = prefix + "menuCode";
			name.id = prefix + "menuName";
			order.id = prefix + "menuOrder";
			url.id = prefix + "menuUrl";
			isGetChild.id = prefix + "isGetChild";
			use.id = prefix + "menuUse";
			code.innerText = menu.menuCode;
			name.innerText = menu.menuName;
			order.innerText = menu.menuOrder;
			url.innerText = menu.menuUrl;
			isGetChild.innerText = Boolean(menu.isGetChild) ? "Y" : "N";
			use.innerText = menu.menuUse;
			attribute1.innerText = menu.menuAttribute1;
			if (tbody.id == "tbody" ) {
				tr.appendChild(check);
				tr.setAttribute("change-modal-id", "menuDetail");
				tr.addEventListener("click",function(event){
				    if( eventBtnFlag ) {
						event.stopPropagation(); // 이벤트 차단
						return false;
					}
					openMenuDetail(menu);
				});
			}
			tr.appendChild(code);
			tr.appendChild(name);
			tr.appendChild(order);
			tr.appendChild(url);
			tr.appendChild(use);
			if (tbody.id == "tbody" ) tr.appendChild(isGetChild);
			tr.appendChild(attribute1);
			tbody.appendChild(tr);
		});
	}
}


/* 메뉴순서 변경 */
let changeOrderStart = function(dom) {
	setEventPrevention(true, dom);
	dom.firstChild.innerText = "저장";
	dom.setAttribute("onclick","changeOrderUpdate()");
	
	document.querySelectorAll("#td_menuOrder").forEach(function(d){
		let div = document.createElement("div");
		div.setAttribute("style", "display: inline-grid;margin-left:30px;");
		
		let spanUp = document.createElement("span");
		spanUp.setAttribute("class", "nav_arrow");
		let spanDown = spanUp.cloneNode();
		
		spanUp.innerHTML = '<i class="la la-angle-up"></i>';
		spanDown.innerHTML = '<i class="la la-angle-down"></i>';
		spanUp.addEventListener("click",function(){
			 orderSlide(d.parentNode, "up");
		});
		spanDown.addEventListener("click",function(){
			 orderSlide(d.parentNode, "down");
		});
		
		div.appendChild(spanUp);
		div.appendChild(spanDown);
		d.setAttribute("style","display: flex;justify-content: center;align-items: center;");
		d.appendChild(div);
	});
	 
}


/* 메뉴순서 변경 이벤트 */
let orderSlide = function(parent, upDown) {
	var maxOrder = 0;
	var nowpage = Number(document.querySelector("#page_menuList_paging_pages > li > a.active").innerText);
	var beforeIndex = (nowpage-1) * 10 + 1;
	var orderArr = [];
	document.querySelectorAll("#tbody > tr").forEach(function() {
		orderArr.push(beforeIndex++);
	});
	maxOrder = Math.max(...orderArr);
	
	var tempOrder = 0;
	var logicNum = 0;	// 0 또는 1
	document.querySelectorAll("#tbody > tr").forEach(function(tr){
		if( logicNum == 0 ) {
			if( parent == tr ) {
				tempOrder = Number(tr.querySelector("#td_menuOrder").firstChild.data);
				if( upDown == "up" ) {
					if( tempOrder > 1 ) {
						tr.parentNode.insertBefore(tr, tr.previousElementSibling);
					}
				} else {
					if( tr.querySelector("#td_menuOrder").firstChild.data != maxOrder ) {
						tr.parentNode.insertBefore(tr.nextElementSibling, tr);
					}
				}
			}
			for(var i = 0 ; i < orderArr.length; i ++) {
				document.querySelectorAll("#tbody > tr > #td_menuOrder")[i].firstChild.data = orderArr[i]; 
			}
		} else if( logicNum == 1 ) {
			if( parent == tr ) {
				tempOrder = Number(tr.querySelector("#td_menuOrder").firstChild.data);
				if( upDown == "up" ) {
					if( tempOrder > 1 ) {
						tr.querySelector("#td_menuOrder").firstChild.data = --tempOrder;
					}
				} else {
					if( tr.querySelector("#td_menuOrder").firstChild.data != maxOrder ) {
						tr.querySelector("#td_menuOrder").firstChild.data = ++tempOrder;
					}
				}
			}
			orderArr.splice(orderArr.indexOf(tempOrder),1);
			document.querySelectorAll("#tbody > tr").forEach(function(tr){
				if( parent != tr ) {
					tr.querySelector("#td_menuOrder").firstChild.data = orderArr.shift(); 
				}
			});
		} 
	});
}


/* 메뉴순서 변경 update */
let changeOrderUpdate = function() {
	var dataArr = [];
	
	document.querySelectorAll("#tbody > tr").forEach(function(tr){
		var data = {};
		data.menuCode = tr.querySelector("#td_menuCode").innerText;
		data.menuOrder = tr.querySelector("#td_menuOrder").firstChild.data;
		dataArr.push(data);
	});

	$.ajax({
		url: "/menu/updateOrders",
		type: "put",
		data : JSON.stringify(dataArr),
		contentType: "application/json; charset=UTF-8",
		success: function( data ){
			if( data.status == "success" ) {
				successPopup(data.message);
			} else {
				alertPopup(data.message);
			}
		}
	});
}

/* 메뉴상세 팝업 */
let openMenuDetail = function(menu) {
	fn_modalOpen("menuDetail");
	
	document.querySelector("#menuDetail > div.modal_header > p").innerText = "메뉴 상세";
	
	setDetailButtons("update");
	
	document.getElementsByName("details").forEach(function(ipt){
		var id = ipt.id.replace("details_","");
		if( ipt.tagName.toLowerCase() == "select" ) {
			if( ipt.id == "details_menuUse" ) {
				setSelectBoxValue(ipt.id, menu.menuUse);
			}
		} else {
			ipt.value = menu[id];
		}
	});
	
	let paramData = {};
	paramData.menuCode = menu.menuCode;
	paramData.menuDepth = Number(menu.menuDepth) + 1;
	paramData.isSearchDownMenu = true;
	paramData.menuUse = "";
	
	if( Number(menu.menuDepth) != 1 ) {
		document.getElementById("detailLowerMenus").setAttribute("style","display:none;");
	}
	
	getSettingMenuList(false, function( menu ) {
		if( menu.length > 0 ) {
			document.getElementsByName("details_lowerDepth").forEach(function(dom){
				dom.setAttribute("style","display:block;");
			});
		}
		makeMenuTable(document.getElementById("details_tbody"), menu);
	}, paramData );	
}

/* 메뉴등록 */
let goRegist = function() {
	
	var data = getDatasByName("details");
	data.menuDepth = Number(data.menuDepth);
	if( Number(data.menuDepth) == 1 && (data.menuAttribute1 == "" || typeof data.menuAttribute1 == "undefined") ) {
		data.menuAttribute1 = "la";
	}
	
	if( !menuExcuteValidator(data) ) return false;

	$.ajax({
		url: "/menu/regist",
		type: "post",
		data : JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( data ){
			alertPopup(data.message);
			if( data.status == "success" ) {
				getSettingMenuList(1);
				document.getElementById("details_menuOrder").value = getMaxOrder() + 1;
			}
		}
	});
}

/* 메뉴 수정 */
let goUpdate = function() {
	var data = getDatasByName("details");
	if( Number(data.menuDepth) == 1 && (data.menuAttribute1 == "" || typeof data.menuAttribute1 == "undefined") ) {
		data.menuAttribute1 = "la";
	}
	
	data.menuDepth = "no update";
	if( !menuExcuteValidator(data) ) return false;
	data.menuDepth = document.getElementById("thisMenuDepth").value;
	
		$.ajax({
		url: "/menu/update",
		type: "put",
		data : JSON.stringify(data),
		contentType: "application/json; charset=UTF-8",
		success: function( data ){
			alertPopup(data.message);
			if( data.status == "success" ) {
				getSettingMenuList(1);
			}
		}
	});
}


/* 메뉴 상태 변경 */
let goUpdateSt = function() {
	var arr = getCheckedArray("chk_");
	if( !arr ) return;

	confirmPopup("선택한 메뉴의 상태를 변경하시겠습니까?");
	document.querySelector( ".js-modal-next" ).onclick = function(e){
		e.stopPropagation();
		$(".modal-box, .modal-overlay").fadeOut(function () {
			$(".modal-overlay").remove();
		       $("#confirmpopup").remove();
		       $("#confirmpopuparea").remove();
		});
		$(window).resize();
		
		$.ajax({
			type 		: 'put',
			url 		: '/menu/updateSt',
			data 		: { arr : arr },
			traditional : true,
			success		: function(data){
				alertPopup(data.message);
				if( data.status == "success" ) {
					getSettingMenuList(1);
				}
			}
		});
	}
}

/* 메뉴 삭제 */
let goDelete = function() {
	var arr = getCheckedArray("chk_");
	if( !arr ) return;
	confirmPopup("선택한 메뉴를 삭제하시겠습니까?");
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
			url 		: '/menu/delete',
			data 		: { arr : arr },
			traditional : true,
			success		: function(data){
				alertPopup(data.message);
				if( data.status == "success" ) {
					getSettingMenuList(1);
				}
			}
		});
	}
}

/* 메뉴 하위목록 조회 */
let searchLowerMenus = function() {
	let paramData = {};
	paramData = getDatasByName("details");
	paramData.isSearchDownMenu = true;
	paramData.menuUse = "";
	paramData.menuDepth = 2;
	paramData.menuUpperCode = paramData.menuCode;
	
	getSettingMenuList(1, {} , paramData);
	document.querySelector(".js-modal-close-change").click();
	g_nowMenu = paramData;
	document.getElementById("thisMenuName").innerText = g_nowMenu.menuName; 
	document.getElementById("thisMenuCode").value =  g_nowMenu.menuCode;
	document.getElementById("thisMenuDepth").value =  g_nowMenu.menuDepth;
}


/* 목록에서 체크된 항목 반환 */
let getCheckedArray = function( prefix ) {
	const query = 'input[name="chk"]:checked';
    const selectedElements = document.querySelectorAll( query );
    
    // 체크박스 체크된 항목의 개수
    const selectedElementsCnt = selectedElements.length;
    var arr = new Array( selectedElementsCnt );
    
    if( selectedElementsCnt == 0 ) {
		alertPopup( "목록을 선택해주세요." );
		return false;
	} else {
		for( var i=0; i<selectedElementsCnt; i++ ) {
			arr[i] = selectedElements[i].getAttribute( "id" ).replace(prefix, "" );
		}
	}
	return arr;
}


/* 메뉴등록 팝업 */
let openRegistPopup = function() {
	fn_modalOpen("menuDetail");
	document.querySelector("#menuDetail > div.modal_header > p").innerText = "메뉴 등록";
	document.getElementById("details_menuCode").removeAttribute("disabled");
	document.getElementById("details_menuOrder").value = getMaxOrder() + 1;
	document.getElementById("details_menuUpperCode").value = document.getElementById("thisMenuCode").value;
	document.getElementById("details_menuDepth").value = document.getElementById("thisMenuDepth").value;
	
	makeMenuTable(document.getElementById("details_tbody"), null);
	
	document.getElementsByName("details_lowerDepth").forEach(function(dom){
		dom.setAttribute("style","display:none;");
	});
	
	setDetailButtons("regist");
	
}

/* 작업 간 타 이벤트 중지 */
let eventBtnFlag = false;
let setEventPrevention = function(flag, dom) {
	if( flag ) {
		eventBtnFlag = true;	// row 클릭 시 이벤트 방지
		document.querySelectorAll(".btn_area_top > a").forEach(function(v){
			if( v.id != dom.id ) {
				v.setAttribute("style", "display:none;");
			}
		});
		document.getElementById("cancelBtn").setAttribute("style", "display:inline-block;");
	} else {
		eventBtnFlag = false;	// row 클릭 시 이벤트 방지
		document.querySelectorAll(".btn_area_top > a").forEach(function(v){
			if( v.id != dom.id ) {
				v.setAttribute("style", "display:inline-block;");
			}
		});
	}
}

/* 디테일화면 버튼목록 컨트롤 */
let setDetailButtons = function(val) {
	if( val == "regist" ) {
		document.getElementById("detailRegist").setAttribute("style","display:block;");
		document.getElementById("detailUpdate").setAttribute("style","display:none;");
		document.getElementById("detailLowerMenus").setAttribute("style","display:none;");
	} else if( val == "update" ) {
		document.getElementById("detailRegist").setAttribute("style","display:none;");
		document.getElementById("detailUpdate").setAttribute("style","display:block;");
		document.getElementById("detailLowerMenus").setAttribute("style","display:block;");
	}
}

/* order 최대값 구하기 */
let getMaxOrder = function() {
	return Number(g_maxOrder);
}

/* 메뉴 데이터 화면값 읽기 */
let getDatasByName = function(elementName) {
	var data = {};
	document.getElementsByName(elementName).forEach(function(ipt){
		var id = ipt.id.replace(elementName +"_","");
		data[id] = ipt.value ;
	});

	return data;
}

/* 메뉴등록 validation check */
let menuExcuteValidator = function(data) {
	if( typeof data == "undefined" ) {
		alertPopup("입력한 값이 올바르지 않습니다.");
		return false;
	}
	if( nullCheck(data.menuCode) == "" ) {
		alertPopup("분류코드를 입력해주세요.");
		return false;
	} else if( nullCheck(data.menuName) == "" ) {
		alertPopup("메뉴명을 입력해주세요.");
		return false;
	} else if( nullCheck(data.menuOrder) == "" ) {
		alertPopup("오류가 발생하였습니다.");
		return false;
	} else if( nullCheck(data.menuDepth) == "" ) {
		alertPopup("오류가 발생하였습니다.");
		return false;
	} else if( nullCheck(data.menuUrl) == "" ) {
		alertPopup("URL을 입력해주세요.");
		return false;
	} else if( nullCheck(data.menuUse) == "" ) {
		alertPopup("사용여부를 입력해주세요.");
		return false;
	} else if( nullCheck(data.menuUpperCode) == "" ) {
		alertPopup("상위코드를 입력해주세요.");
		return false;
	}
	return true;
}

/* 체크박스 만들기 */
let makeCheckBox = function( code , prefix ) {
	let check = document.createElement("td");
	check.setAttribute("class", "list_chk_box");
	check.setAttribute("onclick", "event.cancelBubble=true");
	let checkBox = document.createElement("input");
	checkBox.setAttribute("type","checkbox");
	checkBox.setAttribute("class","chk");
	checkBox.setAttribute("id", prefix + code );
	checkBox.setAttribute("name","chk");
	let label = document.createElement("label");
	label.setAttribute("for", prefix + code );
	let span = document.createElement("span");
	label.appendChild(span);
	check.appendChild(checkBox);
	check.appendChild(label);

	return check;
}
