package com.example.ch16.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailService {

  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
  //아이디가 존재X, userDetails 리턴
}
