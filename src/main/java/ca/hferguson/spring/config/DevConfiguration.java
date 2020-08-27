package ca.hferguson.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.paypal.core.PayPalEnvironment;

@Profile("dev")
@Component
public class DevConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(DevConfiguration.class);
	
	private PayPalEnvironment environment;
	@Value("${paypal.client-id}")
	String clientID;
	@Value("${paypal.client-secret}")
	String clientSecret;
	
	@Bean
	public PayPalEnvironment payPalEnv(){
		if (environment == null) {
		LOGGER.info("Paypal client ID {}", clientID);
			environment = new PayPalEnvironment.Sandbox(clientID, clientSecret);
		}
		LOGGER.info("Returning Paypal Sandbox Environment");
		return environment;
	}
	
}
