package com.rubminds.api.file.web;

import com.rubminds.api.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/file")
public class FileController {
    private final FileService fileService;
    // 테스트용
    @PostMapping("/upload")
    public String uploadAvatar(@RequestPart MultipartFile file){
        fileService.uploadAvatar(file);
        return "ok!";
    }
}
