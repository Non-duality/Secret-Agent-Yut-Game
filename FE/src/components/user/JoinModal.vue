<template>
  <link rel="stylesheet" href="./src/assets/css/user/modal.css" />

  <!-- 회원가입 모달 창 -->
  <div id="joinModal" class="modal">
    <div class="modal-content">
      <button class="close" onclick="closeModal('join')">&times;</button>
      <h2 class="join-text">Join</h2>
      <input type="text" placeholder="이메일" />
      <input
        type="password"
        placeholder="패스워드"
        id="passwordInput"
        required
      /><br />
      <input
        type="password"
        placeholder="패스워드 확인"
        id="confirmPasswordInput"
        required
      /><br />

      <input
        type="text"
        placeholder="닉네임"
        id="nicknameInput"
        required
      /><br />

      <!-- 년도, 월, 일을 나란하게 표시 -->
      <div>
        <select id="birthdateYear" required></select>

        <select id="birthdateMonth" required></select>

        <select id="birthdateDay" required></select>
      </div>

      <div class="gender-selection">
        <button id="male-button" class="gender-button">남성</button>
        <button id="female-button" class="gender-button">여성</button>
      </div>

      <button class="modal-join-btn">회원가입</button>
      <button class="modal-cancel-btn" onclick="closeModal('join')">
        취소
      </button>
    </div>
  </div>
</template>

<script>
</script>
