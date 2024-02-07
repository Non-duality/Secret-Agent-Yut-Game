<template>
  <div class="nav-pro">
    <!-- 사용자가 설정한 사진을 보여준다 -->
    <div class="nav-img">
      <img src="../../assets/icon/user.png" class="img" />
    </div>
    <!-- 사용자의 정보를 간략히 보여준다 -->
    <div class="nav-info">
      <div class="nav-name">
        <span>{{ userName }}님</span>
      </div>
      <!-- 클릭 시 프로필로 화면을 이동한다 -->
      <div class="nav-my">
        <router-link @click="initializeData" to="/" class="nav-btn"
          >로그아웃</router-link
        >
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "@/store/userStore";

export default {
  data() {
    return {};
  },

  methods: {
    // 로그아웃하기.
    initializeData() {
      const confirmMessage = "정말 로그아웃 하시겠습니까?";

      if (confirm(confirmMessage)) {
        useUserStore().initData();
        this.$router.push("/");
      }
    },
  },

  computed: {
    userName() {
      const userInfo = useUserStore().userInfo;

      // userInfo를 이용한 로직
      return userInfo ? userInfo.nickname : "로딩 중...";
    },
  },
};
</script>

<style scoped>
@import "../../assets/css/layout/navPro.css";
</style>
