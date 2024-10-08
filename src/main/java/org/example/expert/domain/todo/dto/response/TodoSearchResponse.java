package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
public class TodoSearchResponse {
    private final Long id;
    private final String title;
    private final Integer managerCount;
    private final Integer commentCount;

    public TodoSearchResponse(Long id, String title, Integer managerCount, Integer commentCount) {
        this.id = id;
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }
}
