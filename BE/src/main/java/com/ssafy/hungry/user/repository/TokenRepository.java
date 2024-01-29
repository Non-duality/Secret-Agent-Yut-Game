package com.ssafy.hungry.user.repository;

import com.ssafy.hungry.user.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<TokenEntity, String> {

}
