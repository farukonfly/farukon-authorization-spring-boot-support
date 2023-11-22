package org.farukon.authorization.jdbc.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.farukon.authorization.DefaultSecurityResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 登陆成功
 * 
 * @author farukon
 *
 */

public class DefaultWebAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(new DefaultSecurityResult(HttpServletResponse.SC_OK, "Login Success", request.getRequestURI()).toJson());
		writer.flush();
		writer.close();
	}

}
