package com.rubminds.api.skill.domain;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.dto.PostSkillRequest;
import com.rubminds.api.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "postskill")
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

    private void setPost(Post post){
        post.getPostSkills().add(this);
        this.post = post;
    }

    public static PostSkill create(Skill skill,Post Post) {
        PostSkill postSkill = PostSkill.builder()
                .skill(skill)
                .build();
        postSkill.setPost(Post);
        return postSkill;
    }



}
