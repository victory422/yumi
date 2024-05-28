/**
 * Script info
 * 1. file path		: /static/js/views/codeDetailJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 공통코드 관리 스크립트 
 */
 
 function nullToEmptyString(val){
	if(val == null){
		val = '';
	}
	return val;
}

var cell2Id = "regDCodeCode";
var cell3Id = "regDCodeDesc";
var cell4Id = "regDCodeV01";
var cell5Id = "regDCodeV02";
var cell6Id = "regDCodeV03";
var cell7Id = "regDCodeV04";
var cell8Id = "regDCodeV05";
var cell9Id = "regDCodeA01";
var cell10Id = "regDCodeA02";
var cell11Id = "regDCodeA03";
var cell12Id = "regDCodeA04";
var cell13Id = "regDCodeA05";

// 상세코드 등록
function goDCodeRegistry(masterCode) {
	let requestData = {
		masterCode: masterCode,
		code: $("#" + cell2Id ).val(),
		description: $("#" + cell3Id ).val(),
		value01: $("#" + cell4Id ).val(),
		value02: $("#" + cell5Id ).val(),
		value03: $("#" + cell6Id ).val(),
		value04: $("#" + cell7Id ).val(),
		value05: $("#" + cell8Id ).val(),
		attribute001: $("#" + cell9Id ).val(),
		attribute002: $("#" + cell10Id ).val(),
		attribute003: $("#" + cell11Id ).val(),
		attribute004: $("#" + cell12Id ).val(),
		attribute005: $("#" + cell13Id ).val()
	};
	
	$.ajax({
		url: "/code/detail/register",
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
				
				$("#" + cell2Id ).val('');
				$("#" + cell3Id ).val('');
				$("#" + cell4Id ).val('');
				$("#" + cell5Id ).val('');
				$("#" + cell6Id ).val('');
				$("#" + cell7Id ).val('');
				$("#" + cell8Id ).val('');
				$("#" + cell9Id ).val('');
				$("#" + cell10Id ).val('');
				$("#" + cell11Id ).val('');
				$("#" + cell12Id ).val('');
				$("#" + cell13Id ).val('');
				
				var regBtn = document.getElementById(masterCode + "RegBtn");
				regBtn.setAttribute( "chk", "false" );
	
				var detailTr = document.getElementById(masterCode + "_detail");
				detailTr.textContent = '';
				
				detailTr.setAttribute( "chk", "false");
				openDetailList(masterCode);
			}
		}
	});
}

// 상세 코드 상태 업데이트
function goDCodeUpdateState(masterCode, code, status){
	var updateState = status == 'Y' ? 'N' : 'Y';
	
	let requestData = {
		masterCode: masterCode,
		code: code,
		enable: updateState
	}
	
	$.ajax({
		url: "/code/detail/updateSt",
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
				
				var detailTr = document.getElementById(masterCode + "_detail");
				detailTr.textContent = '';
				
				detailTr.setAttribute( "chk", "false");
				openDetailList(masterCode);
			}
		}
	});
}

// 상세 코드 등록 행 추가
function addRegistryRow(masterCode){
	
	var regBtn = document.getElementById(masterCode + "RegBtn");
	var isExist = regBtn.getAttribute( "chk" );
	
	if( isExist == "true"){
		alert("등록은 한번에 한 개만 가능합니다.");
	} else {
	
		var detailTableId = 'tableData_detail_' + masterCode;
		
		var table = document.getElementById(detailTableId).getElementsByTagName('tbody')[0];
		
		var newRow = table.insertRow(-1); // 새로운 행 추가
	    var cell1 = newRow.insertCell(0); // 새로운 행의 1 번째 셀 추가
	    var cell2 = newRow.insertCell(1); // 새로운 행의 2 번째 셀 추가
	    var cell3 = newRow.insertCell(2); // 새로운 행의 3 번째 셀 추가
		var cell4 = newRow.insertCell(3); // 새로운 행의 4 번째 셀 추가
	    var cell5 = newRow.insertCell(4); // 새로운 행의 5 번째 셀 추가
		var cell6 = newRow.insertCell(5); // 새로운 행의 6 번째 셀 추가
	    var cell7 = newRow.insertCell(6); // 새로운 행의 7 번째 셀 추가
		var cell8 = newRow.insertCell(7); // 새로운 행의 8 번째 셀 추가
	    var cell9 = newRow.insertCell(8); // 새로운 행의 9 번째 셀 추가
		var cell10 = newRow.insertCell(9); // 새로운 행의 10 번째 셀 추가
	    var cell11 = newRow.insertCell(10); // 새로운 행의 11 번째 셀 추가
		var cell12 = newRow.insertCell(11); // 새로운 행의 12 번째 셀 추가
	    var cell13 = newRow.insertCell(12); // 새로운 행의 13 번째 셀 추가
		var cell14 = newRow.insertCell(13); // 새로운 행의 14 번째 셀 추가
	
	    cell1.innerHTML = "<input type='checkbox'><label><span></span></label>"; // 1 번째 셀에 데이터 입력
	    cell2.innerHTML = "<input type='text' class='input_txt' id='" + cell2Id + "'>"; // 2 번째 셀에 데이터 입력
	    cell3.innerHTML = "<input type='text' class='input_txt' id='" + cell3Id + "'>"; // 3 번째 셀에 데이터 입력
		cell4.innerHTML = "<input type='text' class='input_txt' id='" + cell4Id + "'>"; // 4 번째 셀에 데이터 입력
	    cell5.innerHTML = "<input type='text' class='input_txt' id='" + cell5Id + "'>"; // 5 번째 셀에 데이터 입력
		cell6.innerHTML = "<input type='text' class='input_txt' id='" + cell6Id + "'>"; // 6 번째 셀에 데이터 입력
	    cell7.innerHTML = "<input type='text' class='input_txt' id='" + cell7Id + "'>"; // 7 번째 셀에 데이터 입력
		cell8.innerHTML = "<input type='text' class='input_txt' id='" + cell8Id + "'>"; // 8 번째 셀에 데이터 입력
	    cell9.innerHTML = "<input type='text' class='input_txt' id='" + cell9Id + "'>"; // 9 번째 셀에 데이터 입력
		cell10.innerHTML = "<input type='text' class='input_txt' id='" + cell10Id + "'>"; // 10 번째 셀에 데이터 입력
	    cell11.innerHTML = "<input type='text' class='input_txt' id='" + cell11Id + "'>"; // 11 번째 셀에 데이터 입력
		cell12.innerHTML = "<input type='text' class='input_txt' id='" + cell12Id + "'>"; // 12 번째 셀에 데이터 입력
	    cell13.innerHTML = "<input type='text' class='input_txt' id='" + cell13Id + "'>"; // 13 번째 셀에 데이터 입력
	    
	    var dCodeRegistryFunc = 'goDCodeRegistry("' + masterCode +'")';
		cell14.innerHTML = "<a href='javascript:;' onclick='" +dCodeRegistryFunc+ "'><span class='state_box'>확인</span></a>"; // 14 번째 셀에 데이터 입력
		
		regBtn.setAttribute( "chk", "true");
	}
}



// 상세 코드 목록 오픈
function openDetailList(masterCode){
	var colgroup = '<colgroup>'
		+ '<col style="width:5%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:7%;">'
		+ '<col style="width:9%;">'
		+ '</colgroup>';
		
	var thead = '<thead><tr><th class="list_chk_box"><input type="checkbox" id="01" name="tb_info" value="01" class="check_nor"><label for="01" class="check_label"><span></span></label></th><th>코드</th><th>설명</th><th>V01</th><th>V02</th><th>V03</th><th>V04</th><th>V05</th><th>A01</th><th>A02</th><th>A03</th><th>A04</th><th>A05</th><th>상태</th></tr></thead>';
		
	var addRowFunc = "addRegistryRow('" + masterCode +"')";
	var updateRowFunc = "changeUpdateMode('" + masterCode +"')";
	var deleteFunc = "goDeleteDCode('" + masterCode +"')";
	var detailListInfoArea = '<div class="con_sub_tit_wrap">'
		+ '<span class="con_detail_tit">상세 코드 목록</span>'
		+ '<span class="btn_area_detail">'
		+ '<a href="javascript:;" onclick="' + addRowFunc + '"><span class="width_100 btn_blk" id="' +masterCode+'RegBtn" chk="false">등록</span></a>'
		+ '<a href="javascript:;" onclick="' + updateRowFunc + '"><span class="width_100 btn_blk toggleButton">수정</span></a>'
		+ '<a href="javascript:;" onclick="' + deleteFunc + '"><span class="width_100 btn_blk">삭제</span></a>'
		+ '</span></div>';
	
	var detailTableId = 'tableData_detail_' + masterCode;
		
	var detailTr = document.getElementById(masterCode + "_detail");
	var isExist = detailTr.getAttribute( "chk" );
	
	
	if(isExist == "false"){
		$.ajax({
			type 		: 'get',
			url 		: '/code/detail/list',
			data 		: { masterCode : masterCode },
			traditional : true,
			success		: function(data){
				var tbody = '<tbody>';
				for( var i=0; i<data.length; i++ ){
					var codeDetail = data[i];
					var uuid = "_" + masterCode + "_" + codeDetail.code;
					var dCodeUpdateFunc = 'goDCodeUpdate("'  + masterCode + '", "' +codeDetail.code+ '")';
					var dCodeUpdateStatusFunc = 'goDCodeUpdateState("' + masterCode +'", "' + codeDetail.code + '", "' + codeDetail.enable + '")';
					
					var toggleTextClassName = "toggleText" + uuid;
					var toggleIuputClassName = "toggleInput" + uuid;
					
					var stateTd;
					if(codeDetail.enable == 'Y'){ 
						stateTd = "<td><div class='" + toggleTextClassName + "'><a href='javascript:;' onclick='" +dCodeUpdateStatusFunc+ "'><span class='state_box_Y'>사용가능</span></a></div>"
						+ "<div class='" + toggleIuputClassName + " hidden'><a href='javascript:;' onclick='" +dCodeUpdateFunc+ "'><span class='state_box'>수정</span></a></div></td>";
					} else { 
						stateTd = "<td><div class='" + toggleTextClassName + "'><a href='javascript:;' onclick='" +dCodeUpdateStatusFunc+ "'><span class='state_box_N'>사용불가</span></a></div>"
						+ "<div class='" + toggleIuputClassName + " hidden'><a href='javascript:;' onclick='" +dCodeUpdateFunc+ "'><span class='state_box'>수정</span></a></div></td>";
					}
					
					var checkBoxTd = '<td class="list_chk_box"><input type="checkbox" id="' + uuid + '" code="' + codeDetail.code + '" name="sub_chk" class="chk">' 
					+ '<label for="' + uuid + '" class="check_label"><span></span></label></td>';
					
					var tdHead = '<td><div class="textContainer"><div class="' + toggleTextClassName + '">';
					
					function tdTail(inputId, className){
						return '</div><input type="text" id="' + inputId + '" class="' + className + ' hidden input_txt"></div></td>';
					}
					
					var td = '<tr>'
					+ checkBoxTd 
					+ tdHead + nullToEmptyString(codeDetail.code) + tdTail("dCodeCode" + uuid , toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.description) + tdTail("dCodeDescription" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.value01) + tdTail("dCodeValue01" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.value02) + tdTail("dCodeValue02" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.value03) + tdTail("dCodeValue03" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.value04) + tdTail("dCodeValue04" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.value05) + tdTail("dCodeValue05" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.attribute001) + tdTail("dCodeAttr01" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.attribute002) + tdTail("dCodeAttr02" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.attribute003) + tdTail("dCodeAttr03" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.attribute004) + tdTail("dCodeAttr04" + uuid, toggleIuputClassName)
					+ tdHead + nullToEmptyString(codeDetail.attribute005) + tdTail("dCodeAttr05" + uuid, toggleIuputClassName)
					+ stateTd + '</tr>';
					tbody += td;
				}
				tbody += '</tbody>';
				detailTr.innerHTML
				='<td colspan="6"><div class="panel">' + detailListInfoArea
					+ '<div style="width:1500px; overflow-x: auto;">'
					+ '<div class="con_tb_area">'
					+ '<div class="con_menu_tb_wrap">'
						+'<table id="' + detailTableId + '" class="dt_tbl_detail">'
						+ colgroup + thead + tbody
					+ '</table></div></div></div></div></td>'
				;
			}
		});
		detailTr.setAttribute( "chk", "true" );
		
	} else {
		detailTr.textContent = '';
		detailTr.setAttribute( "chk", "false");
	}
	
}

function goDCodeUpdate(masterCode, code) {
	var uuid = "_" + masterCode + "_" + code;
	
	let requestData = {
		masterCode: masterCode,
		code: code,
		description: $("#dCodeDescription" + uuid ).val(),
		value01: $("#dCodeValue01" + uuid ).val(),
		value02: $("#dCodeValue02" + uuid ).val(),
		value03: $("#dCodeValue03" + uuid ).val(),
		value04: $("#dCodeValue04" + uuid ).val(),
		value05: $("#dCodeValue05" + uuid ).val(),
		attribute001: $("#dCodeAttr01" + uuid ).val(),
		attribute002: $("#dCodeAttr02" + uuid ).val(),
		attribute003: $("#dCodeAttr03" + uuid ).val(),
		attribute004: $("#dCodeAttr04" + uuid ).val(),
		attribute005: $("#dCodeAttr05" + uuid ).val()
	}
	
	$.ajax({
		url: "/code/detail/update",
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
				
				var detailTr = document.getElementById(masterCode + "_detail");
				detailTr.textContent = '';
				
				detailTr.setAttribute( "chk", "false");
				openDetailList(masterCode);
			}
		}
	});
	
}

function changeUpdateMode(){
	
	const query = 'input[name="sub_chk"]:checked';
    const selectedElements = document.querySelectorAll( query );
    
    // 체크박스 체크된 항목의 개수
    const selectedElementsCnt = selectedElements.length;

    if( selectedElementsCnt == 0 ){
    	alert("수정할 코드를 선택해주세요.");
    	return false;
    } else if(selectedElementsCnt > 1) {
    	alert("한 번에 하나의 코드만 수정 가능합니다.");
    	return false;
    } else {
    	var uuid = selectedElements[0].getAttribute( "id" );
    	
    	var toggleTextClassName = ".toggleText" + uuid;
    	var toggleIuputClassName = ".toggleInput" + uuid;
    	
        var toggleTexts = document.querySelectorAll(toggleTextClassName);
        var toggleInputs = document.querySelectorAll(toggleIuputClassName);

        toggleTexts.forEach(function(toggleText, index) {
            if (toggleText.classList.contains("hidden")) {
                toggleText.classList.remove("hidden");
                toggleInputs[index].classList.add("hidden");
            } else {
                toggleText.classList.add("hidden");
                toggleInputs[index].classList.remove("hidden");
                toggleInputs[index].value = toggleText.textContent.trim();
            }
        });
    }
    
}

function goDeleteDCode(masterCode){
	const query = 'input[name="sub_chk"]:checked';
    const selectedElements = document.querySelectorAll( query );
    
    // 체크박스 체크된 항목의 개수
    const selectedElementsCnt = selectedElements.length;
    
    if( selectedElementsCnt == 0 ){
    	alert("삭제할 코드를 선택해주세요.");
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
				arr[i] = masterCode + "-" + selectedElements[i].getAttribute( "code" );
			}
			
			$.ajax({
				type 		: 'delete',
				url 		: '/code/detail/delete',
				data 		: { idArr : arr },
				traditional : true,
				success		: function(data){
					if(data.status == "success"){
						alert(data.message);
						
						var detailTr = document.getElementById(masterCode + "_detail");
						detailTr.textContent = '';
						
						detailTr.setAttribute( "chk", "false");
						openDetailList(masterCode);
						
					} else {
						alertPopup(data.message);
					}
				}
			});
		}
    }
}