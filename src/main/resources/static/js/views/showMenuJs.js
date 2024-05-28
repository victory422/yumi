/**
 * Script info
 * 1. file path		: /static/js/views/showMenuJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 공통 메뉴목록 스크립트 
 */

/* 메뉴 목록 생성 */
let makeLeftNav = function(menus, callbackFn) {
	
	menus.forEach(function(menu){
		var li = document.createElement("li");
		var aTag = document.createElement("a");
		var span = document.createElement("span");
		if( menu.menuDepth == 1 ) {
			li.setAttribute("id", menu.menuCode);
			aTag.setAttribute("href", menu.menuUrl);
			aTag.innerHTML = '<span><i></i></span><span class="nav_txt"></span>';
			aTag.querySelector("span > i ").setAttribute("class", menu.menuAttribute1);
			//aTag.querySelector("span > i ").setAttribute("class", "la la-list-ul");
			aTag.querySelector(".nav_txt").innerText = menu.menuName;
			
			// 자식 노드가 없을 때
			if( menu.isGetChild == false ) {
				li.setAttribute("class", "noNewTab noDepth");
			
			// 자식 노드가 있을 때
			} else {
				span.setAttribute("class","nav_arrow");
				span.innerHTML = '<i class="la la-angle-right"></i>';
				aTag.appendChild(span);
			}
			
			li.appendChild(aTag);
			li.appendChild(document.createElement("ul"));
			document.querySelector("#lnb > ul").appendChild(li);
		
		// childNode setting
		} else if ( menu.menuDepth == 2 ) {
			var parent = document.getElementById(menu.menuUpperCode);
			li.setAttribute("class", "noDepth");
			li.setAttribute("id", menu.menuCode);
			aTag.setAttribute("href", menu.menuUrl);
			span.setAttribute("style", "white-space: nowrap");
			span.innerHTML = menu.menuName;
			aTag.appendChild(span);
			li.appendChild(aTag);
			parent.querySelector("ul").appendChild(li);
		}
		
	});
	
	if ( typeof callbackFn === "function" ) {
		callbackFn(menus);
		setHeaderMenuName(menus);
	}
}

// 좌측메뉴 이벤트
let addEventOnLeftNav = function(menus) {
	// left side event append 
	var script = document.createElement("script");
	script.setAttribute("src", "/js/accodion.js");
	document.body.append(script);
	
	// 현재 메뉴의 parent node side open 
    var link =  window.location.pathname;
	menus.forEach(function(menu){
		if( link === menu.menuUrl ) {
			if( menu.menuDepth != 1 ) {
				// side bar  depth 1 add event
				document.getElementById(menu.menuUpperCode).setAttribute("class","on");
				document.getElementById(menu.menuCode).setAttribute("class","noDepth on");
			}
		}
	});
}


/* 상단 메뉴 출력 */
let setHeaderMenuName = function(menus) {
	var link =  window.location.pathname;
	var pageNameDom = document.getElementById("brand_text02");
    var homeName = "";

	if( pageNameDom.innerHTML != "" ) return;
	for( var i = 0 ; i < menus.length ; i++ ) {
		var menu = menus[i];
		// 하위메뉴 헤더 set
		if( menu.menuAttribute2 == "home" ) homeName = menu.menuName ;
		if( link == menu.menuUrl ) {
			pageNameDom.innerHTML = menu.menuName ;
			break;
		}
	}
	
	var subNameDom = document.getElementById("brand_text01");
	if (link.indexOf('list') != -1) {
		subNameDom.innerHTML='목록';
	} else if ( link == "/login" || link == "/loginForm" ) {
		// 로그인화면일 때 pass
	} else if (link.indexOf('update') != -1) {
		subNameDom.innerHTML='상세';
	} else if (link.indexOf('register') != -1) {
		subNameDom.innerHTML='등록';
	} else if (link.indexOf('') != -1 || link == "/" ) {
		subNameDom.innerHTML = homeName;
	}
	
}
