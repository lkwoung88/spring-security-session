package com.spring.security.security.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    @Value("${spring.session.redis.namespace}")
    private String redisNamespace;

    private final RedisTemplate redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("CustomLogoutHandler.logout");

        HttpSession session = request.getSession(false);
        String sessionId = session.getId();

        session.invalidate();
        redisTemplate.delete(redisNamespace + ":userDetails:" + sessionId);
        redisTemplate.delete(redisNamespace + ":sessions:" + sessionId);
        redisTemplate.delete(redisNamespace + ":expirations:" + sessionId);
    }
}
