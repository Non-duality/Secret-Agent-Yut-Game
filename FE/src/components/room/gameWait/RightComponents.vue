<template>
  <div class="wait-detail">
    <!-- 왼쪽 오른쪽으로 나누었다 -->
    <!-- 왼쪽에는 기본 정보가 있다 -->
    <div class="detail">
      <span class="subject">방 제목</span>
      <span class="content">{{ info.title }}</span>
      <span class="subject">공개</span>
      <span class="content">{{ info.public }}</span>
      <span class="subject">속도</span>
      <span class="content">{{ info.speed }}</span>
      <span class="subject">인원</span>
      <span class="content">{{ info.per }}/6</span>
    </div>
    <!-- 오른쪽은 이미지와 테마가 있다. -->
    <div class="detail">
      <img src="@/assets/img/playboard.png" class="play-img">
      <span class="subject">테마</span>
      <span class="content">{{ info.theme }}</span>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
export default {
  setup() {
    const info = ref({
      title: "방 제목입니다.",
      public: "공개",
      speed: "보통",
      per: "4",
      theme: "설날",
    });

    return { info };
  },
};
</script>

<style>
@import "@/assets/css/room/waitingRoomRight.css";
</style>