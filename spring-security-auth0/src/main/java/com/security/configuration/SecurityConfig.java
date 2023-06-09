package com.security.configuration;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.security.util.AuthConstant;




@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private ImpersonateFilter impersonateFilter;
	
	@Bean
	public JwtDecoder jwtDecoder() {

		OAuth2TokenValidator<Jwt> audienceValidator =new AudienceValidator(
				env.getProperty(AuthConstant.SPA_CLIENT_ID));
		OAuth2TokenValidator<Jwt> withIssuer = JwtValidators
				.createDefaultWithIssuer(env.getProperty(AuthConstant.ISSUER_URL));
		OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

		NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders
				.fromOidcIssuerLocation(env.getProperty(AuthConstant.ISSUER_URL));
		jwtDecoder.setJwtValidator(withAudience);
 System.out.println("inside..."+jwtDecoder);
		return jwtDecoder;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().cors().and().authorizeRequests().mvcMatchers("/*").permitAll().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors().and()
				.oauth2ResourceServer().jwt();
	}

	
	@Bean
	public FilterRegistrationBean someFilterRegistration() {

	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(impersonateFilter);
	    registration.addUrlPatterns("/*");
//	    registration.addInitParameter("paramName", "paramValue");
	    registration.setName("ImpersonateFilter");
	    registration.setOrder(100);
	    return registration;
	}
}
