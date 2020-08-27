package ca.hferguson.spring.filter;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import ca.hferguson.spring.OAuth2Utils;
import ca.hferguson.spring.misc.Account;

@Component
public class UserSessionFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBasketFilter.class);
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

	  HttpServletRequest request = (HttpServletRequest) servletRequest;
	  HttpServletResponse response = (HttpServletResponse) servletResponse;
	  //LOGGER.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

	  SecurityContext context = SecurityContextHolder.getContext();
	  boolean loginStatus = false;
	  String loginName = "";
	  if (context != null) {
		  Authentication token = context.getAuthentication();
		  //LOGGER.info("Authentication token is of type {}", authTokenType(token));
		  if (OAuth2Utils.isAnonToken(token)) {
			  //LOGGER.info("Testing anon token");
			  loginName = "Guest";
		  } else if (OAuth2Utils.isUserToken(token)) {
			  //LOGGER.info("Testing User token");
			  loginStatus = true;
			  loginName = getUsername(token);
		  } else if (OAuth2Utils.isOAuth2Token(token)) {
			  //LOGGER.info("Testing OAuth2 token");
			  loginStatus = true;
			  Account user = OAuth2Utils.getAccount((OAuth2AuthenticationToken)token);
			  if (user != null) {
				  loginStatus = true;
				  loginName = user.getUid();
			  }
		  } else {
			  LOGGER.info("Unrecognized token {}", token != null ? token.getClass().getSimpleName(): " null token");
		  }
	  } else {
		  LOGGER.info("no context");
	  }
	  request.setAttribute("email", loginName);
	  request.setAttribute("loginStatus", loginStatus);
	  //LOGGER.info("Logged in as {}", loginName);
	  
	  //call next filter in the filter chain
	  filterChain.doFilter(request, response);
	  
	}
	
	private String getUsername(Authentication token) {
		return token.getName();
	}
}
