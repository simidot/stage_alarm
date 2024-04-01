package com.example.stagealarm.awsS3;

import com.example.stagealarm.awsS3.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3ImageController {
    private final S3FileService s3FileService;

    @GetMapping
    public String hello() {
        return "content/hello";
    }

    @PostMapping("/image")
    public ResponseEntity<String> imgUpload(
            @RequestBody List<MultipartFile> files) {
        List<String> lists = s3FileService.uploadIntoS3("/boardImg", files);
        return ResponseEntity.ok(lists.get(0));
    }
}
