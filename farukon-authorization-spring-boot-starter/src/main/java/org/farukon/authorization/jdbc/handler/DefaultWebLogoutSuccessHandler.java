package org.farukon.authorization.jdbc.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.farukon.authorization.DefaultSecurityResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DefaultWebLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(
				new DefaultSecurityResult(HttpServletResponse.SC_UNAUTHORIZED, "", "注销成功", request.getRequestURI()).toJson());
		writer.flush();
		writer.close();

	}

}
