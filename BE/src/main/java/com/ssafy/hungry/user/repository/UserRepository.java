package com.ssafy.hungry.user.repository;

import com.ssafy.hungry.user.dto.MyInfoDto;
import com.ssafy.hungry.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    UserEntity findByEmail(String email);
}
