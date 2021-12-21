package com.rubminds.api.file.domain;

import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.file.dto.SavedFile;
import com.rubminds.api.user.domain.User;
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
    @Column(name = "avatar_id")
    private Long id;

    private String name;
    private String extension;
    private String url;
    private Long size;
    private Integer width;
    private Integer height;

    @OneToOne(mappedBy = "avatar")
    private User user;

    public static Avatar create(SavedFile request){
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
