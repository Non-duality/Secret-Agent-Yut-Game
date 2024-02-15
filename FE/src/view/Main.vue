<template>
  <transition name="fade" mode="out-in">
    <!-- 게임 중이거나 로그인 전이면 숨기기 위해 if문 사용 -->
    <header v-if="showModalSide">
      <NavBar />
      <SideBar />
    </header>
  </transition>
  <main>
    <transition name="fade" mode="out-in">
      <router-view />
    </transition>
  </main>
</template>

<script>
import NavBar from "@/components/layout/NavBar.vue";
import SideBar from "@/components/layout/SideBar.vue";
import { useUserStore } from "@/store/userStore";
import { storeToRefs } from "pinia";
import { connectWebSocket } from "@/util/socket";
import { onMounted } from "vue";

export default {
  components: {
    NavBar,
    SideBar,
  },

  setup() {
    const store = useUserStore();
    const { showModalSide } = storeToRefs(store);

    onMounted(() => {
      // 새로고침 할 때 소켓 재연결
      const userString = localStorage.getItem('user')
      if (userString) {
        const user = JSON.parse(userString)
        if (user && user.accessToken)
        connectWebSocket(user.accessToken).then(() => {
        }).catch((error) => {
          console.error("WebSocket 재연결 실패:", error);
        })
      }
    })

    return {
      showModalSide, // 네비 바와 사이드 바를 숨기고 나타내기 위해 선언
    };
  },
};
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.85s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>