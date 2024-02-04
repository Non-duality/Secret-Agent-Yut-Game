package com.ssafy.hungry.domain.user.controller;

import com.ssafy.hungry.domain.user.detail.CustomUserDetails;
import com.ssafy.hungry.domain.user.dto.JoinDto;
import com.ssafy.hungry.domain.user.dto.MyInfoDto;
import com.ssafy.hungry.domain.user.dto.PasswordDto;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    //회원가입
    @PostMapping("/join")
    public String join(@RequestBody JoinDto joinDto) {
        if(!userService.join(joinDto)){
            return "회원가입 실패";
        }
        return "회원가입 성공";
    }

    //임시비밀번호 발급을 위한 이메일 인증 요청
    @PostMapping("get-temporary-password-email-verification-request")
    public ResponseEntity<String> getTemporaryPasswordEmailVerificationRequest(@RequestParam("email") String email){
        userService.getTemporaryPasswordEmailVerificationRequest(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //임시 비밀번호 이메일 인증 검사
    @PostMapping("get-temporary-password-email-verification")
    public ResponseEntity<String> getTemporaryPassword(@RequestParam("email") String email, @RequestParam("code") String code){
        Boolean response = userService.getTemporaryPasswordEmailVerificationCode(email, code);
        if(response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    //비밀번호 변경
    @PostMapping("change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordDto dto){
        if(userService.changePassword(dto.getPreviousPassword(), dto.getNextPassword())){
            return ResponseEntity.ok().body("비밀번호 변경 성공");
        }
        return ResponseEntity.badRequest().body("비밀번호 변경 실패");
    }

    //이메일 중복체크
    @GetMapping("/{userId}")
    public ResponseEntity checkId(@PathVariable("email") String email) {
        Boolean isExist = userService.checkId(email);

        Map<String, String> result = new HashMap<>();

        if (isExist) {
            result.put("message", "이미 존재하는 사용자 email 입니다.");
            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            result.put("message", "사용 가능한 email 입니다.");
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    //회원정보조회
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

    //내정보 조회
    @GetMapping("/myinfo")
    public ResponseEntity<MyInfoDto> getProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity entity = userService.getProfile(userDetails.getUsername());

        if (entity == null) {
            Map<String, String> result = new HashMap<>();
            result.put("message", "failed");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        } else {
            MyInfoDto dto = MyInfoDto.builder()
                    .email(entity.getEmail())
                    .nickname(entity.getNickname())
                    .gender(entity.getGender())
                    .birthDate(entity.getBirthDate())
                    .build();

            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    //회원정보 수정
    @PatchMapping("/{email}")
    public ResponseEntity modifyProfile(@PathVariable("email") String email,
                                        @RequestBody MyInfoDto profileDto) {
        userService.modifyProfile(email, profileDto);

        Map<String, String> result = new HashMap<>();
        result.put("message", "success");
        return new ResponseEntity(result, HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId,
                                        Authentication authentication) {

        Map<String, String> result = new HashMap<>();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUsername().equals(userId)) {
            userService.deleteUser(userId);
            result.put("message", "success");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("message", "failed");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    //메일 인증 요청 api
    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid String email) {
        userService.sendCodeToEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //메일 인증 api
    @GetMapping("/emails/verifications")
    public ResponseEntity verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode) {
        Boolean response = userService.verifiedCode(email, authCode);
        if(response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

//    @GetMapping("/myinfo")
//    public MyInfoDto myInfo(){
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userService.myInfo(email);
//    }
}