package com.ssafy.hungry.domain.room.repository;

import com.ssafy.hungry.domain.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    // owner_id를 통해 방 찾기 (조건 : 종료되지 않은 방)
    RoomEntity findRoomByEndAtIsNullAndOwnerId(int ownerId);
    List<RoomEntity> findAllByEndAtIsNullOrderByStartAt();
}
