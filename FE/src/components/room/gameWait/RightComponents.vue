<template>
  <div class="wait-detail">
    <!-- 왼쪽 오른쪽으로 나누었다 -->
    <!-- 왼쪽에는 기본 정보가 있다 -->
    <div class="detail">
      <span class="subject">방 제목</span>
      <span class="content">{{ roomInfo.title }}</span>
      <span class="subject">공개</span>
      <span class="content" v-if="roomInfo.isPublic">공개</span>
      <span class="content" v-else>비공개</span>
      <span class="subject">속도</span>
      <span class="content" v-if="roomInfo.speed === 1">느림</span>
      <span class="content" v-else-if="roomInfo.speed === 2">보통</span>
      <span class="content" v-else-if="roomInfo.speed === 3">빠름</span>
      <span class="subject">인원</span>
      <span class="content">{{ roomInfo.currentPlayers }}/6</span>
    </div>
    <!-- 오른쪽은 이미지와 테마가 있다. -->
    <div class="detail">
      <img src="@/assets/img/playboard.png" class="play-img">
      <span class="subject">테마</span>
      <span class="content" v-if="roomInfo.theme === 'lunaNewYear'">설날</span>
      <span class="content" v-else-if="roomInfo.theme === 'chuseok'">추석</span>
    </div>
  </div>
</template>

<script>
  // 방 정보 데이터
  import { useRoomStore } from '@/store/roomStore'

  export default {
    data() {
      return {
        roomInfo: null
      }
    },

    created() {
      // 스토어에서 인스턴스 가져오기
      const roomStore = useRoomStore();

      // URL parmas의 roomNum 가져오기
      const roomNum = Number(this.$route.params.roomNum);

      // 스토어에서 해당 ID 방 정보 찾기
      this.roomInfo = roomStore.roomListData.find(room => room.id === roomNum);
    }
  };
</script>

<style>
@import "@/assets/css/room/waitingRoomRight.css";
</style>