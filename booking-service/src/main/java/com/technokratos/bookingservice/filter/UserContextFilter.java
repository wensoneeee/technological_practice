package com.technokratos.bookingservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserContextFilter extends OncePerRequestFilter {

    private final UserContext userContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String userIdHeader = request.getHeader("X-User-Id");
            String userRoleHeader = request.getHeader("X-User-Role");

            if (userIdHeader != null & !userIdHeader.isBlank()) {
                userContext.setUserId(Long.valueOf(userIdHeader));
            }
            if (userRoleHeader != null) {
                UserContext.setUserRole(userRoleHeader);
            }

            filterChain.doFilter(request, response);
        } finally {
            userContext.clear();
        }
    }
}
