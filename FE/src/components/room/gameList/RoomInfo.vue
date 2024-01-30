<template>
  <!-- 방 클릭했을 때 나오는 상세 정보 -->
  <div v-if=" Object.keys(roomInfo).length > 0" class="room-detail">
    <div class="room-detail-content">
      <div class="room-detail-info">
        <p class="room-detail-subtitle">방 제목</p>
        <p>{{ roomInfo.title }}</p>
        <p class="room-detail-subtitle">공개</p>
        <p v-if="roomInfo.isPublic">공개</p>
        <p v-else>비공개</p>
        <p class="room-detail-subtitle">속도</p>
        <p v-if="roomInfo.speed === 1">느림</p>
        <p v-else-if="roomInfo.speed === 2">보통</p>
        <p v-else-if="roomInfo.speed === 3">빠름</p>
        <p class="room-detail-subtitle">인원</p>
        <p>{{ roomInfo.currentPlayers }} / 6</p>
      </div>
      <div class="room-detail-theme">
        <div>
          <img src="@/assets/img/playboard.png" alt="playboard">
        </div>
        <div>
          <p class="room-detail-subtitle">맵 테마</p>
          <p v-if="roomInfo.theme === 'lunaNewYear'">설날</p>
          <p v-else-if="roomInfo.theme === 'chuseok'">추석</p>
        </div>
      </div>
    </div>
    <!-- 예시 방 디테일일 때는 참가 버튼 없애기 -->
    <button @click="joinGame(roomNum)">참가</button>
  </div>
  <div v-else class="room-detail">
    즐거운 윷놀이 한 판 가보자고~!
  </div>
</template>

<script>
  import { useRoomStore } from '@/store/roomStore';

  export default {
    // 부모로부터 받아온 방 상세정보 데이터
    props: {
      roomInfo: Object
    },

    data() {
      return {
        // 임시 유저 데이터 (삭제 필요)
        userInfo: {
          userNickname: '수콩',
        },

        roomNum: this.roomInfo.id
      }
    },

    methods: {
      // 게임 참가 함수
      joinGame(roomNum) {
        const roomStore = useRoomStore();
        const roomInfo = roomStore.roomListData.find(room => room.id === roomNum);
        // 인원이 다 찬 경우 접근 불가능
        if (roomInfo.currentPlayers === 6) {
          alert('해당 방 인원이 다 찼습니다!')
        } else {
          // 유저 정보 JSON으로 변형 (나중에는 store로 관리)
          const userInfoString = JSON.stringify(this.userInfo);
          // 공개 방 일 때
          if ( roomInfo.isPublic ) {
            this.$router.push({ name: 'wait', 
            params: { roomNum: roomNum }, 
            // 나중에 user는 store로 관리 필요
            query: { userInfo: userInfoString, isManager: false } })
            // player가 들어갔으니 사람 추가
            roomStore.increasePlayerCount(roomNum);
          } 
          // 비공개 방 일 때
          else {
            this.openModal('roomPasswordCheck')
          }
        }
      },

      // 모달 관리
      openModal(modalType) {
        const roomStore = useRoomStore();
        roomStore.openModal(modalType);
      },
    }
  }
</script>

<style scoped>
@import "@/assets/css/room/roomInfo.css";
</style>