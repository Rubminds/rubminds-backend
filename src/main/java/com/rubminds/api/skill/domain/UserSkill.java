package com.rubminds.api.skill.domain;

import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_skill")
public class UserSkill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private void setUser(User user){
        user.getUserSkills().add(this);
        this.user = user;
    }

    public static UserSkill create(User user, Skill skill){
        UserSkill userSkill = UserSkill.builder().skill(skill).build();
        userSkill.setUser(user);
        return userSkill;
    }
}
