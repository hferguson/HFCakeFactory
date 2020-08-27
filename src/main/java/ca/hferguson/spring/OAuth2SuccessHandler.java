package ca.hferguson.spring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import ca.hferguson.spring.filter.UserBasketFilter;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBasketFilter.class);
	
	public final static String ATTR_NAME = "name";
	public final static String ATTR_EMAIL = "email";
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken oToken;
		LOGGER.info("OnAuthenticationSuccess called");
		if (authentication instanceof OAuth2AuthenticationToken) {
			oToken = (OAuth2AuthenticationToken)authentication;
			String fullName = oToken.getPrincipal().getAttribute(ATTR_NAME);
			LOGGER.info("Found OAuth token for user named {}", fullName);
		}
		// I will want to change this so I can handle different pages that have
		// a login component
		
		this.redirectStrategy.sendRedirect(request, response, "/");
	}
}