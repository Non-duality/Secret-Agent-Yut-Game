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
import { storeToRefs } from "pinia";
import { useUserStore } from "@/store/userStore";
import { useRouter } from "vue-router";

export default {
  setup() {
    const userStore = useUserStore();
    const router = useRouter();

    const loginUser = ref({
      email: "",
      password: "",
    });

    const { isLogin } = storeToRefs(userStore);
    const { userLogin, getUserInfo } = userStore;

    const login = async () => {
      console.log("로그인 진행중..");
      await userLogin(loginUser.value);

      let token = sessionStorage.getItem("accessToken");

      if (isLogin) {
        getUserInfo(token);
        closeModal();
        userStore.toggleNav();
        router.push("/home");
      } else {
        router.push("/");
      }
    };

    // 회원가입으로 이동.
    const join = () => {
      closeModal();
      userStore.openModal("join");
    };

    // pinia 닫기 함수 호출
    const closeModal = () => {
      userStore.closeModal("login");
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