package com.rubminds.api.skill.domain;

import com.rubminds.api.post.domain.Post;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "costom_skill")
public class CostomSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private void setPost(Post post){
        post.getCostomSkills().add(this);
        this.post = post;
    }

    public static CostomSkill create(String name,Post Post) {
        CostomSkill costomSkill = CostomSkill.builder()
                .name(name)
                .build();
        costomSkill.setPost(Post);
        return costomSkill;
    }

}
