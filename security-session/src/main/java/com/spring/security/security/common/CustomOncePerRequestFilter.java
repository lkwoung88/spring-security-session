package com.spring.security.security.common;

import com.spring.security.security.service.AuthUtilsService;
import com.spring.security.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    @Value("${spring.session.user.details}")
    private String redisUserDetails;

    @Value("${spring.session.timeout}")
    private int sessionTimeOut;

    private static final String HTTP_METHOD = "POST";

    private final AuthUtilsService authUtilsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("CustomOncePerRequestFilter.doFilterInternal");

        if (HTTP_METHOD.equals(request.getMethod())) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                CustomUserDetails userDetails = authUtilsService.getSessionInfo();

                if (userDetails == null) {
                    authUtilsService.responseNoAuthorization(response);
                }

                if (checkValid(request)) {
                    session.setMaxInactiveInterval(authUtilsService.getSessionExpire());
                } else {
                    session.setMaxInactiveInterval(sessionTimeOut);
                }

                Collection<? extends GrantedAuthority> authorities = authUtilsService.getAuthorities();

                CustomUserDetails updateUserDetails = new CustomUserDetails(userDetails.getMember(), authorities);

                session.setAttribute(redisUserDetails, updateUserDetails);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(updateUserDetails, null, updateUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkValid(HttpServletRequest request) {
        return "/api/none/pass".equals(request.getRequestURI());
    }
}
