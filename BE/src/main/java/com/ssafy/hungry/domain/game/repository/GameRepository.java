package com.ssafy.hungry.domain.game.repository;

import com.ssafy.hungry.domain.game.entity.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<UnitEntity, Integer> {
    
}
