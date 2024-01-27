<script>
import NavPro from "./NavPro.vue";

// 프로필은 따로 가져오기 위해 선언
export default {
  components: {
    NavPro,
  },
};
</script>

<template>
  <div class="nav-bar">
    <!-- 로고 -->
    <div>
      <img src="../../assets/logo/logo.png" class="img-log" />
    </div>
    <!-- 홈, 게임, 게시판 클릭시 화면 이동 -->
    <!-- Named Routes로 변경 -->
    <div class="text-box">
      <Router-link :to="{ name: 'home' }" class="nav-text">홈</Router-link>
      <Router-link :to="{ name: 'room' }" class="nav-text">게임</Router-link>
      <Router-link :to="{ name: 'home' }" class="nav-text">게시판</Router-link>
    </div>
    <!-- 프로필 가져와서 보여주기 -->
    <div class="pro-box">
      <NavPro />
    </div>
  </div>
</template>

<style>
@import "../../assets/css/layout/navBar.css";
</style>