<template>
  <div class="nav-pro">
    <!-- 사용자가 설정한 사진을 보여준다 -->
    <div class="nav-img">
      <img src="../../assets/icon/user.png" class="img" />
    </div>
    <!-- 사용자의 정보를 간략히 보여준다 -->
    <div class="nav-info">
      <div class="nav-name">
        <span>{{ userName }}</span>
      </div>
      <!-- 클릭 시 프로필로 화면을 이동한다 -->
      <div class="nav-my">
        <router-link to="/user" class="nav-btn">내정보</router-link>
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

  computed: {
    userName() {
      const userInfo = useUserStore().userInfo;
      return userInfo ? userInfo.name : "";
    },
  },
};
</script>

<style>
@import "../../assets/css/layout/navPro.css";
</style>