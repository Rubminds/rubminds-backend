package com.rubminds.api.post.domain;

import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter @Setter
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


    public static Post createPost(User user, String title, String content,
                                  int headcount, Kinds kinds, Meeting meeting, PostStatus postsStatus,
                                  Region region){


        Post post = Post.builder()
                        .user(user)
                        .title(title)
                        .content(content)
                        .headcount(headcount)
                        .kinds(kinds)
                        .meeting(meeting)
                        .postStatus(postsStatus)
                        .region(region)
                        .build();


        return post;
    }

    public void editPost( String title, String content, int headcount, Kinds kinds, Meeting meeting,
                          PostStatus postsStatus, Region region){

        this.title = title;
        this.content = content;
        this.headcount = headcount;
        this.kinds = kinds;
        this.meeting = meeting;
        this.postStatus = postsStatus;
        this.headcount = headcount;
        this.kinds = kinds;
        this.meeting = meeting;
        this.region = region;

    }

}











