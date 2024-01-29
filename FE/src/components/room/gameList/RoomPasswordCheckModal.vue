<template>
  <!-- 비공개방 비밀번호 체크 모달 창 -->
  <div id="checkRoomPasswordModal" class="modal">
    <div class="modal-content">
      <input type="password" placeholder="|" v-model="roomPassword" />
      <h4 class="password-text">비밀번호를 입력하세요</h4>
      <div class="btn">
        <button @click="joinGame(roomPassword, roomNum)" class="confirm-btn" >확인</button>
        <button @click="closeModal('roomPasswordCheck')" class="cancel-btn">취소</button>
      </div>
    </div>
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
        // 비밀번호
        roomPassword: '',

        // 임시 유저 데이터 (삭제 필요)
        userInfo: {
          userNickname: '수콩',
        },

        roomNum: this.roomInfo.id

      };
    },

    methods: {
      // 모달 관리
      closeModal(modalType) {
        const roomStore = useRoomStore();
        roomStore.closeModal(modalType);
      },

      // 게임 참가 함수
      joinGame(roomPassword, roomNum) {
        // password가 맞을 경우
        if (this.roomInfo.password === roomPassword) {
          const roomStore = useRoomStore();
          const roomInfo = roomStore.roomListData.find(room => room.id === roomNum);
          // 인원이 다 찬 경우 접근 불가능
          if (roomInfo.currentPlayers === 6) {
          alert('해당 방 인원이 다 찼습니다!')
          } else {
            const userInfoString = JSON.stringify(this.userInfo);
            this.$router.push({ name: 'wait', 
            params: { roomNum: roomNum }, 
            // 나중에 user는 store로 관리 필요
            query: { userInfo: userInfoString, isManager: false } })
            // player가 들어갔으니 사람 추가
            roomStore.increasePlayerCount(roomNum);
          } 
        } else {
          alert('비밀번호가 맞지 않습니다.')
        }
        
      }
    }
  }
</script>

<style scoped>
@import "@/assets/css/room/roomPasswordCheckModal.css";
</style>