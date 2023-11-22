CREATE TABLE security_user (
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_non_expired char(1) NOT NULL DEFAULT '1',
    account_non_locked char(1) NOT NULL DEFAULT '1',
    credentials_non_expired char(1) NOT NULL DEFAULT '1',
    enabled char(1) NOT NULL DEFAULT '1',
    UNIQUE (username)    
);

CREATE TABLE security_static_configuration(
    name VARCHAR(255) NOT NULL,
    value VARCHAR(3000) NOT NULL,
    enabled char(1) NOT NULL DEFAULT '1',
    UNIQUE (name)
);

INSERT into security_static_configuration (name,value) values ('security.csrf.enabled','0');
INSERT INTO security_static_configuration (name,value) values ('security.path.permitted',
'/login,/logout,/error,/css/**,/js/**,/images/**,/webjars/**,/favicon.ico,/api/**,/actuator/**,/swagger-ui/**,/swagger-ui.html/**,/swagger-resources/**,/v2/api-docs/**,/v3/api-docs/**');




