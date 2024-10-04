package org.example.expert.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    // 각각 사용자와 관리자 역할을 나타내고 이 값들은 Authority.USER와 Authority.ADMIN으로 매핑된다.
    ROLE_USER(Authority.USER),
    ROLE_ADMIN(Authority.ADMIN);

    // 각 역할에 대응하는 권한 문자열을 저장하는 필드
    private final String userRole;

    /*
    *  of 메소드 -> 인자를 받아 DTO 객체를 생성하는 역할
    * enum 배열 값을 가져와서 입력으로 받은 문자열 role과 대소문자 구분 없이 비교하여 일치하는 값을 필터링
    * */
    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 UerRole"));
    }

    // 상수값 정의 -> ROLE_USER, ROLE_ADMIN을 UserRole에 대응
    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

}
