<template>
  <!-- <link rel="stylesheet" href="../assets//css//user/login.css" /> -->

  <!-- 로그인 전 ( 초기화면 ) -->
  <div v-if="!isLogin">
    <img src="../assets/logo/TitleText.png" class="game-image" />

    <div>
      <!-- 로그인 및 회원 가입 -->
      <div class="button-container">
        <button class="login-btn" @click="openModal('login')">Login</button>
        <button class="join-btn" @click="openModal('join')">Join</button>

        <!-- 로그인 모달 -->
        <transition name="fade">
          <LoginModal v-if="showLoginModal" @close-modal="closeModal" />
        </transition>

        <!-- 회원 가입 모달 -->
        <transition name="fade">
          <JoinModal v-if="showJoinModal" @close-modal="closeModal" />
        </transition>
      </div>
    </div>

    <!-- 로그인 후 ( 메인화면 ) -->`
    <div v-if="isLogin">
      <!-- 상단바 -->
      <!-- <HeaderBar/> -->
      <HomeView />
    </div>
  </div>
</template>

<script>
import LoginModal from "@/components/user/LoginModal.vue";
import JoinModal from "@/components/user/JoinModal.vue";

export default {
  components: {
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

<style scoped>
/* 모달 페이드 */
.fade-enter-from {
  /* 시작시 효과 */
  opacity: 0;
}

.fade-enter-active {
  /* 전체 단계에서 적용될 부분*/
  transition: all 0.5s;
}

.fade-enter-to {
  /* 끝나는 효과 */
  opacity: 1;
}

.fade-leave-from {
  opacity: 1;
}

.fade-leave-active {
  transition: all 0.5s;
}

.fade-leave-to {
  opacity: 0;
}

/* 이미지의 초기 위치와 스타일 설정 */
.game-image {
  position: absolute;
  top: 10%;
  left: 50%;
  transform: translateX(-50%);
}

.button-container {
  position: absolute;
  right: 10px;
  bottom: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  z-index: 2;
}

.login-btn,
.join-btn {
  z-index: 3;
  display: block;
  margin: 5px 0;
  padding: 10px 20px;
  background: transparent;
  color: #fff;
  border: none;
  text-align: center; /* 텍스트 가운데 정렬 */
  font-size: 3em;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  margin-right: 80%; /* 왼쪽 마진 추가 */
  font-family: "Palatino Linotype", "Book Antiqua", palatino, serif; /* Press Start 2P 글꼴 적용 */
}

.join-btn {
  padding: 10px 40px;
  margin-bottom: 80%;
}

/* 버튼 호버 효과 */
.login-btn:hover,
.join-btn:hover {
  color: #46c000; /* 글자 색상 변경 */
  cursor: url(https://cur.cursors-4u.net/cursors/cur-2/cur117.cur), auto !important;
}
</style>