package com.rubminds.api.team.domain;

import com.rubminds.api.team.exception.EvaluateStateException;
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


    public void setTeam(Team team) {
        this.team = team;
    }

    public static TeamUser create(User user) {
        return TeamUser.builder()
                .user(user)
                .finish(false)
                .build();
    }

    public static TeamUser createWithTeam(User user, Team team) {
        return TeamUser.builder()
                .user(user)
                .finish(false)
                .team(team)
                .build();
    }

    public void updateFinish() {
        this.finish = true;
    }

    public void isEvaluated(){
        if (this.finish) throw new EvaluateStateException();
    }
}

