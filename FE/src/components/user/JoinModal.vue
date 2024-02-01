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
      <div class="password-error" v-if="isNotPasswordMatch">
        패스워드가 일치하지 않습니다.
      </div>

      <input
        type="text"
        placeholder="닉네임"
        id="nicknameInput"
        v-model="nickname"
        required
      /><br />

      <div class="gender-selection">
        <button
          id="male-button"
          class="gender-button"
          @click="selectGender('남')"
          :class="{ selected: gender === '남' }"
        >
          남성
        </button>
        <button
          id="female-button"
          class="gender-button"
          @click="selectGender('여')"
          :class="{ selected: gender === '여' }"
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
  data() {
    return {
      passwordMismatch: false,
      gender: null,
      email: null,
      password: null,
      passwordCheck: null,
      nickname: null,
      year: null,
      month: null,
      day: null,
    };
  },

  methods: {
    async performJoin() {
      // 이메일, 패스워드, 닉네임 등의 필드 값이 비어 있는지 확인합니다.
      if (
        !this.email ||
        !this.password ||
        !this.passwordCheck ||
        !this.nickname ||
        !this.gender ||
        !this.year ||
        !this.month ||
        !this.day
      ) {
        // 필수 필드 중 하나라도 비어 있다면 각 필드를 강조하고 알림을 표시합니다.
        if (!this.email) {
          // 이메일 필드가 비어있을 경우 해당 필드 강조
          const emailInput = document.getElementById("emailInput");
          emailInput.style.border = "2px solid red";
        }
        if (!this.password) {
          // 패스워드 필드가 비어있을 경우 해당 필드 강조
          const passwordInput = document.getElementById("passwordInput");
          passwordInput.style.border = "2px solid red";
        }
        if (!this.passwordCheck) {
          // 패스워드 체크 필드가 비어있을 경우 해당 필드 강조
          const passwordCheckInput = document.getElementById(
            "confirmPasswordInput"
          );
          passwordCheckInput.style.border = "2px solid red";
        }
        if (!this.nickname) {
          // 닉네임 필드가 비어있을 경우 해당 필드 강조
          const nicknameInput = document.getElementById("nicknameInput");
          nicknameInput.style.border = "2px solid red";
        }
        if (!this.gender) {
          // 성별이 선택되지 않았을 경우 성별 선택 버튼 강조
          const maleButton = document.getElementById("male-button");
          const femaleButton = document.getElementById("female-button");
          maleButton.style.border = "2px solid red";
          femaleButton.style.border = "2px solid red";
        }
        if (!this.year | !this.month | !this.day) {
          // 생년월일 필드가 비어있을 경우 해당 필드 강조
          const birthdateYear = document.getElementById("birthdateYear");
          const birthdateMonth = document.getElementById("birthdateMonth");
          const birthdateDay = document.getElementById("birthdateDay");
          birthdateYear.style.border = "2px solid red";
          birthdateMonth.style.border = "2px solid red";
          birthdateDay.style.border = "2px solid red";
        }

        // 필수 필드가 비어있는 경우 알림을 표시합니다.
        alert("필수 정보를 모두 입력하세요.");
        return;
         // 모든 정보가 제대로 되어 있을 경우 서버로 넘기기
      } else {
        const userStore = useUserStore();
        
        const joinUser = {
          email: this.email,
          nickname: this.nickname,
          password: this.password,
          birthDate: `${this.year}-${this.month.toString().padStart(2, '0')}-${this.day.toString().padStart(2, '0')}`,
          gender: this.gender
        }

        // 회원가입 전송
        useUserStore().userJoin(JSON.stringify(joinUser))

        // 회원가입창 모달 닫기
        userStore.closeModal('join')
      }

      // 비밀번호가 불일치하는 경우 알림을 표시합니다.
      if (this.passwordMismatch) {
        alert("비밀번호 일치 하지 않습니다.");
        return;
      }
    },

    selectGender(gender) {
      this.gender = gender;
    },

    populateDateOptions() {
      const birthdateYear = document.getElementById("birthdateYear");
      const birthdateMonth = document.getElementById("birthdateMonth");
      const birthdateDay = document.getElementById("birthdateDay");

      // 년도 옵션 추가 (예: 1980부터 2024까지)
      for (let year = 1980; year <= 2024; year++) {
        const option = document.createElement("option");
        option.value = year;
        option.textContent = year;
        birthdateYear.appendChild(option);
      }

      // 월 옵션 추가 (1월부터 12월까지)
      for (let month = 1; month <= 12; month++) {
        const option = document.createElement("option");
        option.value = month;
        option.textContent = month;
        birthdateMonth.appendChild(option);
      }

      // 일 옵션 추가 (1일부터 31일까지)
      for (let day = 1; day <= 31; day++) {
        const option = document.createElement("option");
        option.value = day;
        option.textContent = day;
        birthdateDay.appendChild(option);
      }
    },

    // 회원가입 창 닫기
    closeModal() {
      const userStore = useUserStore();
      userStore.closeModal("join");

    }
  },

  // 계속 패스워드 불일치 여부 판단
  computed: {
    isNotPasswordMatch() {
      return this.password !== this.passwordCheck;
    }
  },
  
  mounted() {
    this.populateDateOptions();
  },

};
</script>

<style scoped>
@import "../../assets/css/user/join.css";
</style>