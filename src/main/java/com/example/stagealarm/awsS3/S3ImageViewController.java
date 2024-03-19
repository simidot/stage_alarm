package com.example.stagealarm.awsS3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class S3ImageViewController {
    @GetMapping("/imageUpload")
    public String imageUpload() {
        return "content/imageUpload";
    }
}
