package com.example.ch16.config.security;

import com.example.ch16.service.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor  //lombok, 필수 매개변수(생성자)를 자동으로 만들어준다, 생성자 생략가능
public class JwtTokenProvider {

  private final UserDetailService userDetailService;
  
  private String secretKey = "daelimSpring!@#$daelimSpring!@#$daelimSpring!@#$";
  //의도)서명할때 secretkey가 필요, 보통 코드에 박지않고 .properties에 넣어놓고 가져다 씀
  //secretKey에 최소 필요한 바이트수때문에 임의로 텍스트3번 반복한거임
  private final long tokenValidMillisecond = 1000L * 60 * 60; //60min=1hour

  @PostConstruct  //의존성 주입이 이루어진 후 초기화를 수행하는 메서드
  //why? 의존성 주입 후에 사용해야하는 메서드에 사용함 -> 빈의 초기화를 걱정할 필요x(bean 생애주기에서 오직 한번만 수행된다는 것을 보장)
  // =>bean이 여러번 초기화되는 걸 방지할수있음
  protected void init() {
    System.out.println("[init] JwtTokenProvier in secretKey init start");  //log남기기
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    //다시 인코딩해서 secretKey를 사용할거임
    System.out.println("secretKey :" + secretKey);  //한번 확인하기
  }

  //토큰 생성
  public String createToken(String userUid, List<String> roles) { //로그인할때 사용하는 id값, roles 필요
    System.out.println("[createToken] 토큰 생성 시작"); //log남기기
    Claims claims = Jwts.claims().setSubject(userUid);  //클래임 생성, set메서드 이용해 클레임 속성(?)을 넣을수있음
    claims.put("roles", roles); //변환, 사용자가 가지고있는 권한 리스트를 넣음
    Date now = new Date();  //현재시간 받아서
    String token = Jwts.builder().setClaims(claims).setIssuedAt(now). 
        setExpiration(new Date(now.getTime() + tokenValidMillisecond)).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    //Jwts.builder().클래임.발급일.만료일(현재시간+1h).sign알고리즘(HS256, secretKey)
    return token; //만들어진 토큰 반환
  }

  //권한 체크: 토큰 이용
  public Authentication getAuthenication(String token) {  //Authentication: 들어온 토큰값으로 토큰정보를 조회하는 역할
    System.out.println("[getAuthenication] 토큰 인증 정보 조회"); //log남기기
    UserDetails userDetails = userDetailService.loadUserByUsername(this.getUsername(token));  //loadUserByUsername메서드를 통해서 UserDetails을 가지고옴 //getUsername밑에 생성함 //Username은 토큰이 아니라서 토큰으로 추출해서 사용
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();  //token을 넣어주고, body값을 추출해서, subject로 가는게 리턴하고자하는 값임.\
    //Why? 토큰을 이렇게 만들었기 때문에(위에서 클레임 subject안에 넣어줌)
    System.out.println("[getUsername] : "+ info);
    return info;
  }

  public String resolveToken(HttpServletRequest request) {
    // 헤더 이름은 변경 가능
    return request.getHeader("X-AUTH-TOKEN"); //request안에 header값 X-AUTH-TOKEN(프로젝트마다 다르게 지정가능)로 지정(프론트랑 같은 헤더이름으로 사용)
  }

  // 토큰 유효성 검증
  public boolean validateToken(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); //Claims를 담은 Jws를 받는데, 파싱할거임(위랑 동일)
      return !claimsJws.getBody().getExpiration().before(new Date()); //무엇을 비교) claimsJws.body.만료일.이전인가(현재)
    } catch(Exception e) {
      return false;
    }
  }
}