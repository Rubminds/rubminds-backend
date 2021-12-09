package com.rubminds.api.team.domain;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.user.domain.User;
import lombok.*;
import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "post_applicant")
public class PostApplicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public static PostApplicant create(User user, Post post){
        PostApplicant postApplicant = PostApplicant.builder().user(user).post(post).build();
        return postApplicant;
    }
}
