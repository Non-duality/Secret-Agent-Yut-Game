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
        <p class="room-detail-subtitle">속도</p>
        <p v-if="roomDetail.speed === 1">느림</p>
        <p v-else-if="roomDetail.speed === 2">보통</p>
        <p v-else-if="roomDetail.speed === 3">빠름</p>
        <p class="room-detail-subtitle">인원</p>
        <p>{{ roomDetail.currentPlayers }} / 6</p>
      </div>
      <div class="room-detail-theme">
        <div>
          <img src="@/assets/img/playboard.png" alt="playboard">
        </div>
        <div>
          <p class="room-detail-subtitle">맵 테마</p>
          <p>{{ roomDetail.theme }}</p>
        </div>
      </div>
    </div>
    <button @click="joinGame(roomDetail)">참가</button>
  </div>
</template>

<script>
  export default {
    // 부모로부터 받아온 방 상세정보 데이터
    props: {
      roomDetail: Object
    },

    data() {
      return {
        // 임시 유저 데이터 (삭제 필요)
        userInfo: {
          userNickname: '수콩',
        }
      }
    },

    methods: {
      // 게임 참가 함수
      // 누르기 전에 방 DB 업데이트 하는 과정 필요할 듯
      joinGame(roomDetail) {
        // 인원이 다 찬 경우 접근 불가능
        if (roomDetail.currentPlayers === 6) {
          alert('해당 방 인원이 다 찼습니다!')
        } 
        // 방 접근 가능
        else {
          // 유저 정보 JSON으로 변형 (나중에는 store로 관리)
          const userInfoString = JSON.stringify(this.userInfo);

          this.$router.push({ name: 'wait', 
          params: { roomNum: roomDetail.id }, 
          // 나중에 user는 store로 관리 필요
          query: { userInfo: userInfoString, isManager: false } })
        }

      }
    }
  }
</script>

<style scoped>
@import "@/assets/css/room/roomInfo.css";
</style>