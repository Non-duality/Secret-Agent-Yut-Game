package com.ssafy.hungry.domain.user.controller;

import com.ssafy.hungry.domain.user.dto.JoinDto;
import com.ssafy.hungry.domain.user.dto.MyInfoDto;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/join")
    public String join(JoinDto joinDto) {
        if(!userService.join(joinDto)){
            return "회원가입 실패";
        }
        return "회원가입 성공";
    }

    @GetMapping("/info/{userId}")
    public ResponseEntity<Map<String, Object>> getInfo(@PathVariable("userId") String userId,
                                                       HttpServletRequest request) {
        System.out.println("UserController.getInfo");

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            UserEntity user = userService.findByEmail(userId);
            MyInfoDto dto = MyInfoDto.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .gender(user.getGender())
                    .birthDate(user.getBirthDate())
                    .build();
            resultMap.put("userInfo", dto);
            status = HttpStatus.OK;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

//    @GetMapping("/myinfo")
//    public MyInfoDto myInfo(){
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userService.myInfo(email);
//    }
}