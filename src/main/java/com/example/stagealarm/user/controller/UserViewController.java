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


}
