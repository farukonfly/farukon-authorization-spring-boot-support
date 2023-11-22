package org.farukon.authorization.spring.boot;

import org.farukon.authorization.jdbc.session.cache.GuavaSessionRepository;
import org.farukon.authorization.jdbc.template.JdbcWebSecurityTemplate;
import org.farukon.authorization.jdbc.template.impl.DefaultJdbcWebSecurityTemplateImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuthorizationJdbcAutoConfiguration {
	
	@ConditionalOnMissingBean(JdbcWebSecurityTemplate.class)
	@Bean
	JdbcWebSecurityTemplate jdbcWebSecurityTemplate() {
		return new DefaultJdbcWebSecurityTemplateImpl();
	}

    // @Bean
    // GuavaSessionRepository guavaSessionRepository() {
    //     return new GuavaSessionRepository();
    // }

}
