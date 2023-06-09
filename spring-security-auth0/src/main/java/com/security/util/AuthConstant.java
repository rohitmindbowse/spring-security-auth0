package com.security.util;

public class AuthConstant {

	private AuthConstant() {
		throw new IllegalStateException("Constant class.can't instatiate");
	}

	// AUTH0
	public static final String AUTH0_DOMAIN = "auth0.domain";
	public static final String AUTH0_ID = "auth0.client.id";
	public static final String AUTH0_SECRET = "auth0.client.secret";
	public static final String AUTH0_AUDIENCE = "auth0.audience";
	public static final String ISSUER_URL = "spring.security.oauth2.resourceserver.jwt.issuer-uri";
	public static final String ACCESS_TOKEN_GRANT_TYPE = "access.token.grant.type";
	public static final String SPA_CLIENT_ID = "spa.client.id";

	public static final String AUTH0_PROVIDER_NAME = "auth0";
	public static final String GOOGLE_PROVIDER_NAME = "google-oauth2";
	public static final String LINKEDIN_PROVIDER_NAME = "linkedin";
	public static final String ID_SEPERATOR = "|";
	
}