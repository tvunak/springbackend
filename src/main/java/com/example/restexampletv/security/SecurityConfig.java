package com.example.restexampletv.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.restexampletv.Exceptions.TokenNotValidException;
import com.example.restexampletv.model.ErrorResponse.ArticleErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

    @Autowired
    AppUserDetailService userDetailService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/api/articles", "/api/article/{id}", "/api/user/add", "/api/articleDetails/{id}");
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailService);

//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception, TokenNotValidException {

       /* http
                .csrf().disable()
                .exceptionHandling()
                //.authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/validateLogin").authenticated()
                .antMatchers("/api/addArticle").hasRole("ADMIN")
                .and()
                .formLogin()
                //.successHandler(mySuccessHandler)
                //.failureHandler(myFailureHandler)
                .and()
                .logout();*/
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.antMatchers("/api/user/login").permitAll()


                //.anyRequest().authenticated()
                .and()
                .addFilterBefore(new JWTLoginFilter("/api/user/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                //.anyRequest().authenticated()
                .antMatchers("/api/user").hasRole("ADMIN")
                .antMatchers("/api/article").hasRole("ADMIN")
                .antMatchers("/api/address/add").authenticated()
                .antMatchers("/api/basket").authenticated()
                .antMatchers( "/api/uploadFile").authenticated()
                .antMatchers( "/api/downloadFile/").authenticated();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
    
    @ExceptionHandler
    public ResponseEntity<ArticleErrorResponse> handleArticleException(ExpiredJwtException exc) {
    	
    	//create ArticleErrorResponse
    	ArticleErrorResponse error = new ArticleErrorResponse();
    	
    	error.setStatus(HttpStatus.UNAUTHORIZED.value());
    	error.setMessage(exc.getMessage());
    	error.setTimestamp(System.currentTimeMillis());
    	
    	//return responseEntity
    	return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    	
    }

}