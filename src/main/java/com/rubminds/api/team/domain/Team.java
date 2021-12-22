package com.rubminds.api.team.domain;

import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "team")
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamUser> teamUsers = new ArrayList<>();

    private void addTeamUser(TeamUser teamUser) {
        teamUser.setTeam(this);
        this.teamUsers.add(teamUser);
    }

    public static Team create(User user, TeamUser teamUser) {
        Team team = Team.builder()
                .admin(user)
                .build();
        team.addTeamUser(teamUser);
        return team;
    }
}
