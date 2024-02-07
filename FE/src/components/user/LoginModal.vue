<template>
  <!-- 로그인 모달 창 -->
  <div id="loginModal" class="modal">
    <div class="modal-content">
      <button class="close" @click="closeModal">&times;</button>
      <h2 class="login-text">Login</h2>
      <input type="text" placeholder="이메일" v-model="loginUser.email" />
      <input
        type="password"
        placeholder="패스워드"
        v-model="loginUser.password"
      />
      <br />
      <label for="rememberMeCheckbox" @click="toggleCheckbox()">
        <input class="login-text" type="checkbox" id="rememberMeCheckbox" />
        <span style="color: white">로그인 유지</span>
      </label>
      <br />
      <button @click="login()" class="modal-login-btn">로그인</button>
      <button @click="join()" class="modal-cancel-btn">회원가입</button>
    </div>
  </div>
</template>
  
<script>
import { ref } from "vue";
import { useUserStore } from "@/store/userStore";
import { useRouter } from "vue-router";

export default {
  setup() {
    const router = useRouter();

    const loginUser = ref({
      email: "",
      password: "",
    });

    // (1) 로그인을 수행한다.
    const login = async () => {
      // (2) 비동기 통신으로 로그인 정보를 전달한다.
      await useUserStore().userLogin(loginUser.value);

      // (3) 만약 로그인이 되었다면, isLogin은 True가 된다.
      if (useUserStore().isLogin) {
        // (4) 유저 정보를 가져온다.
        useUserStore().getUserInfo();

        // (4-1) 모달을 닫는다.
        closeModal();

        // (4-2) 상단바와 사이드바를 나타낸다.
        useUserStore().showModalSide = true;

        // (5) 홈으로 이동한다.
        router.push("/home");
      } else {
        // (3) 만약 로그인이 실패한다면, 초기화면으로 이동한다.
        useUserStore().isLogin = false;
        useUserStore().initData();
        router.push("/");
      }
    };

    // 회원가입으로 이동한다.
    const join = () => {
      closeModal();
      useUserStore().openModal("join");
    };

    // 현재 실행된 모달을 닫는다.
    const closeModal = () => {
      useUserStore().closeModal("login");
    };

    return {
      loginUser,
      closeModal,
      login,
      join,
    };
  },
};
</script>

<style scoped>
@import "../../assets/css/user/login.css";
</style>