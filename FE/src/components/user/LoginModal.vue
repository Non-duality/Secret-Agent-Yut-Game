<template>
  <link rel="stylesheet" href="./src/assets/css/user/modal.css" />

  <!-- 로그인 모달 창 -->
  <div class="modal">
    <div class="modal-content">
      <button class="close" @click="closeModal('login')">&times;</button>
      <h2 class="login-text">Login</h2>
      <input type="text" placeholder="이메일" />
      <input type="password" placeholder="패스워드" />
      <br />
      <label for="rememberMeCheckbox" @click="toggleCheckbox()">
        <input class="login-text" type="checkbox" id="rememberMeCheckbox" />
        <span style="color: white">로그인 유지</span>
      </label>
      <br />
      <button @click="performLogin()" class="modal-login-btn">로그인</button>
      <button @click="closeModal('login')" class="modal-cancel-btn">취소</button>
    </div>
  </div>
</template>
  
<script>
export default {
  methods: {
    closeModal(modalId) {
      this.$emit("close-modal", modalId);
    },
  },
};
</script>