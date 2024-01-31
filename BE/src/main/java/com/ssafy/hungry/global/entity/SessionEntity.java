package com.ssafy.hungry.global.entity;

import com.ssafy.hungry.global.util.StompPrincipal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.security.Principal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RedisHash("session")
public class SessionEntity {
    @Id
    private String email;

    private StompPrincipal principal;

    public SessionEntity(String email, StompPrincipal principal) {
        this.email = email;
        this.principal = principal;
    }
}
