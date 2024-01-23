<template>
  <link rel="stylesheet" href="./src/assets/css/main.css" />
  <link rel="stylesheet" href="./src/assets/css/user/login.css" />

  <!-- 로그인 전 ( 초기화면 ) -->
  <div v-if="!isLogin">
    <div>
      <img class="main-title" src="../assets/logo/TitleText.png" />
    </div>

    <div>
      <!-- 로그인 및 회원 가입 -->
      <div class="button-container">
        <button class="login-btn" @click="openModal('login')">Login</button>
        <button class="join-btn" @click="openModal('join')">Join</button>

        <!-- 로그인 모달 -->
        <LoginModal v-if="showLoginModal" @close-modal="closeModal"/>

        <!-- 회원 가입 모달 -->
        <JoinModal v-if="showJoinModal" @close-modal="closeModal"/>
      </div>
    </div>
  </div>

  <!-- 로그인 후 ( 메인화면 ) -->
  <div v-if="isLogin">
    <!-- 상단바 -->
    <!-- <HeaderBar/> -->
    <!-- HomeView -->
    <HomeView />
  </div>
</template>

<script>
console.log("Main.vue");
import HomeView from "@/view/Home/HomeView.vue";
import LoginModal from "@/components/user/LoginModal.vue";
import JoinModal from "@/components/user/JoinModal.vue";

export default {
  components: {
    HomeView, // HomeView 컴포넌트를 등록합니다.
    LoginModal,
    JoinModal,
  },
  data() {
    return {
      isLogin: false,
      showLoginModal: false,
      showJoinModal: false,
    };
  },

  methods: {
    openModal(value) {
      if (value === "login") {
        this.showLoginModal = true;
      } else if (value === "join") {
        this.showJoinModal = true;
      }
    },

    closeModal(value) {
      if (value === "login") {
        this.showLoginModal = false;
      } else if (value === "join") {
        this.showJoinModal = false;
      }
    },
  },
};
</script>
