<template>
  <!-- 방 클릭했을 때 나오는 상세 정보 -->
  <div class="room-detail">
    <div class="room-detail-content">
      <div class="room-detail-info">
        <p class="room-detail-subtitle">방 제목</p>
        <p>{{ roomDetail.roomName }}</p>
        <p class="room-detail-subtitle">공개</p>
        <p v-if="roomDetail.isPublic">공개</p>
        <p v-else>비공개</p>
        <p class="room-detail-subtitle">인원</p>
        <p>{{ roomDetail.currentPlayers }} / 6</p>
      </div>
      <div class="room-detail-theme">
        <p class="room-detail-subtitle">맵 테마</p>
        <p>{{ roomDetail.theme }}</p>
      </div>
    </div>
    <button>참가</button>
  </div>
</template>

<script>
// 부모로부터 받아온 방 상세정보 데이터
  export default {
    props: {
      roomDetail: Object
    }
  }
</script>

<style scoped>
@import "@/assets/css/room/roomInfo.css";
</style>