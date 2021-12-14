package com.rubminds.api.team.domain;

import com.rubminds.api.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "team_user")
public class TeamUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private boolean finish;

    public static TeamUser create(Team team, User user) {
        TeamUser teamUser = TeamUser.builder()
                .team(team)
                .user(user)
                .finish(false)
                .build();

        return teamUser;
    }

    public void update(TeamUser teamUser) {
        this.team = teamUser.getTeam();
        this.user = teamUser.getUser();
        this.finish = true;
    }

}
