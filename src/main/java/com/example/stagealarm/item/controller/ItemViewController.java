package com.example.stagealarm.item.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class ItemViewController {
    // 아이템 등록
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

    // 아이템 전체 조회
    @GetMapping("/allItems")
    public String items() {
        return "content/item/items";
    }

    // 하나의 공연의 아이템 전체 조회
    @GetMapping("/showItems/{showInfoId}")
    public String ShowInfoItems(
            @PathVariable("showInfoId") Long showInfoId,
            HttpServletResponse response
    ) {

        // 쿠키 생성
        Cookie showInfoIdCookie = new Cookie("showInfoId", showInfoId.toString());
        showInfoIdCookie.setPath("/");
        showInfoIdCookie.setMaxAge(24 * 60 * 60); // 1일 동안 유효
        response.addCookie(showInfoIdCookie); // 응답에 쿠키 추가

        return "content/item/showItems";
    }

    // 아이템 개별 조회
    @GetMapping("/item")
    public String showItem(
    ){
        return "content/item/item";
    }

    // 아이템 결제 성공
    @GetMapping("/success")
    public String success() {
        return "content/item/success";
    }

    // 아이템 결제 실패
    @GetMapping("/fail")
    public String fail(){
        return "content/item/fail";
    }
}
