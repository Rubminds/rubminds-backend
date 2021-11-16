package com.rubminds.api.post.domain;

import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Skill> skill = new ArrayList<Skill>() ;

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


    public void setUser(User user){
        this.user = user;
        user.getPost();
    }

    private void addSkill(Skill skills) {
        skills.builder().post(this);
        skill.add(skills);
    }

    public static Post createPost(User user, String title, String content,
                                  int headcount, Kinds kinds, Meeting meeting, PostStatus postsStatus,
                                  Region region, Skill ...skill){
        Post post = new Post();
        post.builder()
            .user(user)
            .title(title)
            .content(content)
            .headcount(headcount)
            .kinds(kinds)
            .meeting(meeting)
            .postStatus(postsStatus)
            .region(region)
            .build();

        for(Skill i : skill ){
            post.addSkill(i);
        }

        return post;
    }




}
