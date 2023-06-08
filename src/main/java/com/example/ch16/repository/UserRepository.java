package com.example.ch16.repository;


import com.example.ch16.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User getByUid(String uid);  //uid를 통해서 유저를 리턴할수있음

}
