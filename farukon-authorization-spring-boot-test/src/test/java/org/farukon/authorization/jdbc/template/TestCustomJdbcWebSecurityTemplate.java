package org.farukon.authorization.jdbc.template;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//@Component
public class TestCustomJdbcWebSecurityTemplate extends JdbcWebSecurityTemplate {

	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user1 = User.withUsername("admin").password("123456").authorities("admin:api", "user:api").build();
		UserDetails user2 = User.withUsername("user").password("123456").authorities("user:api").build();

		return new InMemoryUserDetailsManager(user1, user2);
	}

}
