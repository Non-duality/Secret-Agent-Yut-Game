<template>
  <tr>
    <td>{{ roomInfo.currentPlayers }} / 6</td>
    <td>{{ roomInfo.roomName }}</td>
    <td>
      <span v-if="roomInfo.isPublic">공개</span>
      <span v-else>비공개</span>
    </td>
  </tr>
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

</style>