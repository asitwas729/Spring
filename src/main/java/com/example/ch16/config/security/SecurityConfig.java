package com.example.ch16.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; 
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtTokenProvider jwtTokenProvider;

  //생성자 생성
  public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  //단축키:ctrl+o
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    //httpbasic.
    httpSecurity
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/sign-api/sign-in", "/sign-api/sign-up", "/sign-api/exception").permitAll()
        .antMatchers(HttpMethod.GET, "/user/**").permitAll()
        //.antMatchers("**exception**").permitAll()
        .anyRequest().hasAnyRole("ADMIN")
        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenicationEntryPoint())
        .and()
        .addFilterBefore(new JwtAuthenicationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity webSecurity) throws Exception {
    webSecurity.ignoring().antMatchers("/v2/api-docs", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**","/swagger-ui/index.html", "/webjars/**", "/swagger/**", "/sign-api/exception");
    //webSecurity.무시한다.antMatchers(패턴) ->swagger관련 예외처리
  }
}