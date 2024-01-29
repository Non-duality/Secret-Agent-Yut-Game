<template>
  <div class="wait-div">
    <!-- 왼쪽 사용자 화면이다 참가한 유저가 보이고 채팅이 보인다 -->
    <div class="wait-left"><LeftComponentsVue /></div>
    <!-- 방 상세 정보와 준비, 나가기 버튼이 있다 -->
    <div class="wait-right">
      <RightComponentsVue />
      <button class="wait-btn" v-if="isManager === true">시작</button>
      <button class="wait-btn" v-else>준비</button>
      <button class="wait-btn" @click="goToList">나가기</button>
    </div>
  </div>
</template>

<script>
  // 방 정보 데이터
  import { useRoomStore } from '@/store/roomStore'

  // 유저 정보
  import { useUserStore } from "@/store/userStore";

  // 자식 컴포넌트
  import RightComponentsVue from "@/components/room/gameWait/RightComponents.vue";
  import LeftComponentsVue from "@/components/room/gameWait/LeftComponents.vue";

  export default {
    components: {
      RightComponentsVue, // 방 상세
      LeftComponentsVue,  // 참여 유저 정보
    },

    data() {
      return {
        // 방장 여부
        isManager: false,
        // 방 정보
        roomInfo: null
      }
    },
    
    methods: {
      // 나갈 때 로직
      goToList() {
        const roomStore = useRoomStore();
        const roomNum = Number(this.$route.params.roomNum);
        const roomInfo = roomStore.roomListData.find(room => room.id === roomNum);

        if (roomInfo) {
          // 인원 감소
          roomStore.decreasePlayerCount(roomInfo.id);
          // 만약 currentPlayers가 0이면 방 삭제
          roomStore.removeEmptyRoom(roomInfo.id);
        }
        // 나가기
        this.navigateToRoomList();
      },

      // 나가기 라우터
      navigateToRoomList() {
        this.$router.push({ name: 'list' })
      }

    },

    created() {
      // URL query의 isManager 가져와서 방장 여부 판단
      this.isManager = this.$route.query.isManager === 'true';
      // URL params의 roomNum 가져오기
      const roomNum = Number(this.$route.params.roomNum);
      // 스토어 선언
      const roomStore = useRoomStore()
      // 스토어에서 해당 ID 방 정보 찾기
      this.roomInfo = roomStore.roomListData.find(room => room.id === roomNum);
    },
  };
</script>

<style>
@import "@/assets/css/room/waitingRoom.css";
</style>