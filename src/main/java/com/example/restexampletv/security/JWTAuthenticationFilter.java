package com.example.restexampletv.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.GenericFilterBean;

import com.example.restexampletv.Exceptions.ArticleNotFoundException;
import com.example.restexampletv.Exceptions.TokenNotValidException;
import com.example.restexampletv.model.ErrorResponse.ArticleErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


public class JWTAuthenticationFilter extends GenericFilterBean {

    public JWTAuthenticationFilter() {
        System.out.println("JWTAuthenticationFilter called");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("JWTAuthenticationFilter doFilter ");
        
        try{
        	Authentication authentication = TokenAuthenticationService
                    .getAuthentication((HttpServletRequest) req);
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            filterChain.doFilter(req, res);
        }catch(ExpiredJwtException e) {
        	System.out.println("JWTAuthenticationFilter error ");
        	// getting response from method parameters
        	PrintWriter out = res.getWriter();
        	// creating om object 
            ObjectMapper mapper = new ObjectMapper();
            // creating custom response object 
            ArticleErrorResponse error = new ArticleErrorResponse();
            // getting httpServletResponse, so we can add status 401, if this line would not be written,
            // then this response would have http status 200 (ok)
            HttpServletResponse response=(HttpServletResponse) res;
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        	error.setStatus(HttpStatus.UNAUTHORIZED.value());
        	error.setMessage(e.getMessage());
        	error.setTimestamp(System.currentTimeMillis());
        	
            out.print( mapper.writeValueAsString(error));
            out.flush();
            //throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "filter jwt message");
        }
        
        
    }
    
    
}
