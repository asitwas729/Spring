package com.example.ch16.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data //GETTER+SETTER
@NoArgsConstructor
@AllArgsConstructor //전체 필드 가지고 있는 생성자
@ToString
public class EntryPointErrorResponse {

  private String msg;

}