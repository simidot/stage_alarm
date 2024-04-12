package com.example.stagealarm.user.service;

import com.example.stagealarm.alarm.service.EmailAuthService;
import com.example.stagealarm.awsS3.S3FileService;
import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.jwt.JwtRequestDto;
import com.example.stagealarm.jwt.JwtResponseDto;
import com.example.stagealarm.jwt.JwtTokenUtils;
import com.example.stagealarm.user.dto.PasswordDto;
import com.example.stagealarm.user.entity.UserEntity;
import com.example.stagealarm.user.dto.CustomUserDetails;
import com.example.stagealarm.user.dto.UserDto;
import com.example.stagealarm.user.repo.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationFacade authFacade;
    private final EmailAuthService emailAlertService;
    private final StringRedisTemplate redisTemplate;
    private final S3FileService s3FileService;

    // Security 위한 메서드 구현
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new UsernameNotFoundException(loginId));

        // SecurityContext 에서 접속자 조회시 얻을수 있는 정보(로그인 아이디, 비밀번호, 권한)
        return CustomUserDetails.builder()
                .loginId(userEntity.getLoginId())
                .password(userEntity.getPassword())
                .authorities(userEntity.getAuthorities())
                .build();
    }

    // 로그인 아이디로 엔티티 조회
    public UserEntity searchByLoginId(String loginId){
        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new UsernameNotFoundException(loginId)
        );
    }

    // 해당 이메일로 가입한 아이디가 있나 조회
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    // 해당 로그인 아이디로 가입한 아이디가 있나 확인하는 메서드
    public boolean userExists(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    // 회원 등록
    @Transactional
    public UserDto join(UserDto dto, MultipartFile file) {
        // 로그인 아이디가 이미 있을경우 오류
        if(this.userExists(dto.getLoginId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        UserEntity newUser = UserEntity.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .profileImg(s3FileService.uploadIntoS3("/profileImg", file))
                .address(dto.getAddress())
                .authorities("ROLE_USER")
                .build();

        return UserDto.fromEntity(userRepository.save(newUser));
    }

    // 내 정보 조회
    public UserDto getMyProfile() {
        return UserDto.fromEntity(authFacade.getUserEntity());
    }

    // 모든 회원 조회(관리자)
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 회원 조회(관리자)
    public UserDto searchById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        return UserDto.fromEntity(userEntity);
    }

    // 이메일 중복방지용
    public UserEntity searchByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    // 회원 수정
    @Transactional
    public UserDto update(UserDto dto, MultipartFile file) {
        // 본인이나 관리자 인지 확인
        UserEntity currentUser = authFacade.getUserEntity();


        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated.");
        }

        boolean isCurrentUserOrAdmin = currentUser.getLoginId().equals(dto.getLoginId())
                || currentUser.getAuthorities().contains("ROLE_ADMIN");

        if (!isCurrentUserOrAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        UserEntity userEntity = searchByLoginId(dto.getLoginId());

        userEntity.setNickname(dto.getNickname());
        userEntity.setGender(dto.getGender());
        userEntity.setPhone(dto.getPhone());
        userEntity.setAddress(dto.getAddress());
        userEntity.setProfileImg(s3FileService.uploadIntoS3("/profileImg", file));

        return UserDto.fromEntity(userRepository.save(userEntity));
    }

    // 회원 삭제
    @Transactional
    public void delete() {
        UserEntity currentUser = authFacade.getUserEntity();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated.");
        }
        userRepository.deleteById(currentUser.getId());
    }

    // 회원 삭제(관리자)
    @Transactional
    public void deleteUser(Long id) {
        // 관리자 인지
        if (!authFacade.getUserEntity().getAuthorities().contains("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }

        // 있는지 없는지
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id); // Delete the user by ID.
    }

    // 로그인시 jwt 토큰을 발급하는 메서드
    public JwtResponseDto issueToken(JwtRequestDto dto) {
        // 로그인 아이디가 존재하는지
        if (!userExists(dto.getLoginId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        UserDetails userDetails = loadUserByUsername(dto.getLoginId());

        // 패스워드가 같은지
        if(!passwordEncoder
                .matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);

        return response;
    }

    // jwt 토큰을 재발급하는 메서드
    public JwtResponseDto reissueToken(String tokenId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String token = operations.get(tokenId);
        log.info(token);

        String loginId = null;

        // 리프레쉬 토큰이 유효한지
        try {
            Claims jwtClaims = jwtTokenUtils
                    .parseClaims(token);
            loginId = jwtClaims.getSubject();

        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        UserDetails userDetails = loadUserByUsername(loginId);

        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);

        return response;
    }

    // 로그인시 Refresh jwt 토큰을 발급하는 메서드
    @Transactional
    public Cookie issueRefreshToken(JwtRequestDto dto) {
        // 로그인 아이디가 존재하는지
        if (!userExists(dto.getLoginId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        UserDetails userDetails = loadUserByUsername(dto.getLoginId());

        // 패스워드가 같은지
        if(!passwordEncoder
                .matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return jwtTokenUtils.generateRefreshToken(userDetails);
    }

    // 인증번호 보내는 로직
    public void sendEmail(String email) throws MessagingException {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        Random random = new Random();
        String code = String.valueOf(1000000 + random.nextInt(9000000));
        // 레디스에 인증 코드 저장 및 만료 시간 5분으로 설정
        operations.set(email, code, 5, TimeUnit.MINUTES);

        emailAlertService.
                sendMail(email,
                        "스테이지알람 이메일 인증 코드입니다",
                        "귀하의 인증 코드는: " + code + " 입니다.");
    }


    @Async("threadPoolTaskExecutor")
    @Transactional
    public void sendPwEmail(String email) {
        // 이메일 전송 로직
        String tempPassword = generateTempPassword(10);
        try {
            // 이메일 전송
            emailAlertService.sendMail(email,
                    "스테이지알람 임시 비밀번호 입니다",
                    "귀하의 임시 비밀번호는: " + tempPassword + " 입니다.\n로그인 후 빠른 시일 내에 비밀번호를 수정하시길 바랍니다.");


        } catch (MessagingException e) {
            // 예외 처리 로직
            log.error("Failed to send temporary password email to {}", email, e);
        } finally {
            // 임시 비밀번호로 비밀번호 변경
            changeTempPassword(email, tempPassword);
        }
    }

    public void changeTempPassword(String email, String tempPassword) {
        log.info("==============================");
        UserEntity userEntity = searchByEmail(email);

        userEntity.setPassword(passwordEncoder.encode(tempPassword));

        userRepository.save(userEntity);
        log.info("==============================");
    }


    // 인증 로직
    public ResponseEntity<String> checkEmailCode(String email, String code) {
        log.info("email auth start");
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String original = operations.get(email);

        if(original == null){
            // 이메일 주소에 해당하는 코드가 존재하지 않을 경우(코드 만료), 클라이언트에게 Not Found 응답을 반환
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Email not found");
        }

        if(!original.equals(code)){
            // 코드와 인증번호가 맞지 않을경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid code");
        }

        //
        return ResponseEntity.ok("Code verified successfully");

    }



    // 이메일로 아이디 찾기 로직
    public ResponseEntity<UserDto> findIdByEmailCode(String email, String code) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String original = operations.get(email);

        if(original == null){
            // 이메일 주소에 해당하는 코드가 존재하지 않을 경우(코드 만료), 클라이언트에게 Not Found 응답을 반환
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(null);
        }

        if(!original.equals(code)){
            // 코드와 인증번호가 맞지 않을경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if(!existsByEmail(email)){
            // 없는 이메일일 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 인증 성공시 UserDto 를 body 에 담아서 리턴
        return ResponseEntity.ok(UserDto.fromEntity(searchByEmail(email)));

    }



    @Transactional
    public UserDto updateWithoutFile(UserDto dto) {
        // 본인이나 관리자 인지 확인
        UserEntity currentUser = authFacade.getUserEntity();

        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated.");
        }

        boolean isCurrentUserOrAdmin = currentUser.getLoginId().equals(dto.getLoginId())
                || currentUser.getAuthorities().contains("ROLE_ADMIN");

        if (!isCurrentUserOrAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        UserEntity userEntity = searchByLoginId(dto.getLoginId());
        userEntity.setNickname(dto.getNickname());
        userEntity.setGender(dto.getGender());
        userEntity.setPhone(dto.getPhone());
        userEntity.setAddress(dto.getAddress());

        return UserDto.fromEntity(userRepository.save(userEntity));
    }

    @Transactional
    public UserDto joinWithoutFile(UserDto dto) {
        // 로그인 아이디가 이미 있을경우 오류
        if(this.userExists(dto.getLoginId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        UserEntity newUser = UserEntity.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .authorities("ROLE_USER")
                .build();

        return UserDto.fromEntity(userRepository.save(newUser));
    }

    @Transactional
    public void changePassword(PasswordDto dto) {
        UserEntity userEntity = authFacade.getUserEntity();
        if (!passwordEncoder.matches(dto.getCurrentPassword(), userEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        userEntity.setPassword(passwordEncoder.encode(dto.getNewPassword()));

    }

    public UserDto findLoginIdByEmail(String email) {
        return UserDto.fromEntity(userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    // 임시 비밀번호 생성 메서드
    private String generateTempPassword(int length) {
        // 영문 대소문자와 숫자를 포함하는 문자열을 정의
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        // 보안에 더 강화된 랜덤 값을 생성하기 위해 SecureRandom 인스턴스를 생성
        SecureRandom random = new SecureRandom();
        // 임시 비밀번호를 생성하기 위해 아래의 과정
        return random.ints(length, 0, letters.length())
                .mapToObj(i -> String.valueOf(letters.charAt(i)))
                .collect(Collectors.joining());
    }


    public ResponseEntity<String> checkEmailPwCode(String email, String code) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String original = operations.get(email);

        if(original == null){
            // 이메일 주소에 해당하는 코드가 존재하지 않을 경우(코드 만료), 클라이언트에게 Not Found 응답을 반환
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(null);
        }

        if(!original.equals(code)){
            // 코드와 인증번호가 맞지 않을경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if(!existsByEmail(email)){
            // 없는 이메일일 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 인증 성공시 UserDto 를 body 에 담아서 리턴
        return ResponseEntity.ok("success");
    }

}
