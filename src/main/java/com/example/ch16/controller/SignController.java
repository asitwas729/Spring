package com.example.ch16.controller;

import com.example.ch16.dto.SignInResultDto;
import com.example.ch16.dto.SignUpResultDto;
import com.example.ch16.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign-api")  //SecurityConfig에서 한 예외처리
public class SignController {

  private final SignService signService;

  @Autowired
  public SignController(SignService signService) {
    this.signService = signService;
  }
  @Operation(summary = "로그인")
  @PostMapping("/sing-in")
  public SignInResultDto signIn(@RequestParam String id, @RequestParam String password) throws RuntimeException {
    SignInResultDto signInResultDto = signService.signIn(id, password);
    if(signInResultDto.getCode() == 0) {
      System.out.println("[SignIn] 정상적으로 로그인되었습니다. "+ signInResultDto.getToken());
    }
    return signInResultDto;
  }
  @Operation(summary = "회원가입")
  @PostMapping("/sing-up")
  public SignUpResultDto signUp(@RequestParam String id, @RequestParam String password, @RequestParam String name, @RequestParam String email, @RequestParam String role) throws RuntimeException {
    SignUpResultDto signUpResultDto = signService.signUp(id, password, name,email, role);
    return signUpResultDto;
  }

  @GetMapping("/exception")
  public void exception() throws RuntimeException {
    throw new RuntimeException("접근이 금지되었습니다.");
  }
}