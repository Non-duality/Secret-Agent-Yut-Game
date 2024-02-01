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
import { ref } from "vue";
import { storeToRefs } from "pinia";
import { useUserStore } from "@/store/userStore";
import { useRouter } from "vue-router";

export default {
  setup() {
    const userStore = useUserStore();

    const { userLogin } = userStore;

    const join = async () => {};

    // pinia 닫기 함수 호출
    const closeModal = () => {
      userStore.closeModal("join");
    };

    return {
      closeModal,
    };
  },

  data() {
    return {
      passwordMismatch: false,
      selectedGender: null,
      userId: null,
      password: null,
      passwordCheck: null,
      name: null,
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
        !this.name ||
        !this.selectedGender ||
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
          // 패스워드 필드가 비어있을 경우 해당 필드 강조
          const passwordCheckInput = document.getElementById(
            "confirmPasswordInput"
          );
          passwordCheckInput.style.border = "2px solid red";
        }
        if (!this.name) {
          // 닉네임 필드가 비어있을 경우 해당 필드 강조
          const nicknameInput = document.getElementById("nicknameInput");
          nicknameInput.style.border = "2px solid red";
        }
        if (!this.selectedGender) {
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
      }

      if (this.passwordMismatch) {
        alert("비밀번호 일치 하지 않습니다.");
        return;
      }
    },

    checkPassword() {
      const passwordInput = document.getElementById("passwordInput").value;
      const confirmPasswordInput = document.getElementById(
        "confirmPasswordInput"
      ).value;

      if (passwordInput !== confirmPasswordInput) {
        // 패스워드가 일치하지 않으면 불일치 여부를 true로 설정하여 메시지 표시
        this.passwordMismatch = true;
      } else {
        // 패스워드가 일치하면 불일치 여부를 false로 설정하여 메시지 감춤
        this.passwordMismatch = false;
      }
    },

    selectGender(gender) {
      this.selectedGender = gender;
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
  },

  mounted() {
    this.populateDateOptions();
  },
};
</script>

<style scoped>
@import "../../assets/css/user/join.css";
</style>