/**
 * Script info
 * 1. file path		: /static/js/views/auditJs.js
 * 2. project name	: yumi korea Admin
 * 3. description	: 감사 로그 스크립트 
 */
 

/* 상세 조회 팝업*/
function goAdminDetail(webaudit){
	$("#detWebAuditNo").text(webaudit.adminId);
	$("#detWebAuditRegDate").text(setDateFormat(webaudit.regDate));
	$("#detWebAuditCommand").text(webaudit.url);
	$("#detWebAuditResult").text(webaudit.result);
	$("#detWebAuditRegId").text(webaudit.adminId);
	$("#detWebAuditRegName").text(webaudit.adminName);
	$("#detWebAuditAdminIp").text(webaudit.adminIp);
	$("#detWebAuditReqMsg").text(nullCheck(webaudit.reqMsg));
	$("#detWebAuditErrMsg").text(nullCheck(webaudit.errMsg));
}

function goList(page){
	let srcRegId = document.querySelector("#srcRegId").value;
	let srcRegIp = document.querySelector("#srcRegIp").value;
	let srcCommand = document.querySelector("#srcCommand").value;
	let srcResult = document.querySelector("#srcResult").value;
	let srcFrom = document.querySelector("#srcFrom").value;
	let srcTo = document.querySelector("#srcTo").value;
	let queryStr = "";
	
	if( (srcRegId != null && srcRegId != "") ||
		(srcRegIp != null && srcRegIp != "") ||
		(srcCommand != null && srcCommand != "") ||
		(srcResult != null && srcResult != "") ||
		(srcFrom != null && srcFrom != "") ||
		(srcTo != null && srcTo != "")
	 ) {
		 queryStr += "&srcRegId=" + srcRegId + "&srcRegIp=" + srcRegIp +  "&srcCommand=" + srcCommand;
		 queryStr += "&srcResult=" + srcResult + "&srcFrom=" + srcFrom + "&srcTo=" + srcTo;
	 }
	
	let uri = '/audit/admin/list?page=' + page;
	
	location.href= uri + queryStr ;
}

/* 모달 팝업 */
$(document).on("click", ".js-modal-next", function(e) {
	e.stopPropagation();
	$(".modal-box, .modal-overlay").fadeOut(function() {
		$(".modal-overlay").remove();
		$("#confirmpopup").remove();
		$("#confirmpopuparea").remove();
	});
	$(window).resize();
	location.href = "/manager/delete?managerIds=" + $("#managerId").val();
});


$(document).ready(function() {
     // modal popup
     $(document).on("click", "a[change-modal-id], tr[change-modal-id]", function(){
         const popupHidden =  "<div id='auditPoparea' name='auditPoparea' class='js-open-modal btn'></div>"
         $("body").append(popupHidden);
         const appendthis =  ("<div class='change-modal-overlay js-modal-close-change'></div>");
         //e.preventDefault();
         $("body").append(appendthis);
         $(".change-modal-overlay").fadeTo(100, 0.7);
         const modalBox = $(this).attr('change-modal-id');
         $('#'+modalBox).fadeIn($(this).data());

         $(".modal-box-change").css({
             top: ($(window).height() - $(".modal-box-change").outerHeight()) / 2,
             left: ($(window).width() - $(".modal-box-change").outerWidth()) / 2
         });
     });
     
     // modal popup - close
     $(document).on("click", ".js-modal-close-change", function (e) {
         e.stopPropagation();
         $("#adminAuditDetail").css("display","none");
         $("auditPoparea").remove();
     });

 });

