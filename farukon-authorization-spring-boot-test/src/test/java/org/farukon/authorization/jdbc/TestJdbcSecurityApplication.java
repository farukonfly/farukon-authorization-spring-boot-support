package org.farukon.authorization.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

@SpringBootApplication
@EnableSpringHttpSession
public class TestJdbcSecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestJdbcSecurityApplication.class, args);
	}
}
