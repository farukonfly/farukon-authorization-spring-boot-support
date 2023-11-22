package org.farukon.authorization.jdbc.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.farukon.authorization.DefaultSecurityResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//用户未登录处理器
public class DefaultWebAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(new DefaultSecurityResult(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage(), authException.getLocalizedMessage(),
				request.getRequestURI()).toJson());
		writer.flush();
		writer.close();
		
	}

}
