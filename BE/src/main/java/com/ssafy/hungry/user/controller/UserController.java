package com.ssafy.hungry.user.controller;

import com.ssafy.hungry.user.dto.JoinDto;
import com.ssafy.hungry.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
            return ResponseEntity.status(200).body("회원가입 실패");
        }
        return ResponseEntity.status(201).body("회원가입 성공");
    }
}
