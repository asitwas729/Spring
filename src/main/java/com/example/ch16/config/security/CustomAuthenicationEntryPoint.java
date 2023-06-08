package com.example.ch16.config.security;


import com.example.ch16.dto.EntryPointErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenicationEntryPoint implements AuthenticationEntryPoint {
  //AuthenticationEntryPoint의 구현체
  
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    System.out.println("[commence] 인증 실패로 에러 발생");  //log
    ObjectMapper objectMapper = new ObjectMapper();
    EntryPointErrorResponse entryPointErrorResponse = new EntryPointErrorResponse();
    entryPointErrorResponse.setMsg("인증이 실패하였습니다.");

    response.setStatus(401);  //응답 401: 권한없음, 에러를 401로 만들어서 보내주는거임
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    response.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
    //이 영역은 컨트롤이 되지않아서 String으로 넣어서 -> json형태로 만들어서 보내주는 것
  }
}