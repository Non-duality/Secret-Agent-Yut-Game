<template>
  <!-- 회원가입 모달 창 -->
  <div id="joinModal" class="modal">
    <div class="modal-content">
      <button class="close" @click="closeModal('join')">&times;</button>
      <h2 class="join-text">Join</h2>
      <input id="emailInput" type="text" placeholder="이메일" v-model="email" />
      <input
        type="password"
        placeholder="패스워드"
        id="passwordInput"
        v-model="password"
        required
      /><br />
      <input
        type="password"
        placeholder="패스워드 확인"
        id="confirmPasswordInput"
        @input="checkPassword"
        v-model="passwordCheck"
        required
      /><br />
      <div class="password-error" v-if="passwordMismatch">
        패스워드가 일치하지 않습니다.
      </div>

      <input
        type="text"
        placeholder="닉네임"
        id="nicknameInput"
        v-model="name"
        required
      /><br />

      <div class="gender-selection">
        <button
          id="male-button"
          class="gender-button"
          @click="selectGender('male')"
          :class="{ selected: selectedGender === 'male' }"
        >
          남성
        </button>
        <button
          id="female-button"
          class="gender-button"
          @click="selectGender('female')"
          :class="{ selected: selectedGender === 'female' }"
        >
          여성
        </button>
      </div>
      <!-- 년도, 월, 일을 나란하게 표시 -->
      <div>
        <select id="birthdateYear" v-model="year" required></select>

        <select id="birthdateMonth" v-model="month" required></select>

        <select id="birthdateDay" v-model="day" required></select>
      </div>

      <button class="modal-join-btn" @click="performJoin">회원가입</button>
      <button class="modal-cancel-btn" @click="closeModal">취소</button>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "@/store/userStore";

export default {
  setup() {
    const store = useUserStore();
    // 닫기 버튼 누르면 pinia에 저장되어 있는 함수 호출.
    const closeModal = () => {
      store.closeModal('join');
    }
    return {
      closeModal,
    };
  },
};
</script>

<style scoped>
@import "../../assets/css/user/join.css";
</style>