<template>
  <div class="wait-div">
    <!-- 왼쪽 사용자 화면이다 참가한 유저가 보이고 채팅이 보인다 -->
    <div class="wait-left">
      <LeftComponentsVue/>
    </div>

    <!-- 방 상세 정보와 준비, 나가기 버튼이 있다 -->
    <div class="wait-right">
      <RightComponentsVue/>

      <!-- v-if/else로 방장 구분 -->
      <button class="wait-btn">시작</button>
      <button class="wait-btn">준비</button>
      <button class="wait-btn" @click="goToList">나가기</button>
    </div>
    <!-- <banModal @close-ban-modal="closeBanModal" v-if="ban" /> -->
  </div>
</template>

<script>
import { useUserStore } from "@/store/userStore";
import { useRouter } from "vue-router";

const router = useRouter();

export default {
  method:{
    goTolist() {
      router.push("/room");
    },
  },
};
</script>

<style scoped>
@import "@/assets/css/room/waitingRoom.css";
</style>
