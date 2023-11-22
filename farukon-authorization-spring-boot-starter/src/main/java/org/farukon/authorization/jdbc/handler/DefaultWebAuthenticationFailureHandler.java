package org.farukon.authorization.jdbc.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.farukon.authorization.DefaultSecurityResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//登陆失败
public class DefaultWebAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(new DefaultSecurityResult(HttpServletResponse.SC_UNAUTHORIZED, exception.getLocalizedMessage(),
				exception.getLocalizedMessage(), request.getRequestURI()).toJson());
		writer.flush();
		writer.close();

	}

}
