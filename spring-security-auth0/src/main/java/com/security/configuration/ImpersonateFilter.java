package com.security.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Component;


@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ImpersonateFilter implements Filter {
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	private Logger logger = LogManager.getLogger();
	
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest)request, (HttpServletResponse)response);
		chain.doFilter(request, response);
	}
	
	private void doFilter(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			String impersonateUserName  = request.getHeader("impersonateUser");
			if(impersonateUserName == null || impersonateUserName.isEmpty())
				return;
			logger.debug("impersonateUserName : /%s" , impersonateUserName);
			Authentication targetUser = attemptSwitchUser(request);
			// update the current context to the new target user
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(targetUser);
			SecurityContextHolder.setContext(context);
			logger.debug(LogMessage.format("Set SecurityContextHolder to %s", targetUser));
		}
		catch (AuthenticationException ex) {
			logger.error("Failed to switch user" ,  ex);
		}

	}

	
	/**
	 * Attempt to switch to another user. If the user does not exist or is not active,
	 * return null.
	 * @return The new <code>Authentication</code> request if successfully switched to
	 * another user, <code>null</code> otherwise.
	 * @throws UsernameNotFoundException If the target user is not found.
	 * @throws LockedException if the account is locked.
	 * @throws DisabledException If the target user is disabled.
	 * @throws AccountExpiredException If the target user account is expired.
	 * @throws CredentialsExpiredException If the target user credentials are expired.
	 */
	protected Authentication attemptSwitchUser(HttpServletRequest request) throws AuthenticationException {
		UsernamePasswordAuthenticationToken targetUserRequest;
		String impersonateUserProviderId  = request.getHeader("impersonateUser");
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		User user = new User(impersonateUserProviderId,"impersonateUser",grantedAuthorities);
		logger.debug(LogMessage.format("Attempting to switch to user [%s]", impersonateUserProviderId));
		// OK, create the switch user token
		targetUserRequest = createSwitchUserToken(request, user);
		return targetUserRequest;
	}

	private UsernamePasswordAuthenticationToken createSwitchUserToken(HttpServletRequest request,
			User targetUser) {
		UsernamePasswordAuthenticationToken targetUserRequest;

		// add the new switch user authority
		List<GrantedAuthority> newAuths = new ArrayList<>();
		// create the new authentication token
		targetUserRequest = new UsernamePasswordAuthenticationToken(targetUser, "impersonated", newAuths);
		// set details
		targetUserRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
		return targetUserRequest;
	}

	protected Authentication attemptExitUser(HttpServletRequest request)
			throws AuthenticationCredentialsNotFoundException {
		// need to check to see if the current user has a SwitchUserGrantedAuthority
		Authentication current = SecurityContextHolder.getContext().getAuthentication();
		if (current == null) {
			throw new AuthenticationCredentialsNotFoundException(this.messages
					.getMessage("SwitchUserFilter.noCurrentUser", "No current user associated with this request"));
		}
		// check to see if the current user did actual switch to another user
		// if so, get the original source user so we can switch back
		Authentication original = getSourceAuthentication(current);
		if (original == null) {
			logger.info("Failed to find original user");
			throw new AuthenticationCredentialsNotFoundException(this.messages
					.getMessage("SwitchUserFilter.noOriginalAuthentication", "Failed to find original user"));
		}
		return original;
	}

	
	private Authentication getSourceAuthentication(Authentication current) {
		Authentication original = null;
		// iterate over granted authorities and find the 'switch user' authority
		Collection<? extends GrantedAuthority> authorities = current.getAuthorities();
		for (GrantedAuthority auth : authorities) {
			// check for switch user type of authority
			if (auth instanceof SwitchUserGrantedAuthority) {
				original = ((SwitchUserGrantedAuthority) auth).getSource();
				logger.debug(LogMessage.format("Found original switch user granted authority [%s]", original));
			}
		}
		return original;
	}

	
}

