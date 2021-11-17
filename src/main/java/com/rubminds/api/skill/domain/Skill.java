package com.rubminds.api.skill.domain;

import com.rubminds.api.post.domain.Post;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skill;

    public static Skill createSkills(String skill) {
        Skill skill1 = new Skill();
        skill1.builder().skill(skill).build();

        return skill1;
    }

}

