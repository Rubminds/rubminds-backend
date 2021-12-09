package com.rubminds.api.file.dto;

import lombok.*;

public class FileRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Upload {
        private String originalFileName;
        private String name;
        private String extension;
        private Long size;
        private String publicUrl;
        private Integer width;
        private Integer height;
    }
}
