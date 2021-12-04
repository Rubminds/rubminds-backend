package com.rubminds.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.UserSkill;
import com.rubminds.api.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostSkill> postSkills = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int headcount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Kinds kinds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Meeting meeting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus postStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;

    public void update(PostRequest.Create request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.headcount = request.getHeadcount();
        this.kinds = request.getKinds();
        this.meeting = request.getMeeting();
        this.postStatus = request.getPostsStatus();
        this.region = request.getRegion();
    }

    public static Post create(PostRequest.Create request, User user) {
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .headcount(request.getHeadcount())
                .kinds(request.getKinds())
                .meeting(request.getMeeting())
                .postStatus(request.getPostsStatus())
                .region(request.getRegion())
                .writer(user)
                .build();
    }
}