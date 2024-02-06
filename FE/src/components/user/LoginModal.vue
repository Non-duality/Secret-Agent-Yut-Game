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
import { useRoomStore } from "@/store/roomStore";
import { useRouter } from "vue-router";
import { connectWS } from "@/util/socket.js"

export default {
  setup() {
    const router = useRouter();

    const loginUser = ref({
      email: "",
      password: "",
    });

    const login = async () => {
      await useUserStore().userLogin(loginUser.value);

      if (useUserStore().isLogin) {
        try {
          await connectWS();

          if (useRoomStore().isConnected) {
            useUserStore().getUserInfo();
            closeModal();
            useUserStore().showModalSide = true;
            router.push("/home");
          } else {
            useUserStore().isLogin = false;
            useUserStore().initData();
            router.push("/");
          }
        } catch (error) {
          useUserStore().isLogin = false;
          useUserStore().initData();
          router.push("/");
          console.error("Error:", error);
        }
      } else {
        useUserStore().isLogin = false;
        useUserStore().initData();
        router.push("/");
      }
    };

    // 회원가입으로 이동.
    const join = () => {
      closeModal();
      useUserStore().openModal("join");
    };

    // pinia 닫기 함수 호출
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