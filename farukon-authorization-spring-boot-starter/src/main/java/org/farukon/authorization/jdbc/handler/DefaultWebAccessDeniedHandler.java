package org.farukon.authorization.jdbc.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.farukon.authorization.DefaultSecurityResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//403
public class DefaultWebAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(new DefaultSecurityResult(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getLocalizedMessage(), accessDeniedException.getLocalizedMessage(),
				request.getRequestURI()).toJson());
		writer.flush();
		writer.close();
		
	}

}
