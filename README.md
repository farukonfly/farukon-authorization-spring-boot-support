# farukon-authorization-spring-boot-support

## DB Schema

```sql

CREATE TABLE security_user (
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_non_expired char(1) NOT NULL DEFAULT '1',
    account_non_locked char(1) NOT NULL DEFAULT '1',
    credentials_non_expired char(1) NOT NULL DEFAULT '1',
    enabled char(1) NOT NULL DEFAULT '1',
    UNIQUE (username)    
);--Standard username, password login form

CREATE TABLE security_static_configuration(
    name VARCHAR(255) NOT NULL,
    value VARCHAR(3000) NOT NULL,
    enabled char(1) NOT NULL DEFAULT '1',
    UNIQUE (name)
);
--Load configuration on startup
-- More configuration items will be supported in the future

INSERT into security_static_configuration (name,value) values ('security.csrf.enabled','0');
INSERT INTO security_static_configuration (name,value) values ('security.path.permitted',
'/login,/logout,/error,/css/**,/js/**,/images/**,/webjars/**,/favicon.ico,/api/**,/actuator/**,/swagger-ui/**,/swagger-ui.html/**,/swagger-resources/**,/v2/api-docs/**,/v3/api-docs/**');
```
## Usage
1. Login  from security_user table is automatically effective and does not require any configuration.
2. Customize the security configuration by extend the JdbcWebSecurityTemplate class and override the specific method.
```java
@component
public class TestCustomJdbcWebSecurityTemplate extends JdbcWebSecurityTemplate {

	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user1 = User.withUsername("admin").password("123456").authorities("admin:api", "user:api").build();
		UserDetails user2 = User.withUsername("user").password("123456").authorities("user:api").build();

		return new InMemoryUserDetailsManager(user1, user2);
	}
}
```

## Todo
1. Add more security configuration items in security_static_configuration table.
2. Add Rbac support.
3. Add Oauth2 support.
4. Add Sso support.
