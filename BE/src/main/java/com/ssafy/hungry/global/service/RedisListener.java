package com.ssafy.hungry.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
// Redis 채널에서 메시지를 수신 받고, 알맞은 구독자들에게 전달.
public class RedisListener implements MessageListener {

    // Redis 채널에서 메시지가 수신될 때마다 자동으로 호출 되는 메서드
    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
