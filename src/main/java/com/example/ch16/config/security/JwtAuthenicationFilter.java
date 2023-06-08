package com.example.ch16.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter; //객체상속개체를 사용

public class JwtAuthenicationFilter extends OncePerRequestFilter {  //클라이언트 -> 필터역할 -> DispatcherServlet
//JwtTokenProvider: 상속받으면 구현해야할 메서드
  
  //JwtTokenProvider주입
  private final JwtTokenProvider jwtTokenProvider;

  //생성자 생성
  public JwtAuthenicationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }


  @Override //메서드
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken(request);  //token으로 provider의 만들어놓은 resolve에서 ?~?을 추출함
    System.out.println("[doFilterInternal] 토큰 : "+ token);  //log남기기(정상적으로 추출하였는가)

    // =>권한이 있는지 체크하는부분
    if(token != null && jwtTokenProvider.validateToken(token)){ //token이 nullx && token이 만료x
      Authentication authentication = jwtTokenProvider.getAuthenication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } // =>권한이 있는지 체크하는부분

    filterChain.doFilter(request, response);
  }
}