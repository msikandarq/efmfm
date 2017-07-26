package com.newtglobal.eFmFmFleet.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.access.AccessDeniedHandlerImpl;

public class AccessDeniedHandlerApp extends AccessDeniedHandlerImpl {
	
	
	 public void handle(HttpServletRequest _request, HttpServletResponse _response, org.springframework.security.access.AccessDeniedException _exception) throws IOException, ServletException {
		 setErrorPage("/accessdenied");  // this is a standard Spring MVC Controller
         super.handle(_request, _response, _exception);
}

}
