package com.example.restexampletv.security;

import com.example.restexampletv.controllers.UserController;
import com.example.restexampletv.model.User;
import com.example.restexampletv.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;


public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
    User creds;

    @Autowired
    private AppUserDetailService appUserDetailService;


    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
        System.out.println("attempt Authentication ");
        InputStream body = req.getInputStream();
        //mapper for console loggging
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("request:"+mapper.writeValueAsString(body));
        this.creds = new ObjectMapper().readValue(body, User.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), creds.getAuthorities()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        System.out.println("successful Authentication ");

        TokenAuthenticationService.addAuthentication(res, auth.getName(), auth.getAuthorities().toString());
        res.addHeader("access-control-expose-headers", "Authorization");
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("auth principal "+auth.getPrincipal());
        System.out.println(mapper.writeValueAsString(auth));
        out.print( mapper.writeValueAsString(auth.getPrincipal()));
        out.flush();
    }
}
