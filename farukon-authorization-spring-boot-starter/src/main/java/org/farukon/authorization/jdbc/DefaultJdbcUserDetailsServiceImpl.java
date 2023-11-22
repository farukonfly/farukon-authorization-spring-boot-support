package org.farukon.authorization.jdbc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;


public class DefaultJdbcUserDetailsServiceImpl implements UserDetailsService, MessageSourceAware {
    private static final Logger logger = LoggerFactory.getLogger(DefaultJdbcUserDetailsServiceImpl.class);
    
    private  JdbcTemplate jdbcTemplate;

    private static final String DEF_USERS_BY_USERNAME_QUERY =
            """
		select username,password,account_non_expired,account_non_locked,credentials_non_expired,enabled\s\
		from security_user\s\
		where username = ?""";

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private String usersByUsernameQuery;


    public DefaultJdbcUserDetailsServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.usersByUsernameQuery = DEF_USERS_BY_USERNAME_QUERY;
        // this.authoritiesByUsernameQuery = DEF_AUTHORITIES_BY_USERNAME_QUERY;
        // this.groupAuthoritiesByUsernameQuery = DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY;
    }

    protected List<UserDetails> doLoadUserByUsername(String username) {
        RowMapper<UserDetails> mapper = (rs, rowNum) -> {
            String username1 = rs.getString(1);
            String password = rs.getString(2);
            boolean account_non_expired = rs.getString(3).equals("0") ? false : true;
            boolean account_non_locked = rs.getString(4).equals("0") ? false : true;
            boolean credentials_non_expired = rs.getString(5).equals("0") ? false : true;
            boolean enabled = rs.getString(6).equals("0") ? false : true;
            return new User(username1, password, enabled, account_non_expired, account_non_locked,
                    credentials_non_expired, AuthorityUtils.NO_AUTHORITIES);
        };
        return jdbcTemplate.query(this.usersByUsernameQuery, mapper, username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetails> users = doLoadUserByUsername(username);
        if (users.size() == 0) {
            logger.debug("Query returned no results for user '" + username + "'");
            throw new UsernameNotFoundException(this.messages.getMessage("JdbcDaoImpl.notFound",
                    new Object[] {username}, "Username {0} not found"));
        }
        UserDetails user = users.get(0); // contains no GrantedAuthority[]
        Set<GrantedAuthority> dbAuthsSet = new HashSet<>();
        dbAuthsSet.addAll(user.getAuthorities());
        dbAuthsSet.addAll(addCustomAuthorities(username));
        return user;
    }



    protected List<GrantedAuthority> addCustomAuthorities(String username) {
        return AuthorityUtils.NO_AUTHORITIES;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messages = new MessageSourceAccessor(messageSource);
    }



}
