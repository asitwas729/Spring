package com.example.ch16.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String uid;

  @JsonProperty(access = Access.WRITE_ONLY) //노출X, Access.WRITE_ONLY(쓰기전용):패스워드는 쓰기만하지 읽어주진않으니까
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @ElementCollection(fetch = FetchType.EAGER) //즉시로딩(EAGER): 조회시 한방에 쿼리로 다 조회해옴
  @Builder.Default  //특정 필드를 특정 값으로 초기화하고싶은경우
  private List<String> roles = new ArrayList<>();

  //메서드 구현
  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public String getPassword() {
    return this.password;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public String getUsername() {
    return this.uid;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override //계정이 만료됐는지 리턴(true=만료x)
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override //계정이 잠겨있는지 리턴(true=잠겨있지않다)
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override //비밀번호 만료됐는지 리턴(true=만료x)
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override //계정이 활성화되있는지 리턴(true=활성화상태)
  public boolean isEnabled() {
    return true;
  }
}