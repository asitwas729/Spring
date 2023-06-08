package com.example.ch16.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpResultDto {

  private boolean success;  //성공여부

  private int code; //상태 코드값

  private String msg;

}