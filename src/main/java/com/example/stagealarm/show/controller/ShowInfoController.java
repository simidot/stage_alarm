package com.example.stagealarm.show.controller;

import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.show.dto.*;
import com.example.stagealarm.show.service.ShowCommentsService;
import com.example.stagealarm.show.service.ShowInfoService;
import com.example.stagealarm.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/show")
@RequiredArgsConstructor
public class ShowInfoController {
    private final ShowInfoService showInfoService;
    private final ShowCommentsService showCommentsService;

    @GetMapping
    public ResponseEntity<Page<ShowInfoResponseDto>> readAll(@RequestParam(required = false) String title, Pageable pageable, Sortable sortable) {
        Page<ShowInfoResponseDto> showInfoResponseDtos = showInfoService.readAll(title, pageable, sortable);
        return ResponseEntity.ok().body(showInfoResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ShowInfoResponseDto> create(@RequestPart("dto") ShowInfoRequestDto dto, @RequestPart("file") MultipartFile file) {
        ShowInfoResponseDto showInfoResponseDto = showInfoService.create(dto, file);
        Long id = showInfoResponseDto.getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(showInfoResponseDto);
    }

    // 게시글 및 댓글 읽기
    @GetMapping("/{id}")
    public ResponseEntity<ShowResponseDto> readOne(@PathVariable("id") Long id) {
        ShowInfoResponseDto showInfoResponseDto = showInfoService.readOne(id);
        List<ShowCommentsResponseDto> showCommentsResponseDtos = showCommentsService.readAll(id);

        ShowResponseDto showResponseDto = new ShowResponseDto(showInfoResponseDto, showCommentsResponseDtos);
        return ResponseEntity.ok().body(showResponseDto);
    }

    // 댓글 작성
    @PostMapping("/{id}")
    public ResponseEntity<ShowCommentsResponseDto> writeComments(@PathVariable("id") Long id, @RequestBody ShowCommentsRequestDto dto) {
        ShowCommentsResponseDto commentsResponseDto = showCommentsService.write(id, dto);
        return ResponseEntity.ok().body(commentsResponseDto);
    }

    // 게시글 업데이트
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody ShowInfoRequestDto dto) {
        showInfoService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        showInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarDto>> calendar() {
        List<CalendarDto> calendar = showInfoService.calendar();

        return ResponseEntity.ok().body(calendar);
    }
}
