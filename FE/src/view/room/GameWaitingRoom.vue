<template>
  <div class="wait-div">
    <!-- 왼쪽 사용자 화면이다 참가한 유저가 보이고 채팅이 보인다 -->
    <div class="wait-left"><LeftComponentsVue /></div>
    <!-- 방 상세 정보와 준비, 나가기 버튼이 있다 -->
    <div class="wait-right">
      <RightComponentsVue />
      <button class="wait-btn">준비</button>
      <button class="wait-btn">나가기</button>
    </div>
  </div>
  <!-- 라우터 설정이 아직 안되어있어 테스트 용으로 나둠.. 나중에 삭제해야함 -->
  <button @click="goTolist">nav</button>
</template>

<script>
import { useUserStore } from "@/store/userStore";
import { useRouter } from "vue-router";
import RightComponentsVue from "@/components/room/gameWait/RightComponents.vue";
import LeftComponentsVue from "@/components/room/gameWait/LeftComponents.vue";

export default {
  components: {
    RightComponentsVue, // 방 상세
    LeftComponentsVue,  // 참여 유저 정보
  },
  setup() {
    const store = useUserStore();
    const router = useRouter();

    const goTolist = () => {
      store.toggleNav();
      // router.push("/room");
    };

    return {
      goTolist,
    };
  },
};
</script>

<style>
@import "@/assets/css/room/waitingRoom.css";
</style>