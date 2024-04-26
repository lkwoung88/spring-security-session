package com.spring.security.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.code.ResponseCode;
import com.spring.security.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        LoginResponse res = new LoginResponse(ResponseCode.LOGIN_FAILURE);
        String responseValue = objectMapper.writeValueAsString(res);

        response.getWriter().write(responseValue);
    }
}
