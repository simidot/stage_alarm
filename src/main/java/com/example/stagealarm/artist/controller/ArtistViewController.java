package com.example.stagealarm.artist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArtistViewController {
    @GetMapping("/artists")
    public String artistUpload() {
        return "content/artist/artistUpload";
    }

    @GetMapping("/artists/{id}/update")
    public String artistUpdate() {
        return "content/artist/artistUpdate";
    }
}
