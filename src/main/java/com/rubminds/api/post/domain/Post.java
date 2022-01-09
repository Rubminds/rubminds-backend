package com.rubminds.api.post.domain;

import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.exception.NotFullFinishedException;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.exception.AdminException;
import com.rubminds.api.team.exception.TeamOutOfBoundException;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.*;

import javax.persistence.*;
import java.util.*;

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

    @Column(nullable = false)
    private String region;

    private String completeContent;

    private String refLink;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<PostSkill> postSkills = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<CustomSkill> customSkills = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<PostLike> postLikeList = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<PostFile> postFileList = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public boolean isLike(CustomUserDetails customUserDetails) {
        return customUserDetails != null && this.postLikeList.stream().anyMatch(postLike -> postLike.getUser().getId().equals(customUserDetails.getUser().getId()));
    }

    public void update(PostRequest.CreateOrUpdate request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.headcount = request.getHeadcount();
        this.meeting = request.getMeeting();
        this.kinds = request.getKinds();
    }

    public void changeStatus(PostRequest.ChangeStatus request) {
        this.postStatus = request.getPostStatus();
    }

    public void updateComplete(PostRequest.CreateCompletePost request) {
        this.completeContent = request.getCompleteContent();
        this.refLink = request.getRefLink();
    }

    public static Post create(PostRequest.CreateOrUpdate request, Team team, User user) {
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .headcount(request.getHeadcount())
                .kinds(request.getKinds())
                .meeting(request.getMeeting())
                .postStatus(PostStatus.RECRUIT)
                .region(request.getRegion())
                .writer(user)
                .team(team)
                .build();
    }

    public void isHeadcountFull(Team team){
        if (Objects.equals(this.headcount, team.getTeamUsers().size())) throw new TeamOutOfBoundException();
    }

}