package com.example.stagealarm.show.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shows")
public class ShowInfoViewController {
    @GetMapping
    public String readAll() {
        return "content/show/shows";
    }

    @GetMapping("/showInfo")
    public String createShowInfo() {
        return "content/show/showUpload";
    }
}
