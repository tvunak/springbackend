package com.example.restexampletv.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.restexampletv.Exceptions.ArticleNotFoundException;
import com.example.restexampletv.Exceptions.TokenNotValidException;
import com.example.restexampletv.model.ErrorResponse.ArticleErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

@Service
public class TokenAuthenticationService {
    static final long EXPIRATIONTIME = 14_400_000; // 4 hour (in milliseconds)
    static final String AUTHORITY = "AUTHORITY";
    static final String USERID = "USERID";
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String username, String role) {
        // removing [] brackets from role string
        role = role.substring(1, role.length() - 1);
        String JWT = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITY, role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        System.out.println("TAService getAuthentication:");
        String token = request.getHeader(HEADER_STRING);
        System.out.println("service0");
        if (token != null) {
            // parse the token to get username and user authority
        	try{
        		System.out.println("service1");
        		String user = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody()
                        .getSubject();
        		System.out.println("service2");
                String userAuthority = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody()
                        .get(AUTHORITY).toString();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userAuthority);



                UsernamePasswordAuthenticationToken uToken = new UsernamePasswordAuthenticationToken(user, null, Collections.singletonList(authority));
                System.out.println("uToken: "+uToken);
                
                return user != null ?
                        uToken :
                        null;
        	}catch(ExpiredJwtException e) {
        		System.out.println("service_error");
        		throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "token expired! ");
        		//throw new TokenNotValidException("token expired! ", e);
        	}
            

            
        }
        return null;
    }

    public String getUsernameFromToken(String token){
        System.out.println("token: " + token);

        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
    }
    
    
}