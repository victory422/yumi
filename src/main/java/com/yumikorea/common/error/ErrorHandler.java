package com.yumikorea.common.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.utils.CommonUtil;

@Controller
@RequestMapping("/error")
public class ErrorHandler implements ErrorController {
	
//	private final Logger logger = LoggerFactory.getLogger( ErrorHandler.class );
	@RequestMapping
	public String handleError(HttpServletRequest request, HttpServletResponse response) {
		
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String message = "";
		String page = "";
		Boolean redirect = false;
		
		if(status != null) {
			int errCode = Integer.valueOf(status.toString());
			
			// 404인 경우
			if(errCode == HttpStatus.NOT_FOUND.value()) {
				message = "404 error : the web server cannot find a resource.";
				page = "error/404";
				redirect = true;
			} else if(errCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				message = "500error : the server encountered an unexpected condition that prevented it from fulfilling the request.";
				page = "error/error";
				redirect = true;
			} else {
				page = "error/error";
			}
		}
		
		if( redirect ) {
			if( !this.isRequestFromAjax(request) ) {
				this.doRedirect(response, message);
				return page;
			} else {
				try {
					request.getRequestDispatcher("/error/ajax").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return page;
	}
	
	@RequestMapping("/ajax")
	public ResponseEntity<?> ajaxErrorhandler(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> rstMap = new HashMap<>();
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		Object message = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//		readAttributes(request);
		rstMap.put("status", status);
		rstMap.put("message", message);

		return new ResponseEntity<Map<String, Object>>(rstMap, HttpStatus.valueOf(500));
	}
	
	@GetMapping("/invalidSession")
	public String invalidSession(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("REQUIRES_AUTH", "2");
		return "common/invalidSession";
	}
	
	@RequestMapping("/invalidAuthority")
	public ResponseEntity<Map<String,String>> invalidAuthority(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("REQUIRES_AUTH", "3");
		
		String message = (String) request.getSession().getAttribute(EAdminConstants.MESSAGE.getValue());
		if( CommonUtil.isNull(message) ) {
			message = EAdminMessages.NOT_PERMISSION_URL.getMessage(); 
		} else {
			request.getSession().setAttribute(EAdminConstants.MESSAGE.getValue(), "");
		}
		if( !this.isRequestFromAjax(request) ) this.doRedirect(response, message);
		
		Map<String,String> map = new HashMap<>();
		map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
		map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.NOT_PERMISSION_URL.getMessage() 
//		 + "<br/> URI : " + request.getAttribute(AdminConstants.REQUESTED_URI)
//		 + "<br/> method : " + request.getMethod()
		 + "\r\n URI : " + request.getAttribute(EAdminConstants.REQUESTED_URI.getValue())
		 + "\r\n method : " + request.getMethod()
				);
		request.removeAttribute(EAdminConstants.REQUESTED_URI.getValue());
		return ResponseEntity.ok(map);
	}
	
	
	private void doRedirect(HttpServletResponse response, String message) {
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html; charset=UTF-8");
			out.print("<script>alert('" + message + "'); history.go(-1);</script>");
			out.flush();
			out.close();
			response.flushBuffer();
		}catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private Boolean isRequestFromAjax(HttpServletRequest request) {
		String header = request.getHeader("x-requested-with");
		if( "XMLHttpRequest".equals(header) ) {
			return true;
		}else {
			return false;
		}
	}
	
	
//	private void readAttributes(HttpServletRequest request) {
//		Enumeration<String> params = request.getAttributeNames();
//		int cnt = 0;
//		String queryString = "";
//		while (params.hasMoreElements()){
//		    String name = (String)params.nextElement();
//		    System.out.println("name : " + name + ", val : " + request.getAttribute(name));
//		}
//	}
	
}
