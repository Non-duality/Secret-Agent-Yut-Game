package com.ssafy.hungry.global.util;

import lombok.Getter;

import java.security.Principal;

@Getter
public class StompPrincipal implements Principal {
    String name;

    public StompPrincipal(String name){
        this.name = name;
    }
}
