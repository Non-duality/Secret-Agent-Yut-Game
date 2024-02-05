<template>
  <div class="wait-detail">
    <!-- 왼쪽 오른쪽으로 나누었다 -->
    <!-- 왼쪽에는 기본 정보가 있다 -->
    <div class="detail">
      <span class="subject">방 제목</span>
      <span class="content">{{ roomInfo.title }}</span>
      <span class="subject">공개</span>
      <span class="content" v-if="roomInfo.public">공개</span>
      <span class="content" v-else>비공개</span>
      <span class="subject">속도</span>
      <span class="content" v-if="roomInfo.speed === 0">느림</span>
      <span class="content" v-else-if="roomInfo.speed === 1">보통</span>
      <span class="content" v-else-if="roomInfo.speed === 2">빠름</span>
    </div>
    <!-- 오른쪽은 이미지와 테마가 있다. -->
    <div class="detail">
      <img src="@/assets/img/playboard.png" class="play-img" />
      <span class="subject">테마</span>
      <span class="content" v-if="roomInfo.theme === '설날'">설날</span>
      <span class="content" v-else-if="roomInfo.theme === '추석'">추석</span>
    </div>
  </div>
</template>

<script>
// 방 정보 데이터
import { useUserStore } from "@/store/userStore";

export default {
  data() {
    return {
      roomInfo: useUserStore().currentRoomInfo,
    };
  },

  mounted(){
    console.log(useUserStore().currentRoomInfo);
  }
};
</script>

<style>
@import "../../../assets/css/room/waitingRoomRight.css";
</style>