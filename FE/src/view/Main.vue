<template>
  <!-- 게임 중이거나 로그인 전이면 숨기기 위해 if문 사용 -->
  <header v-if="showModalSide">
    <NavBar />
    <SideBar/>
  </header>
  <main>
    <router-view />
  </main>
</template>

<script>
import NavBar from "@/components/layout/NavBar.vue";
import SideBar from "@/components/layout/SideBar.vue";
import { useUserStore } from "@/store/userStore";
import { storeToRefs } from "pinia";

export default {
  components: {
    NavBar,
    SideBar,
  },
  setup() {
    const store = useUserStore();
    const { showModalSide } = storeToRefs(store);

    return {
      showModalSide,  // 네비 바와 사이드 바를 숨기고 나타내기 위해 선언
    };
  },
};
</script>