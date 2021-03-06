package com.example.restexampletv.security;

import com.example.restexampletv.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public class JWTAuthenticationFilter extends GenericFilterBean {

    public JWTAuthenticationFilter() {
        System.out.println("JWTAuthenticationFilter called");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("JWTAuthenticationFilter doFilter ");

        Authentication authentication = TokenAuthenticationService
                .getAuthentication((HttpServletRequest) req);
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(req, res);
    }
}
