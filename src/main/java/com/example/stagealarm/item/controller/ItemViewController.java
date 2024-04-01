package com.example.stagealarm.item.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemViewController {
    @GetMapping("/item/register")
    public String register(
            @RequestParam("showInfoId") Long showInfoId,
            HttpServletResponse response){
        // 쿠키 생성
        Cookie showInfoIdCookie = new Cookie("showInfoId", showInfoId.toString());
        showInfoIdCookie.setPath("/");
        showInfoIdCookie.setMaxAge(24 * 60 * 60); // 1일 동안 유효
        response.addCookie(showInfoIdCookie); // 응답에 쿠키 추가

        return "/content/item/registerItem";
    }
}
