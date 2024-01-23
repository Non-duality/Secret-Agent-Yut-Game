<template>
  <!-- 로그인 전 ( 초기화면 ) -->
  <div v-if="isLogin">
    <link rel="stylesheet" href="./src/assets/css/main.css" />

    <div>
      <img class="main-title" src="../assets/logo/TitleText.png" />
    </div>

    <div>
      <!-- 로그인 및 회원 가입 -->
      <div class="button-container">
        <button class="login-btn" @click="openModal('login')">Login</button>
        <button class="join-btn" @click="openModal('join')">Join</button>

        <!-- 로그인 모달 -->
        <LoginModal v-if="showLoginModal" @close-modal="closeModal" />

        <!-- 회원 가입 모달 -->
        <JoinModal v-if="showJoinModal" @close-modal="closeModal" />
      </div>
    </div>
  </div>

  <!-- 로그인 후 ( 메인화면 ) -->
  <div v-if="!isLogin">
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
      isLogin: true,
      showLoginModal: false,
      showJoinModal: false,
    };
  },

  methods: {
    openModal(value) {
      var modal;

      if (value === "login") {
        modal = document.getElementById("loginModal");
      } else {
        modal = document.getElementById("joinModal");
      }

      modal.style.display = "block";

      // 모달 열 때 입력 필드 초기화
      const inputs = modal.querySelectorAll("input");
      inputs.forEach((input) => {
        input.value = "";
      });

      setTimeout(function () {
        modal.classList.add("show"); // 열 때 애니메이션 클래스 추가
      }, 100); // 애니메이션 지속 시간 (1초) 후에 display 속성 변경
    },
    closeModal(value) {
      var modal;

      if (value === "login") {
        modal = document.getElementById("loginModal");
      } else {
        modal = document.getElementById("joinModal");
      }

      modal.classList.remove("show"); // 애니메이션 클래스 제거

      // 모달 열 때 입력 필드 초기화
      const inputs = modal.querySelectorAll("input");
      inputs.forEach((input) => {
        input.value = "";
      });

      setTimeout(function () {
        modal.style.display = "none";
      }, 500); // 애니메이션 지속 시간 (0.5초) 후에 display 속성 변경
    },
    performLogin() {
      // 로그인 로직 구현
    },
    performSignup() {
      // 회원 가입 로직 구현
    },
  },
  onMounted() {},
};
</script>
