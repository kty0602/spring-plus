package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final String nickname;
    private final UserRole userRole;

    /*
    * 권한 목록을 저장하는데 사용
    * GrantedAuthority -> Spring Security에서 사용자의 권한을 나타내는 인터페이스
    * 저장하고자 하는 권한 문자열 값을 SimpleGrantedAuthority 생성자 파라미터에 넣어주면 된다.*/
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long id, String email, String nickname, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
        this.authorities = List.of(new SimpleGrantedAuthority(userRole.name()));
    }
}
