package com.rubminds.api.post.domain;
import com.rubminds.api.user.domain.User;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    private String skill;

    public static Skill createSkills(String skill) {
        Skill skill1 = new Skill();
        skill1.builder().skill(skill).build();

        return skill1;
    }

}

