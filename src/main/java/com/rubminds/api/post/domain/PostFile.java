package com.rubminds.api.post.domain;


import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.file.dto.SavedFile;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_file")
public class PostFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String extension;
    private String url;
    private Long size;

    private Integer width;
    private Integer height;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public static PostFile create(Post post, SavedFile file) {
        return PostFile.builder()
                .name(file.getName())
                .extension(file.getExtension())
                .height(file.getHeight())
                .width(file.getWidth())
                .size(file.getSize())
                .url(file.getPublicUrl())
                .post(post)
                .build();
    }
}

