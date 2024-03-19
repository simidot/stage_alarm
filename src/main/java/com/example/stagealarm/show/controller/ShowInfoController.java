package com.example.stagealarm.show.controller;

import com.example.stagealarm.show.dto.ShowInfoDto;
import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.service.ShowInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowInfoController {
    private final ShowInfoService showInfoService;

    @GetMapping
    public String readAll(Model model) {
        List<ShowInfo> showInfos = showInfoService.readAll();
        model.addAttribute("shows", showInfos);

        return "content/shows";
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ShowInfoDto dto, MultipartFile file) {
        showInfoService.create(dto, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public void readOne(@PathVariable("id")Long id, Model model) {
        ShowInfo showInfo = showInfoService.readOne(id);
        model.addAttribute("show", showInfo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody ShowInfoDto dto) {
        showInfoService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        showInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
