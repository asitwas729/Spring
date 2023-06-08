package com.example.ch16.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto extends SignUpResultDto{ //success,code,msg상속받음

  private String token;

  @Builder
  public SignInResultDto(boolean success, int code, String msg, String token) {
    super(success, code, msg);
    this.token = token;
  }
}