package com.example.stagealarm.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserViewController {

    // 사용자 정보 확인
    @GetMapping("/userInfo")
    public String myProfile(

    ){
        return "/content/user/myProfile";
    }

    // 로그인 화면
    @GetMapping("/user/login")
    public String login() {
        return "content/user/signIn";
    }

    // 로그아웃
    @GetMapping("/user/logout")
    public String logout() {
        return "content/user/logout";
    }

    // 회원가입
    @GetMapping("/user/signup")
    public String signup() {
        return "content/user/signup";
    }

    // 회원정보 수정
    @GetMapping("/user/update")
    public String update(){
        return "content/user/update";
    }

    // 소셜 로그인
    @GetMapping("/user/oauthClient")
    public String oauthLogin(){
        return "content/user/oauthClient";
    }

    // 소셜 로그인 중복 이메일 방지
    @GetMapping("/user/emailDuplicate")
    public String emailDuplicate() {
        return "content/user/emailDuplicate";
    }

    // 비밀번호 변경 페이지
    @GetMapping("/user/change-password")
    public String changePassword(){
        return "content/user/changePassword";}

    // 아이디 찾기 페이지
    @GetMapping("/user/find/id")
    public String findingId(){
        return "content/user/findId";
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/user/find/pw")
    public String findingPassword(){
        return "content/user/findPassword";
    }

}
