package com.spring.security.security.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.request.LoginRequest;
import com.spring.security.security.handler.CustomAuthenticationFailureHandler;
import com.spring.security.security.handler.CustomAuthenticationSuccessHandler;
import com.spring.security.security.provider.CustomAuthenticationProvider;
import com.spring.security.security.service.AuthUtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class CustomJsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOGIN_REQUEST_URL = "/api/login/**";
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json";

    public CustomJsonAuthenticationFilter(CustomAuthenticationProvider authenticationProvider,
                                          CustomAuthenticationSuccessHandler successHandler,
                                          CustomAuthenticationFailureHandler failureHandler) {
        super(new AntPathRequestMatcher(LOGIN_REQUEST_URL, HTTP_METHOD));
        setAuthenticationManager(new ProviderManager(authenticationProvider));
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (request.getContextPath() == null || !CONTENT_TYPE.equals(request.getContentType())) {
            throw new AuthenticationServiceException("Authentication content type is not supported : " + request.getContentType());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        LoginRequest loginRequest = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), LoginRequest.class);

        String username = loginRequest.getId();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            throw new AuthenticationServiceException("[AttemptAuthentication] Data is not valid");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
