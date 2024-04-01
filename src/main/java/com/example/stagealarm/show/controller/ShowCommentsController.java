package com.example.stagealarm.show.controller;

import com.example.stagealarm.show.dto.ShowCommentsResponseDto;
import com.example.stagealarm.show.dto.ShowCommentsUpdateDto;
import com.example.stagealarm.show.service.ShowCommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class ShowCommentsController {
    private final ShowCommentsService showCommentsService;

    @PatchMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody ShowCommentsUpdateDto dto) {
        showCommentsService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        showCommentsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
