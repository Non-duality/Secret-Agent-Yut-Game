package com.ssafy.hungry.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String hello(){
        return "hello world";
    }

    @GetMapping("/hello")
    public String hi(){
        return "테스트2";
    }

    @GetMapping("/test")
    public String test(){
        return "테스트이빈다";
    }
}
