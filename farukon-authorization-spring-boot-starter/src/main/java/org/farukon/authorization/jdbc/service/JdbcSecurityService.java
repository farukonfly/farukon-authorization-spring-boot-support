package org.farukon.authorization.jdbc.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

public interface JdbcSecurityService {
   Optional<UserDetails> getCurrentUserDetails();

}
