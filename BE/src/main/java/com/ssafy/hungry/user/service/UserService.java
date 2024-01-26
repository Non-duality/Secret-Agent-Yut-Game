package com.ssafy.hungry.user.service;

import com.ssafy.hungry.user.dto.JoinDto;
import com.ssafy.hungry.user.dto.LoginDto;
import com.ssafy.hungry.user.entity.UserEntity;
import com.ssafy.hungry.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService { //회원 관련 서비스를 모아둔 클래스
    //유저 레포지토리 명령어 사용을 위한 선언
    private final UserRepository userRepository;
    //비밀번호를 암호화 하여 저장하기 위한 선언
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //의존성 주입을 위한 생성자 주입 패턴
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //회원 가입 메소드
    public Boolean join(JoinDto joinDto) {
        String email = joinDto.getEmail();
        String nickname = joinDto.getNickname();

        //이메일 닉네임 중복 확인
        if (!userRepository.existsByEmail(email) && !userRepository.existsByNickname(nickname)) {
            UserEntity user = new UserEntity();
            user.setEmail(joinDto.getEmail());
            user.setGender(joinDto.getGender());
            user.setBirthDate(joinDto.getBirthDate());
            user.setNickname(joinDto.getNickname());
            user.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
            user.setRole("ROLE_USER");

            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("userDetailService");
        UserEntity userData = userRepository.findByEmail(email);

        if(userData != null){
            return new LoginDto(userData);
        }

        return null;
    }
}
