//package com.rubminds.api.team.domain;
//
//import com.rubminds.api.common.domain.BaseEntity;
//import com.rubminds.api.user.domain.User;
//import lombok.*;
//
//import javax.persistence.*;
//
//@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
//@Entity
//@Table(name = "team_user")
//public class TeamUser extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(mappedBy = "team_user", fetch = FetchType.LAZY)
//    @JoinColumn(name = "team_id")
//    private Team team;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @Column(nullable = false)
//    private Integer finish;
//
//    @Column(nullable = false)
//    private String grade;
//}
