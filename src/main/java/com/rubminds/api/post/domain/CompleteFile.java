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
@Table(name = "complete_file")
public class CompleteFile extends BaseEntity {
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

    private void setPost(Post post) {
        this.post = post;
        post.getCompleteFileList().add(this);
    }

    public static CompleteFile create(Post post, SavedFile file) {
        CompleteFile completeFile = CompleteFile.builder()
                .name(file.getName())
                .extension(file.getExtension())
                .height(file.getHeight())
                .width(file.getWidth())
                .size(file.getSize())
                .url(file.getPublicUrl())
                .build();
        completeFile.setPost(post);
        return completeFile;
    }
}

