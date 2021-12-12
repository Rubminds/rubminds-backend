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

    @Column(nullable = false)
    private String grade;

    public static TeamUser create(Team team, User user, String grade) {
        TeamUser teamUser = TeamUser.builder()
                .team(team)
                .user(user)
                .finish(false)
                .grade(grade)
                .build();

        return teamUser;
    }

    public void update(TeamUser teamUser) {
        this.team = teamUser.getTeam();
        this.user = teamUser.getUser();
        this.grade = teamUser.getGrade();
        this.finish = true;
    }

}
