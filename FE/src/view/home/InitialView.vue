<template>
  <img
    src="@/assets/logo/TitleText.png"
    class="game-image"
    :class="{ animated: isAnimated }"
  />

  <div>
    <!-- 로그인 및 회원 가입 -->
    <div class="button-container">
      <button class="login-btn" @click="openModal('login')">Login</button>
      <button class="join-btn" @click="openModal('join')">Join</button>

      <!-- 로그인 모달 -->
      <transition name="fade">
        <LoginModal v-if="showLoginModal" />
      </transition>

      <!-- 회원 가입 모달 -->
      <transition name="fade">
        <JoinModal v-if="showJoinModal" />
      </transition>
    </div>
  </div>
</template>

<script>
import LoginModal from "@/components/user/LoginModal.vue";
import JoinModal from "@/components/user/JoinModal.vue";
import { useUserStore } from "@/store/userStore";
import { storeToRefs } from "pinia";

export default {
  components: {
    LoginModal,
    JoinModal,
  },
  data() {
    return {
      isAnimated: true,
    };
  },
  setup() {
    const store = useUserStore();
    const { isLogin, showLoginModal, showJoinModal } = storeToRefs(store);

    return {
      isLogin,
      showLoginModal,
      showJoinModal,
      openModal: store.openModal,
      closeModal: store.closeModal,
      loginTest: store.loginTest,
    };
  },
};
</script>

<style>
@import url("../../assets/css/home/initial.css");
</style>