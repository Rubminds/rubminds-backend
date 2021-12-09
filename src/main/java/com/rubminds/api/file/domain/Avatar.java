package com.rubminds.api.file.domain;

import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.file.dto.FileRequest;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "avatar")
public class Avatar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String extension;
    private String url;
    private Long size;
    private Integer width;
    private Integer height;

    public static Avatar create(FileRequest.Upload request){
        return Avatar.builder()
                .name(request.getName())
                .extension(request.getExtension())
                .url(request.getPublicUrl())
                .size(request.getSize())
                .width(request.getWidth())
                .height(request.getHeight())
                .build();
    }

}
