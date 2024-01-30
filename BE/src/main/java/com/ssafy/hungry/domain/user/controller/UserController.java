package com.ssafy.hungry.domain.user.controller;

import com.ssafy.hungry.domain.user.dto.JoinDto;
import com.ssafy.hungry.domain.user.dto.MyInfoDto;
import com.ssafy.hungry.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@ModelAttribute JoinDto joinDto) {
        if(userService.join(joinDto)){
            return ResponseEntity.status(200).body("회원가입 성공");
        }
        return ResponseEntity.status(201).body("회원가입 실패");
    }

    @GetMapping("/myinfo")
    public MyInfoDto myInfo(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.myInfo(email);
    }
}
