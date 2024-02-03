package com.ssafy.hungry.domain.user.service;

import com.ssafy.hungry.domain.user.detail.CustomUserDetails;
import com.ssafy.hungry.domain.user.entity.EmailEntity;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.repository.EmailRepository;
import com.ssafy.hungry.domain.user.repository.UserRepository;
import com.ssafy.hungry.domain.user.dto.JoinDto;
import com.ssafy.hungry.domain.user.dto.MyInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.NoSuchElementException;

@Service
public class UserService implements UserDetailsService { //회원 관련 서비스를 모아둔 클래스
    //유저 레포지토리 명령어 사용을 위한 선언
    private final UserRepository userRepository;
    //비밀번호를 암호화 하여 저장하기 위한 선언
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailRepository emailRepository;
    private final MailService mailService;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    //의존성 주입을 위한 생성자 주입 패턴
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MailService mailService, EmailRepository emailRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailService = mailService;
        this.emailRepository = emailRepository;
    }


    //회원 가입 메소드
    public Boolean join(JoinDto joinDto) {
        String email = joinDto.getEmail();
        String nickname = joinDto.getNickname();

        //이메일 닉네임 중복 확인
        if (userRepository.existsByEmail(email) && !userRepository.existsByNickname(nickname)) return false;

        //이메일 인증을 받았는지 확인
        if(emailRepository.existsById(AUTH_CODE_PREFIX + email)) return false;

        UserEntity user = new UserEntity();
        user.setEmail(joinDto.getEmail());
        user.setGender(joinDto.getGender());
        user.setBirthDate(joinDto.getBirthDate());
        user.setNickname(joinDto.getNickname());
        user.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);
        return true;
    }

    // 아이디 중복 검사
    public boolean checkId(String email){
        return userRepository.existsByEmail(email);
    }

    // 스웨거용
    public UserEntity getProfile(String userId){
        return userRepository.findByEmail(userId);
    }

    // 사용자 정보 변경
    public void modifyProfile(String userId, MyInfoDto dto) {

    }

//    public MyInfoDto myInfo(String email) {
//        MyInfoDto myInfo = new MyInfoDto();
//        UserEntity findUser = userRepository.findByEmail(email);
//        myInfo.setEmail(findUser.getEmail());
//        myInfo.setNickname(findUser.getNickname());
//        myInfo.setBirthDate(findUser.getBirthDate());
//        myInfo.setGender(findUser.getGender());
//        myInfo.setProfileImgUrl(findUser.getProfileImgUrl());
//        return myInfo;
//    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("userDetailService");
        UserEntity userData = userRepository.findByEmail(email);

        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }

    public UserEntity findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void deleteUser(String userId) {
        userRepository.deleteByEmail(userId);
    }

    //이메일을 받아서 해당 이메일에 인증 메일 발송
    public void sendCodeToEmail(String toEmail) {
        //제목 세팅
        String title = "밀정 윷놀이 가입 이메일 인증 코드입니다.";
        //인증 코드 세팅
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        EmailEntity entity = new EmailEntity(AUTH_CODE_PREFIX + toEmail, authCode, authCodeExpirationMillis );
        emailRepository.save(entity);
    }

    //인증코드를 만들어주는 메소드
    private String createCode() {
        //6자리의 인증코드를 생성
        int lenth = 6;
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //사용자가 입력한 인증코드가 맞는지 이메일을 키로 레디스에서 검색
    public Boolean verifiedCode(String email, String authCode) {
        try{
            String redisAuthCode = emailRepository.findById(AUTH_CODE_PREFIX + email).get().getAuthCode();
            boolean authResult = redisAuthCode.equals(authCode);
            if (authResult){
                emailRepository.deleteById(AUTH_CODE_PREFIX + email);
            }
            return authResult;
        }catch (NoSuchElementException e) {
            System.out.println("레디스에서 찾을 수 없습민당");
        }
        return false;
    }
}