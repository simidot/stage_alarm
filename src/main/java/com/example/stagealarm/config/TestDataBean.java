package com.example.stagealarm.config;

import com.example.stagealarm.user.entity.UserEntity;
import com.example.stagealarm.user.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestDataBean {

    public TestDataBean(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ){

        userRepository.save(UserEntity.builder()
                .loginId("admin")
                .password(passwordEncoder.encode("password"))
                .email("ggg@gmail.com")
                .authorities("ROLE_ADMIN")
                .build());


        userRepository.save(UserEntity.builder()
                .loginId("user")
                .password(passwordEncoder.encode("password"))
                .email("hhhjs0133@naver.com123")
                .authorities("ROLE_USER")
                .build());

    }
}
