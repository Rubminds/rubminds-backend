package com.rubminds.api.user.domain;

import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.skill.domain.UserSkill;
import com.rubminds.api.user.dto.AuthRequest;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserSkill> userSkills = new ArrayList<>();

    private String oauthId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    private String nickname;

    private String job;

    private String introduce;

    private boolean signupCheck;

    private double attendLevel;

    private double workLevel;

    @Enumerated(EnumType.STRING)
    private SignupProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public void update(AuthRequest.Update request, List<UserSkill> userSkills){
        this.nickname = request.getNickname();
        this.job = request.getJob();
        this.introduce = request.getIntroduce();
        this.userSkills.addAll(userSkills);
    }

    public void updateAvatar(Avatar updateAvatar){
        this.avatar = updateAvatar;
    }

}
