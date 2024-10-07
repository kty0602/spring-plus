package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> checkDSLUser(@Param("nickname") String nickname);
}
