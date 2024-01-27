<template>
  <!-- 실제 게임 방 목록 하나 -->
  <div class="room-list-component">
    <td>{{ roomInfo.currentPlayers }} / 6</td>
    <td>{{ roomInfo.roomName }}</td>
    <td>
      <span v-if="roomInfo.isPublic">공개</span>
      <span v-else>비공개</span>
    </td>
  </div>
</template>

<script>
  export default {
    // 부모로부터 받아온 방 상세정보 데이터
    props: {
      roomInfo: Object
    },
  }
</script>

<style scoped>
@import "@/assets/css/room/roomListComponent.css";
</style>