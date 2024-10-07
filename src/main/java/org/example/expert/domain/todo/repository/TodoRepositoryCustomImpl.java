package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo getTodo = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user)
                .fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();
        return Optional.ofNullable(getTodo);
    }

    @Override
    public Page<TodoSearchResponse> searchTodos(String title, LocalDateTime startDate, LocalDateTime endDate, String nickname, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        // 동적 조건을 위한 BooleanBuilder 생성
        BooleanBuilder builder = new BooleanBuilder();

        if(title != null && !title.isEmpty()) {
            builder.and(todo.title.containsIgnoreCase(title));
        }
        // goe -> >=
        // loe -> <=
        // lt -> <
        // gt -> >
        if(startDate != null) {
            builder.and(todo.createdAt.goe(startDate));
        }
        if(endDate != null) {
            builder.and(todo.createdAt.lt(endDate));
        }
        if(nickname != null && !nickname.isEmpty()) {
            builder.and(user.nickname.containsIgnoreCase(nickname));
        }


        List<TodoSearchResponse> results = jpaQueryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        todo.title,
                        todo.managers.size(),
                        todo.comments.size()
                        ))
                .from(todo)
                .where(builder)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}
