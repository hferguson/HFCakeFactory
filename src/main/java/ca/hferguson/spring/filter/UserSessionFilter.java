package ca.hferguson.spring.filter;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserSessionFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBasketFilter.class);
	private static String USER_TOKEN_CLASS = "UsernamePasswordAuthenticationToken";
	private static String ANON_TOKEN_CLASS = "AnonymousAuthenticationToken";
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

	  HttpServletRequest request = (HttpServletRequest) servletRequest;
	  HttpServletResponse response = (HttpServletResponse) servletResponse;
	  LOGGER.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

	  SecurityContext context = SecurityContextHolder.getContext();
	  boolean loginStatus = false;
	  String loginName = "";
	  if (context != null) {
		  Authentication token = context.getAuthentication();
		  LOGGER.info("Authentication is of type {}", authTokenType(token));
		  if (authTokenType(token).equals(ANON_TOKEN_CLASS)) 
			  loginName = "Guest";
		  else if (authTokenType(token).equals(USER_TOKEN_CLASS)) {
			  loginStatus = true;
			  loginName = getUsername(token);
		  }
	  }
	  request.setAttribute("email", loginName);
	  request.setAttribute("loginStatus", loginStatus);
	  LOGGER.info("Logged in as {}", loginName);
	  
	  //call next filter in the filter chain
	  filterChain.doFilter(request, response);
	  
	}
	
	private String getUsername(Authentication token) {
		return token.getName();
	}
	private String authTokenType(Authentication token) {
		return token.getClass().getSimpleName();
	}
}
