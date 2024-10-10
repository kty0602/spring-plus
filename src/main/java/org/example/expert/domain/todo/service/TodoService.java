package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.log.service.LogService;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;
    private final LogService logService;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

         String weather = weatherClient.getTodayWeather();
//        String weather = null;

        try {
            if(weather == null) {
                throw new InvalidRequestException("날씨 데이터 오류");
            }

            Todo newTodo = new Todo(
                    todoSaveRequest.getTitle(),
                    todoSaveRequest.getContents(),
                    weather,
                    user
            );
            Todo savedTodo = todoRepository.save(newTodo);


            logService.saveLog("일정 등록", "SUCCESS");

            return new TodoSaveResponse(
                    savedTodo.getId(),
                    savedTodo.getTitle(),
                    savedTodo.getContents(),
                    weather,
                    new UserResponse(user.getId(), user.getEmail(), user.getLink())
            );
        } catch (InvalidRequestException e) {
            logService.saveLog("일정 등록", "FAIL");
            throw e;
        }
    }

    public Page<TodoResponse> getTodos(int page, int size, String weather, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);
        /*
        * 참고 : https://batory.tistory.com/508
        * LocalDate값을 LocalDateTime 값으로 변환
        * 입력값이 2024-09-01 이면 2024-09-01 00:00:00으로 변환
        * 기간 끝은 하루 더해서 자정값으로 변환 repository에서 < 으로 처리
        * */
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.plusDays(1).atStartOfDay() : null;
        Page<Todo> todos = todoRepository.findTodosByCondition(weather, startDateTime, endDateTime, pageable);
        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail(), todo.getUser().getLink()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail(), user.getLink()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    public Page<TodoSearchResponse> searchTodos(int page, int size, String title, LocalDate startDate, LocalDate endDate, String nickname) {
        Pageable pageable = PageRequest.of(page - 1, size);
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.plusDays(1).atStartOfDay() : null;
        Page<TodoSearchResponse> todos = todoRepository.searchTodos(title, startDateTime, endDateTime, nickname, pageable);
        return todos;
    }
}
