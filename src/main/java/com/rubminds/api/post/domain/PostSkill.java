package com.rubminds.api.post.domain;

import com.rubminds.api.skill.domain.Skill;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "post_skill")
public class PostSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private void setPost(Post post) {
        this.post = post;
        post.getPostSkills().add(this);
    }

    public static PostSkill create(Skill skill, Post post) {
        PostSkill postSkill = PostSkill.builder()
                .skill(skill)
                .build();
        postSkill.setPost(post);
        return postSkill;
    }
}