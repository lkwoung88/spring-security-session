package com.spring.security.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.code.ResponseCode;
import com.spring.security.response.LoginResponse;
import com.spring.security.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${spring.session.timeout}")
    private int sessionTimeout;

    @Value("${spring.session.user.details}")
    private String redisUserDetails;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomAuthenticationSuccessHandler.onAuthenticationSuccess");

        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        HttpSession session = request.getSession();
        String sessionId = session.getId();
        session.setAttribute(redisUserDetails, userDetails);
        session.setMaxInactiveInterval(sessionTimeout);

        Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(sessionTimeout);
        sessionCookie.setSecure(true);
        sessionCookie.setHttpOnly(true);

        LoginResponse res = new LoginResponse(ResponseCode.LOGIN_SUCCESS);
        String responseValue = objectMapper.writeValueAsString(res);

        response.addCookie(sessionCookie);
        response.getWriter().write(responseValue);
    }

}
