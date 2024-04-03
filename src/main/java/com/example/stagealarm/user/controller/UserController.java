package com.example.stagealarm.user.controller;

import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.jwt.JwtRequestDto;
import com.example.stagealarm.jwt.JwtResponseDto;
import com.example.stagealarm.user.dto.PasswordDto;
import com.example.stagealarm.user.dto.UserDto;
import com.example.stagealarm.user.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationFacade facade;



    // 로그인 하기
    @PostMapping("/login")
    public JwtResponseDto token(
            @RequestBody
            JwtRequestDto dto
    ){
        log.info("로그인 하기");
        return userService.issueToken(dto);

    }

    // 회원 가입
    @PostMapping
    public UserDto signUp(
            @RequestPart("dto") UserDto dto,
            @RequestPart(name = "file", required = false) MultipartFile file
    ){
        if (file != null && !file.isEmpty()) {
            // 파일이 존재하면, 해당 파일과 함께 사용자 정보 업데이트 처리
            return userService.join(dto, file);
        } else {
            // 파일이 없으면, 파일을 제외한 나머지 정보로 사용자 정보 업데이트 처리
            return userService.joinWithoutFile(dto);
        }
    }


    // 나의 정보 조회
    @GetMapping
    public UserDto myProfile(

    ){
        return userService.getMyProfile();
    }

    // 나의 정보 수정
    @PatchMapping
    public UserDto update(
            @RequestPart("dto") UserDto dto,
            @RequestPart(name = "file", required = false) MultipartFile file
    ){
        if (file != null && !file.isEmpty()) {
            // 파일이 존재하면, 해당 파일과 함께 사용자 정보 업데이트 처리
            return userService.update(dto, file);
        } else {
            // 파일이 없으면, 파일을 제외한 나머지 정보로 사용자 정보 업데이트 처리
            return userService.updateWithoutFile(dto);
        }
    }

    @DeleteMapping
    public void delete(){
        userService.delete();
    }

    // 모든 회원 조회(관리자)
    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    // id로 회원 조회(관리자)
    @GetMapping("/{id}")
    public UserDto getUser(
            @PathVariable("id")
            Long id

    ){
        return userService.searchById(id);
    }

    // id로 회원 삭제(관리자)
    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable("id")
            Long id
    ){
        userService.deleteUser(id);
    }


    // 로그인 이미지 클릭시 토글화면을 위함
    @GetMapping("/auth/status")
    public ResponseEntity<?> isAuthenticated() {
        Map<String, Object> response = new HashMap<>();

        // 인증된 사용자면 ture 익명 사용자면 false
        boolean isAuthenticated = !(facade.getAuth() instanceof AnonymousAuthenticationToken);

        response.put("isAuthenticated", isAuthenticated);
        return ResponseEntity.ok(response);
    }

    // 회원가입시 이메일 중복 체크 로직
    @PostMapping("/email-check")
    public boolean checkEmail(
            @RequestParam("email")
            String email
    ){
        return userService.existsByEmail(email);
    }

    // 회원가입시 이메일 전송
    @PostMapping("/email-send")
    public void checkEmailAuth(
            @RequestParam("email")
            String email
    ) throws MessagingException {
        userService.sendEmail(email);
    }

    // 이메일 인증번호 확인
    @PostMapping("/email-auth")
    public ResponseEntity<String> checkEmail(
            @RequestParam("email")
            String email,
            @RequestParam("code")
            String code
    ){
        return userService.checkEmailCode(email, code);
    }

    // 비밀번호 인증번호 확인
    @PostMapping("/email-pwAuth")
    public ResponseEntity<String> checkEmailPwCode(
            @RequestParam("email")
            String email,
            @RequestParam("code")
            String code
    ){
        return userService.checkEmailPwCode(email, code);
    }

    // 임시비밀번호 발급
    @PostMapping("/email-tempPwSend")
    public void sendTempPw(
            @RequestParam("email")
            String email
    ){
        userService.sendPwEmail(email);
    }

    // 이메일로 아이디 찾기
    @PostMapping("/email-findId")
    public ResponseEntity<UserDto> findIdByEmail(
            @RequestParam("email")
            String email,
            @RequestParam("code")
            String code
    ){
        return userService.findIdByEmailCode(email, code);
    }



    // 회원가입시 로그인 아이디 중복 체크 로직
    @PostMapping("/loginId-check")
    public boolean checkLoginId(
            @RequestParam("loginId")
            String loginId
    ){
        return userService.userExists(loginId);
    }

    // 비밀번호 변경 로직
    @PatchMapping("/change-password")
    public void changePassword(
            @RequestBody
            PasswordDto dto
    ){
        userService.changePassword(dto);
    }

    @PostMapping("find/loginId")
    public UserDto findLogId(
            @RequestParam("email")
            String email
    ){
        return userService.findLoginIdByEmail(email);
    }

}
