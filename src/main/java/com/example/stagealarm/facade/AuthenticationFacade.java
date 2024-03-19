package com.example.stagealarm.facade;

import com.example.stagealarm.user.UserEntity;
import com.example.stagealarm.user.dto.CustomUserDetails;
import com.example.stagealarm.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
    private final UserRepository userRepository;
    public Authentication getAuth(){
        return SecurityContextHolder.getContext().getAuthentication();

    }
    // 누가 접속했는지 조회
    public UserEntity getUserEntity(){

        // CustomUserDetails 로 형변환하는 과정
        Authentication authentication = getAuth();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return userRepository.findByLoginId(customUserDetails.getLoginId())
                    .orElseThrow(
                            () -> new UsernameNotFoundException(customUserDetails.getLoginId())
                    );
        } else {
            // CustomUserDetails 로 형변환 실패시
            throw new RuntimeException("Authentication principal is not of type CustomUserDetails");
        }
    }
}
