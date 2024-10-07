package org.example.expert.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.QUser;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> checkDSLUser(String nickname) {
        QUser user = QUser.user;

        User getUser = jpaQueryFactory
                .selectFrom(user)
                .where(user.nickname.eq(nickname))
                .fetchOne();
        return Optional.ofNullable(getUser);
    }
}
