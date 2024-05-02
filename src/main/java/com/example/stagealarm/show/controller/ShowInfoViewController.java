package com.example.stagealarm.show.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/show")
public class ShowInfoViewController {
    @GetMapping
    public String readAll() {
        return "content/show/shows";
    }

    @GetMapping("/{id}")
    public String readOne() {
        return "content/show/show";
    }

    @GetMapping("/show-Info")
    public String create() {
        return "content/show/showUpload";
    }

    @GetMapping("/{id}/update")
    public String update() {
        return "content/show/showUpdate";
    }
}
