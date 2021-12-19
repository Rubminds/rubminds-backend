package com.rubminds.api.skill.domain;

import com.rubminds.api.post.domain.Post;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "custom_skill")
public class CustomSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private void setPost(Post post) {
        this.post = post;
        post.getCustomSkills().add(this);
    }

    public static CustomSkill create(String name, Post post) {
        CustomSkill customSkill = CustomSkill.builder()
                .name(name)
                .build();
        customSkill.setPost(post);
        return customSkill;
    }

}
