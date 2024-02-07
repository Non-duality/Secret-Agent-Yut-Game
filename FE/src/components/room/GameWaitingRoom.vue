<template>
  <div class="wait-div">
    <!-- 왼쪽 사용자 화면이다 참가한 유저가 보이고 채팅이 보인다 -->
    <div class="wait-left">
      <LeftComponentsVue />
    </div>

    <!-- 방 상세 정보와 준비, 나가기 버튼이 있다 -->
    <div class="wait-right">
      <RightComponentsVue />

      <!-- v-if/else로 방장 구분
      <button class="wait-btn">시작</button>
      <button class="wait-btn">준비</button>
      <button class="wait-btn" @click="goTolist">나가기</button> -->
    </div>
    <!-- <banModal @close-ban-modal="closeBanModal" v-if="ban" /> -->
  </div>
</template>

<script>
import { pubRoom } from "@/util/socket.js";
import { useUserStore } from '@/store/userStore';

export default {
  method: {
    goTolist() {
      // 방을 나가기 위해 pub로 알린다.
      pubRoom("/pub/room/" + useUserStore().currentRoomInfo.roomCode + "/exit"
      , useUserStore().userInfo.email);
      
      // 구독 취소한 뒤 방 정보에 대해 모두 리셋한다.
      useRoomStore().subscription.room.unsubscribe();
      const initialStateRoom = useRoomStore().$reset();
      Object.assign(this, initialStateRoom);

      this.$router.push({name: "room"});
    },
  },
};
</script>

<style scoped>
@import "@/assets/css/room/waitingRoom.css";
</style>
