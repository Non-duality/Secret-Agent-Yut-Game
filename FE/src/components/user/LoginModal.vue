<template>
  <!-- 로그인 모달 창 -->
  <div id="loginModal" class="modal">
    <div class="modal-content">
      <button class="close" @click="closeModal">&times;</button>
      <h2 class="login-text">Login</h2>
      <input type="text" placeholder="이메일" v-model="email" />
      <input type="password" placeholder="패스워드" v-model="password" />
      <br />
      <label for="rememberMeCheckbox" @click="toggleCheckbox()">
        <input class="login-text" type="checkbox" id="rememberMeCheckbox" />
        <span style="color: white">로그인 유지</span>
      </label>
      <br />
      <button @click="loginTest" class="modal-login-btn">로그인</button>
      <button @click="loginJoin" class="modal-cancel-btn">
        회원가입
      </button>
    </div>
  </div>
</template>
  
<script>
import { useUserStore } from "@/store/userStore";
import { useRouter } from "vue-router";

export default {
  data() {
    return {
      email: null,
      password: null,
    };
  },
  setup() {
    const store = useUserStore();
    const router = useRouter();

    // pinia 닫기 함수 호출
    const closeModal = () => {
      store.closeModal('login');
    }

    // 로그인 테스트용 
    // 백과 연결되면 수정해야 함.
    // 라우터 이동.
    const loginTest = () => {
      closeModal();
      store.toggleNav();
      router.push("/home");
    }

    // 회원가입으로 이동.
    const loginJoin = () => {
      closeModal();
      store.openModal('join');
    }

    return {
      closeModal,
      loginTest,
      loginJoin,
    };
  },
};
</script>

<style scoped>
@import "../../assets/css/user/login.css";
</style>