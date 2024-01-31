<template>
  <div class="nav-pro">
    <!-- 사용자가 설정한 사진을 보여준다 -->
    <div class="nav-img">
      <img
        src="../../assets/icon/user.png"
        @click="goToUserProfile"
        class="img"
      />
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
    return {
    };
  },

  methods: {
    goToUserProfile() {
      // 사용자 프로필 페이지로 이동
      this.$router.push("/user");
    },

    initializeData() {
      useUserStore().initData();
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

<style>
@import "../../assets/css/layout/navPro.css";
</style>
