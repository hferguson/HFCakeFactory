package ca.hferguson.spring;

import ca.hferguson.spring.bean.*;
import ca.hferguson.spring.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web
                    .configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Autowired
	private IBasket basket;
	
	@Bean
	public PasswordEncoder encoder() {
	  return new BCryptPasswordEncoder();
	}

	@Bean
	protected UserBasketFilter basketFilter() {
		return new UserBasketFilter(basket);
	}
	
	@Bean
	protected UserSessionFilter sessionFilter() {
		return new UserSessionFilter();
	}
	
	protected AuthenticationSuccessHandler oauth2SuccessHandler() {
		return new OAuth2SuccessHandler();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	    throws Exception {

	  auth
	    .userDetailsService(userDetailsService)
	    .passwordEncoder(encoder());

	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.httpBasic();
		http
	    //.addFilterAfter(basketFilter(), BasicAuthenticationFilter.class)
	    //.addFilterAfter(sessionFilter(), SessionManagementFilter.class)
	    .authorizeRequests()
			.antMatchers("/account/**")
			.hasAuthority("ROLE_USER")
			.antMatchers("/", "/**").permitAll()
			.and()
	      .formLogin()
	      .loginPage("/login")
	        .defaultSuccessUrl("/")
		.and()
		.oauth2Login()
		.loginPage("/login")
		.successHandler(oauth2SuccessHandler());
	}
}
