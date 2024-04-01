package com.example.stagealarm.artist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArtistViewController {
    @GetMapping("/artists")
    public String imageUpload() {
        return "content/artist/artistUpload";
    }
}
