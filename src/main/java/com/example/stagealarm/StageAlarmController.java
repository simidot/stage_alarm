package com.example.stagealarm;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "main 컨트롤러", description = "main API입니다.")
@Controller
@RequestMapping
public class StageAlarmController {
    @GetMapping
    public String hello() {
        return "content/main";
    }

    @GetMapping("/test")
    public String hi() {
        return "hello";
    }

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }
}
