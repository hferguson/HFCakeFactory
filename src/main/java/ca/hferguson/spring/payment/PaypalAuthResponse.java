package ca.hferguson.spring.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
@Data
@RequiredArgsConstructor

public class PaypalAuthResponse {

	@JsonProperty("scope")
	private String scope;
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("app_id")
	private String appId;
	@JsonProperty("expires_in")
	private int expiresIn;
	// We will test removal of this
	@JsonProperty("nonce")
	private String nonce;
}
