package com.example.team_service.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(ApiLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.info(
                "[API - LOG] method={}, uri={}",
                request.getMethod(),
                request.getRequestURI()
        );

        filterChain.doFilter(request, response);
    }
}