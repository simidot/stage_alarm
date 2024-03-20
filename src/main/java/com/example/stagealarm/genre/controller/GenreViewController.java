package com.example.stagealarm.genre.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenreViewController {
    @GetMapping("/subscribe")
    public String subscribeView() {
        return "content/show/subscribe";
    }
}
