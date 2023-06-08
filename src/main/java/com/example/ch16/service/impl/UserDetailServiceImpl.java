package com.example.ch16.service.impl;

import com.example.ch16.repository.UserRepository;
import com.example.ch16.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor  //lombok, 필수 매개변수(생성자)를 자동으로 만들어준다, 생성자 생략가능
@Service
public class UserDetailServiceImpl implements UserDetailService {
//UserDetailService의 구현체

  private final UserRepository userRepository;
  //주입하려면 컨스트럭쳐

  //@service를 넣지않으면, Autowired자동주입이 안됨

  //메서드 구현
  @Override //User로 리턴했는데 (public옆에)UserDetails로 받아도 에러가 안나는 이유, User에 implements UserDetails했기때문
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.getByUid(username);
  }
}