package com.ssafy.hungry.domain.user.service;

import com.ssafy.hungry.domain.login.dto.LoginDto;
import com.ssafy.hungry.domain.user.detail.CustomUserDetails;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.repository.UserRepository;
import com.ssafy.hungry.domain.user.dto.JoinDto;
import com.ssafy.hungry.domain.user.dto.MyInfoDto;
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
        if (userRepository.existsByEmail(email) && !userRepository.existsByNickname(nickname)) return false;

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
}