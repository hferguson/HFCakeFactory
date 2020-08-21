package ca.hferguson.spring;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import ca.hferguson.spring.misc.Account;

public class OAuth2Utils {
	public final static String ATTR_NAME = "name";
	public final static String ATTR_EMAIL = "email";
	
	private final static String USER_TOKEN_CLASS = UsernamePasswordAuthenticationToken.class.getSimpleName();
	private final static String ANON_TOKEN_CLASS = AnonymousAuthenticationToken.class.getSimpleName();
	private final static String OAUTH2_TOKEN_CLASS = OAuth2AuthenticationToken.class.getSimpleName();
	
	/**
	 * Create an account object from elements found in OAuth2 token
	 * @param token
	 * @return
	 */
	public static Account getAccount(OAuth2AuthenticationToken token) {
		Account user = null;
		String email = getAttribute(token, ATTR_EMAIL);
		String fullName = getAttribute(token, ATTR_NAME);
		String fName = fullName.split(" ")[0];
		String lName = fullName.split(" ")[1];
		user = new Account(email, fName, lName, "***");
		return user;
	}
	
	public static String getAttribute(OAuth2AuthenticationToken token, String attrName) {
		String value = token.getPrincipal().getAttribute(attrName);
		if (value == null)
			value = "";
		return value;
	}
	
	public static boolean isAnonToken(Authentication token) {
		boolean retVal = false;
		if (token != null)
			retVal = token.getClass().getSimpleName().equals(ANON_TOKEN_CLASS);
		return retVal;
	}
	public static boolean isOAuth2Token(Authentication token) {
		boolean retVal = false;
		if (token != null)
			retVal = token.getClass().getSimpleName().equals(OAUTH2_TOKEN_CLASS);
		return retVal;
	}
	
	public static boolean isUserToken(Authentication token) {
		boolean retVal = false;
		if (token != null)
			retVal =token.getClass().getSimpleName().equals(USER_TOKEN_CLASS);
		return retVal;
	}
}
