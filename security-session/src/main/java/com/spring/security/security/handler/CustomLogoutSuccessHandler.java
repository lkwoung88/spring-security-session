package com.spring.security.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.code.ResponseCode;
import com.spring.security.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomLogoutSuccessHandler.onLogoutSuccess");

        ObjectMapper objectMapper = new ObjectMapper();
        BaseResponse res = new BaseResponse(ResponseCode.LOGOUT_SUCCESS);

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        String responseValue = objectMapper.writeValueAsString(res);
        response.getWriter().write(responseValue);
    }
}
