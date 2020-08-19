package ca.hferguson.spring;

import ca.hferguson.spring.bean.*;
import ca.hferguson.spring.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web
                        .configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web
                    .configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private IBasket basket;
	
	@Bean
	public PasswordEncoder encoder() {
	  return new BCryptPasswordEncoder();
	}

	protected UserBasketFilter basketFilter() {
		return new UserBasketFilter(basket);
	}
	
	protected UserSessionFilter sessionFilter() {
		return new UserSessionFilter();
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
		http
	    .addFilterAfter(basketFilter(), BasicAuthenticationFilter.class)
	    .addFilterAfter(sessionFilter(), SessionManagementFilter.class)
	    .authorizeRequests()
			.antMatchers("/account/**")
			.hasAuthority("ROLE_USER")
			.antMatchers("/", "/**").permitAll()
			.and()
	      .formLogin()
	        .loginPage("/login")
	        .defaultSuccessUrl("/");
		 
	}
}
