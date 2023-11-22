package org.farukon.authorization.jdbc.template;

import org.farukon.authorization.jdbc.DefaultJdbcUserDetailsServiceImpl;
import org.farukon.authorization.jdbc.handler.DefaultWebAccessDeniedHandler;
import org.farukon.authorization.jdbc.handler.DefaultWebAuthenticationEntryPoint;
import org.farukon.authorization.jdbc.handler.DefaultWebAuthenticationFailureHandler;
import org.farukon.authorization.jdbc.handler.DefaultWebAuthenticationSuccessHandler;
import org.farukon.authorization.jdbc.handler.DefaultWebLogoutSuccessHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public abstract class JdbcWebSecurityTemplate {
	public String loginProcessingUrl() {
		return "/login";
	}
	
	public String logoutUrl() {
		return "/logout";
	}
	
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new DefaultWebLogoutSuccessHandler();
	}
	
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new DefaultWebAuthenticationSuccessHandler();
	}
	
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new DefaultWebAuthenticationFailureHandler();
	}
	
	
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new DefaultWebAuthenticationEntryPoint();
	}
	
	public AccessDeniedHandler accessDeniedHandler() {
		return new DefaultWebAccessDeniedHandler();
	}
	
	 public UserDetailsService userDetailsService() {return null;}
	 
	 public final UserDetailsService defaultUserDetailsService(JdbcTemplate jdbcTemplate) {
		 return new DefaultJdbcUserDetailsServiceImpl(jdbcTemplate);
	 }
}
