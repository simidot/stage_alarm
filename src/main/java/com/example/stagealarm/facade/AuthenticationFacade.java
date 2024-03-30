package com.example.stagealarm.facade;

import com.example.stagealarm.user.entity.UserEntity;
import com.example.stagealarm.user.dto.CustomUserDetails;
import com.example.stagealarm.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
    private final UserRepository userRepository;

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 누가 접속했는지 조회
    public UserEntity getUserEntity() {

        // CustomUserDetails 로 형변환하는 과정
        Authentication authentication = getAuth();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(
                            () -> new UsernameNotFoundException(customUserDetails.getLoginId())
                    );
        } else {
            // CustomUserDetails 로 형변환 실패시
            // 토큰이 없으면 반드시 아래의 예외처리가 되기때문에 return null로 변경
            // throw new RuntimeException("Authentication principal is not of type CustomUserDetails");

            // userEntity NullPointerException 발생하기 때문에 비로그인 유저가 사용가능한 서비스를 구현할 때 null처리 반드시 필요
            return null;
        }
    }
}
