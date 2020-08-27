package ca.hferguson.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.paypal.core.PayPalEnvironment;

@Profile("prod")
@Component
public class ProdConfiguration  {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProdConfiguration.class);
	
	private PayPalEnvironment environment;
	@Value("${paypal.client-id}")
	String clientID;
	@Value("${paypal.client-secret}")
	String clientSecret;
	
	@Bean
	public PayPalEnvironment payPalEnv() {
		if (environment == null) {
			LOGGER.info("Paypal client ID {}", clientID);
			environment = new PayPalEnvironment.Live(clientID, clientSecret);
			LOGGER.info("Returning Paypal Live Environment");
		}
		return environment;
	}
}
