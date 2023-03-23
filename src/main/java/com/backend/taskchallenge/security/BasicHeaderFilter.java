package com.backend.taskchallenge.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicHeaderFilter extends OncePerRequestFilter {
    private final String authApiHeader;
    private final String secretApiKey;

    public BasicHeaderFilter(@Value("${task.api.auth.header}") final String authHeader,
                              @Value("${task.api.auth.key}") final String secretKey) {
        authApiHeader = authHeader;
        secretApiKey = secretKey;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authApiKey = request.getHeader(authApiHeader);
        if (authApiKey == null || !authApiKey.equals(secretApiKey)) {
            response.setStatus(401);
            response.getWriter().append("Not authorized to execute, Provide right credentials");
            return;
        }
        filterChain.doFilter(request, response);
    }


}
