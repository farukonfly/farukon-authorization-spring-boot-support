package org.farukon.authorization.jdbc.service.impl;

import java.util.Optional;

import org.farukon.authorization.jdbc.service.JdbcSecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class JdbcSecurityServiceImpl implements JdbcSecurityService {

	@Override
	public Optional<UserDetails> getCurrentUserDetails() {
		if(SecurityContextHolder.getContext()!=null && SecurityContextHolder.getContext().getAuthentication()!=null) {
			return Optional.of((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
		return Optional.empty();
	}
	

}
