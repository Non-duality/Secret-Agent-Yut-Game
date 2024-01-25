<template>
  <div>
    <h2>상세정보</h2>
    <div>
      <div>
        <p>방 제목</p>
        <p>{{ roomDetail.roomName }}</p>
        <p>공개</p>
        <p v-if="roomDetail.isPublic">공개</p>
        <p v-else>비공개</p>
        <p>인원</p>
        <p>{{ roomDetail.currentPlayers }} / 6</p>
      </div>
      <div>
        <p>맵 테마</p>
        <p>{{ roomDetail.theme }}</p>
      </div>
      <button>참가</button>
    </div>
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

</style>