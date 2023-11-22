package org.farukon.authorization.jdbc;

import java.util.List;
import org.farukon.authorization.jdbc.experiment.TokenAuthenticationFilter;
import org.farukon.authorization.jdbc.service.JdbcSecurityService;
import org.farukon.authorization.jdbc.service.impl.JdbcSecurityServiceImpl;
import org.farukon.authorization.jdbc.template.JdbcWebSecurityTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
@Configuration
@EnableWebSecurity
public class JdbcWebSecurityConfiguration {
	
	
	private static final Logger logger = LoggerFactory.getLogger(JdbcWebSecurityConfiguration.class);

	
	private static final String DEF_SECURITY_STATIC_CFG_QUERY = "select name,value "
			+ "from security_static_configuration where enabled='1'";

	private static final String SECURITY_CSRF_ENABLED = "security.csrf.enabled";
	private static final String SECURITY_PATH_PERMITTED = "security.path.permitted";
	private List<SecurityStaticConfiguration> securityStaticConfigurations;
	private  JdbcTemplate jdbcTemplate;
	private  JdbcWebSecurityTemplate securityTemplate;
	
	public JdbcWebSecurityConfiguration(JdbcWebSecurityTemplate securityTemplate,JdbcTemplate jdbcTemplate) {
		this.securityTemplate = securityTemplate ;
		this.jdbcTemplate = jdbcTemplate;
	}
	

	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		securityStaticConfigurations = loadSecurityStaticConfiguration();
		buildCsrf(http);
		buildPermittedPath(http);
		buildFormLogin(http);
		buildLogout(http);
		buildExceptionHandling(http);
		buildCharacterEncodingFilter(http);
        //http.addFilterBefore(new TokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	private HttpSecurity buildCsrf(HttpSecurity http) throws Exception {
		boolean csrfEnabled = isCSRFEnabled();
		if (!csrfEnabled) {
			http.csrf(AbstractHttpConfigurer::disable);
			http.cors(httpSecurityCorsConfigurer -> {
				UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
				CorsConfiguration configuration = new CorsConfiguration();
				configuration.addAllowedOriginPattern("*");
				configuration.addAllowedHeader("*");
				configuration.addAllowedMethod("*");
				configuration.setAllowCredentials(true);
				configurationSource.registerCorsConfiguration("/**", configuration);
				httpSecurityCorsConfigurer.configurationSource(configurationSource);
			});
		}
		return http;

	}

	private HttpSecurity buildPermittedPath(HttpSecurity http) throws Exception {
		String permittedPath = getPermittedPath();
		if (permittedPath != null && !permittedPath.isEmpty()) {
			String[] permittedPaths = permittedPath.split(",");

			http.authorizeHttpRequests(authorizeHttpRequests -> {
				for (String path : permittedPaths) {
					authorizeHttpRequests.requestMatchers(AntPathRequestMatcher.antMatcher(path)).permitAll();
				}
				authorizeHttpRequests.anyRequest().authenticated();
			});

		}
		return http;
	}

	private HttpSecurity buildFormLogin(HttpSecurity http) throws Exception {
        //loginPage:登录页面
        //loginProcessingUrl:登录接口 过滤器
        //defaultSuccessUrl:登录成功之后访问的页面
		http.formLogin(formLogin -> {
			formLogin.loginProcessingUrl(securityTemplate.loginProcessingUrl());
			formLogin.successHandler(securityTemplate.authenticationSuccessHandler());
			formLogin.failureHandler(securityTemplate.authenticationFailureHandler());
		});
		http.logout(logout -> {
			logout.logoutUrl(securityTemplate.logoutUrl());
		});
		return http;
	}

	private HttpSecurity buildExceptionHandling(HttpSecurity http) throws Exception {
		http.exceptionHandling(exceptionHandlingCustomizer -> {
			exceptionHandlingCustomizer.authenticationEntryPoint(securityTemplate.authenticationEntryPoint());
			exceptionHandlingCustomizer.accessDeniedHandler(securityTemplate.accessDeniedHandler());
		});

		return http;
	}

	private HttpSecurity buildLogout(HttpSecurity http) throws Exception {
		http.logout(logoutCustomizer -> {
			logoutCustomizer.logoutSuccessHandler(securityTemplate.logoutSuccessHandler());
			logoutCustomizer.invalidateHttpSession(true);
		});
		return http;
	}
	
	private HttpSecurity buildCharacterEncodingFilter(HttpSecurity http)throws Exception{
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("utf-8");
		characterEncodingFilter.setForceEncoding(true);
		http.addFilterBefore(characterEncodingFilter, CsrfFilter.class);
		return http;
	}

//	@ConditionalOnMissingBean(JdbcWebSecurityTemplate.class)
//	@Bean
//	JdbcWebSecurityTemplate jdbcWebSecurityTemplate() {
//		securityTemplate = new DefaultJdbcWebSecurityTemplateImpl();
//		return securityTemplate;
//	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return securityTemplate.passwordEncoder();
	}
	
	@Bean
	JdbcSecurityService jdbcSecurityService() {
		return new JdbcSecurityServiceImpl();
	}

	@Bean
	UserDetailsService userDetailsService(JdbcWebSecurityTemplate securityTemplate) {
		if (securityTemplate.userDetailsService() != null) {
			return securityTemplate.userDetailsService();
		}
		return securityTemplate.defaultUserDetailsService(jdbcTemplate);
	}

	private List<SecurityStaticConfiguration> loadSecurityStaticConfiguration() {
		RowMapper<SecurityStaticConfiguration> mapper = (rs, rowNum) -> {
			String name = rs.getString(1);
			String value = rs.getString(2);
			return new SecurityStaticConfiguration(name, value);
		};
		return jdbcTemplate.query(DEF_SECURITY_STATIC_CFG_QUERY, mapper);
	}

	private boolean isCSRFEnabled() {
		return !securityStaticConfigurations.stream().filter(s -> s.name.equals(SECURITY_CSRF_ENABLED)).findFirst()
				.get().value.equals("0");

	}

	private String getPermittedPath() {
		return securityStaticConfigurations.stream().filter(s -> s.name.equals(SECURITY_PATH_PERMITTED)).findFirst()
				.get().value;

	}

	private static class SecurityStaticConfiguration {
		private String name;
		private String value;

		public SecurityStaticConfiguration(String name, String value) {
			this.name = name;
			this.value = value;
		}

	}


}
