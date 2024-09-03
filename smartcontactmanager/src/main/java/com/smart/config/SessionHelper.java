package com.smart.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;
@Controller
public class SessionHelper {
	 public void removeMessageFromSession() {
		   try {
			 System.out.println("Removing the message from session");
			 HttpSession session=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			 session.removeAttribute("message");
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
	   }
}
