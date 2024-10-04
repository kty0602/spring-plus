package org.example.expert.config;

import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

// ArgumentResolver 대체
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUser authUser;

    /*
    * 부모 클래스인 AbstractAuthenticationToken의 생성자를 호출하여 권한 설정
    * 인증 상태를 true로 설정하여 해당 토큰이 이미 인증된 사용자 정보를 담고 있음을 의미*/
    public JwtAuthenticationToken(AuthUser authUser) {
        super(authUser.getAuthorities());
        this.authUser = authUser;
        setAuthenticated(true);
    }

    // 인증에 필요한 자격 증명을 반환할 필요가 없어서 null 처리
    @Override
    public Object getCredentials() {
        return null;
    }

    /*
    * 기존 ArgumentResolver 역할 대체
    * 사용자 주체 정보 반환*/
    @Override
    public Object getPrincipal() {
        return authUser;
    }
}