<template>
  <!-- 닉네임 변경 모달 창 -->
  <div id="changeNicknameModal" class="modal">
    <div class="modal-content">
      <input type="text" placeholder="|" v-model="nickname" />
      <br />
      <h2 class="nickname-text">변경할 닉네임을 입력하시오</h2>
      <br />
      <button @click="closeModal('Nick')" class="modal-change-nickname-btn">확인</button>
      취소
    </div>
  </div>

  <!-- <label for="rememberMeCheckbox" @click="toggleCheckbox()">
    <input class="login-text" type="checkbox" id="rememberMeCheckbox" />
    <span style="color: white">로그인 유지</span>
  </label> -->

</template>

<script>
import { useUserStore } from "@/store/userStore";

export default {
  setup() {
    const store = useUserStore();

    return {
      closeModal: store.closeModal,
    };
  },
  data() {
    return {
      nickname: 'test',
    };
  },
  methods: {
    nicknameChangeTest() {
      this.$emit("nicknameChangeTest")
    }
  }
}
</script>

<style>
@import "@/assets/css/user/changeNick.css";
</style>