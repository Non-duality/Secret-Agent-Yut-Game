<template>
  <div class="wait-div">
    <!-- 왼쪽 사용자 화면이다 참가한 유저가 보이고 채팅이 보인다 -->
    <div class="wait-left">
      <LeftComponentsVue />
    </div>
    <!-- 방 상세 정보와 준비, 나가기 버튼이 있다 -->
    <div class="wait-right">
      <RightComponentsVue />
      <button class="wait-btn">시작</button>
      <!-- <button class="wait-btn">준비</button> -->
      <button class="wait-btn" @click="goTolist">나가기</button>
    </div>
    <!-- <banModal @close-ban-modal="closeBanModal" v-if="ban"/> -->
  </div>
</template>

<script>
import { pubRoom } from "@/util/socket.js";
import { useUserStore } from "@/store/userStore";
import { useRoomStore } from "@/store/roomStore";

// 자식 컴포넌트
import RightComponentsVue from "@/components/room/gameWait/RightComponents.vue";
import LeftComponentsVue from "@/components/room/gameWait/LeftComponents.vue";
// import banModal from '@/components/room/gameWait/banModal.vue';

export default {
  components: {
    RightComponentsVue, // 방 상세
    LeftComponentsVue, // 참여 유저 정보
    // banModal // 금지 모달
  },

  data() {
    return {
      // 방 정보
      roomInfo: null,
    };
  },

  methods: {
    // 나갈 때 로직
    goTolist() {
      // 방을 나가기 위해 pub로 알린다.
      pubRoom(
        "/pub/room/" + useUserStore().currentRoomInfo.roomCode + "/exit",
        useUserStore().userInfo.email
      );

      // 구독 취소한 뒤 방 정보에 대해 모두 리셋한다.
      useRoomStore().subscription.room.unsubscribe();
      const initialStateRoom = useRoomStore().$reset();
      Object.assign(this, initialStateRoom);

      this.$router.push({ name: "room" });
    },
  },
};
</script>

<style scoped>
@import "@/assets/css/room/waitingRoom.css";
</style>