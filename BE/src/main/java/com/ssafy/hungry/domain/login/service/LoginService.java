package com.ssafy.hungry.domain.login.service;



import com.ssafy.hungry.domain.login.dto.LoginDto;
import com.ssafy.hungry.domain.user.dto.MyInfoDto;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.repository.UserRepository;
import com.ssafy.hungry.global.util.JWTUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class LoginService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;

    public LoginService(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTUtil jwtUtil){
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserEntity doLogin(LoginDto dto) throws AuthenticationException {
        UserEntity entity = repository.findByEmail(dto.getEmail());

        if(entity == null){
            throw new AuthenticationException("User not Found");
        }

        if(!bCryptPasswordEncoder.matches(dto.getPassword(), entity.getPassword())){
            throw new AuthenticationException("Invalid Password");
        }

        return entity;
    }

    public boolean checkId(String userId){
        return repository.existsByEmail(userId);
    }

    public UserEntity getProfile(String userId){
        return repository.findByEmail(userId);
    }

    public void deleteUser(String userId) {
        repository.deleteByEmail(userId);
    }

    public void modifyProfile(String userId, MyInfoDto dto) {

    }
}