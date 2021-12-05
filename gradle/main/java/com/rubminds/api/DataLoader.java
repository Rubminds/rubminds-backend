package com.rubminds.api;

import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.user.domain.Role;
import com.rubminds.api.user.domain.SignupProvider;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("local")
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findAll().isEmpty()) {
            List<User> users = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                User user = User.builder().oauthId(String.valueOf(i)).nickname("테스터" + i).job("학생").provider(SignupProvider.RUBMINDS).role(Role.USER).signupCheck(true).build();
                users.add(user);

            }
            userRepository.saveAll(users);
        }
        if (skillRepository.findAll().isEmpty()){
            List<Skill> skills = new ArrayList<>();
            String[] skillnames = new String[] {"Spring","JavaScript","TypeScript","Node.js","React,js","Vue.js","Python","Python",
                                                "Java","C","C++","C#","Django","Go","Swift","Kotlin","Angular.js","Ruby","Java","Flutter"};

            for(String i :skillnames){
                Skill skill = Skill.builder().name(i).build();
                skills.add(skill);
            }


            skillRepository.saveAll(skills);
        }
    }
}
