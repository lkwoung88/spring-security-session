package com.spring.security.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.code.ResponseCode;
import com.spring.security.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUtilsService {

    @Value("${spring.session.user.details}")
    private String redisUserDetails;

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${spring.session.user.expire}")
    private String sessionExpires;

    @Value("${spring.session.timeout}")
    private int sessionTimeOut;

    public HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    public CustomUserDetails getSessionInfo() {
        CustomUserDetails userDetails = null;

        HttpServletRequest request = this.getHttpServletRequest();
        HttpSession session = request.getSession(false);

        if (session != null) {
            userDetails = (CustomUserDetails) session.getAttribute(redisUserDetails);
        }

        return userDetails;
    }

    public int getSessionExpire() {
        try {
            HttpServletRequest request = this.getHttpServletRequest();
            HttpSession session = request.getSession(false);
            if (session != null) {
                Long expire = stringRedisTemplate.getExpire(sessionExpires + session.getId());
                return Math.toIntExact(expire);
            }
        } catch (Exception e) {
            return sessionTimeOut;
        }
        return sessionTimeOut;
    }

    public void responseNoAuthorization(HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BaseResponse res = new BaseResponse(ResponseCode.NO_AUTHORIZATION);

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        String responseValue = objectMapper.writeValueAsString(res);
        response.getWriter().write(responseValue);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
}
