package org.example.expert.domain.user;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class UserData  {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int NICKNAME_LENGTH = 8; // 닉네임 길이
    private SecureRandom random = new SecureRandom();

    private String randomNickname() {
        StringBuilder nickname = new StringBuilder(NICKNAME_LENGTH);
        for (int i = 0; i < NICKNAME_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            nickname.append(CHARACTERS.charAt(index));
        }
        return nickname.toString();
    }

    @Test
    public void testGenerateUsers() {
        saveUsers(1000000);
    }

    private void saveUsers(int count) {
        Set<String> nicknames = new HashSet<>();
        for (int i = 1; i <= count; i++) {
            String email = "test" + i + "@example.com";
            String password = "1234";
            String nickname = randomNickname();

            if (!nicknames.contains(nickname)) {
                nicknames.add(nickname);

                User newUser = new User(email, passwordEncoder.encode(password), nickname, UserRole.ROLE_USER, "Ss");
                userRepository.save(newUser);
            }
        }
        System.out.println("100만 건의 사용자 데이터 생성 완료");
    }

}
