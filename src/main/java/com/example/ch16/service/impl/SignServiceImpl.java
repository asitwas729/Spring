package com.example.ch16.service.impl;

import com.example.ch16.config.security.JwtTokenProvider;
import com.example.ch16.dto.CommonResponse;
import com.example.ch16.dto.SignInResultDto;
import com.example.ch16.dto.SignUpResultDto;
import com.example.ch16.entity.User;
import com.example.ch16.repository.UserRepository;
import com.example.ch16.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignServiceImpl implements SignService {
  //SignService의 구현체
  public UserRepository userRepository;
  public JwtTokenProvider jwtTokenProvider;
  public PasswordEncoder passwordEncoder;

  //생성자 생성
  @Autowired
  public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) { //주입
    this.userRepository = userRepository;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public SignUpResultDto signUp(String id, String password, String name, String email, String role) {
    System.out.println("[signUp] 회원가입");  //log남기기
    User user;
    // ADMIN SignUp
    if(role.equalsIgnoreCase("admin")) {  //넘어온 role(파라미터)이 admin(대소문자모두)이면
      user = User.builder().uid(id).name(name).email(email)
          .password(passwordEncoder.encode(password))
          .roles(Collections.singletonList("ROLE_ADMIN")).build();  //리스트(Collections)형태로 바꿔줌
      //필요한 항목 셋팅, encode(패스워드 암호화해서 저장)
    } else {  //User SignUp
      user = User.builder().uid(id).name(name).email(email)
          .password(passwordEncoder.encode(password))
          .roles(Collections.singletonList("ROLE_USER")).build();
    }

    User savedUser = userRepository.save(user);
    SignUpResultDto signUpResultDto = new SignUpResultDto();
    if(!savedUser.getName().isEmpty()) {  //DB의 등록된 user의 getName이 비워있지않으면 정상적으로 저장됨
      setSuccessResult(signUpResultDto);  //signUpResultDto(밑에서 메서드 만듬)
    } else {
      setFailResult(signUpResultDto);
    }
    return signUpResultDto;
  }

  @Override
  public SignInResultDto signIn(String id, String password) throws RuntimeException {
    User user = userRepository.getByUid(id);
    if(!passwordEncoder.matches(password, user.getPassword())) {//입력한 패스워드와 받은 아이디값으로 DB에 저장되어있는 패스워드와 다르면
      throw new RuntimeException(); //로그인 실패
    }

    SignInResultDto signInResultDto = SignInResultDto.builder().token(jwtTokenProvider.createToken(String.valueOf(user.getUid()), user.getRoles())).build();
    setSuccessResult(signInResultDto);

    return signInResultDto;
  }

  private void setSuccessResult(SignUpResultDto result) {
    result.setSuccess(true);
    result.setCode(CommonResponse.SUCCESS.getCode()); //0
    result.setMsg(CommonResponse.SUCCESS.getMsg());  //success
  }
  private void setFailResult(SignUpResultDto result) {
    result.setSuccess(false);
    result.setCode(CommonResponse.FAIL.getCode());  //-1
    result.setMsg(CommonResponse.FAIL.getMsg());  //fail
  }
}