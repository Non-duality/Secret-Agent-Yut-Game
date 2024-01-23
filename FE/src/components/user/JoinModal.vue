<template>
  <!-- 회원가입 모달 창 -->
  <div id="joinModal" class="modal">
    <div class="modal-content">
      <button class="close" @click="closeModal('join')">&times;</button>
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
        @input="checkPassword"
        required
      /><br />
      <div class="password-error" v-if="passwordMismatch">
        패스워드가 일치하지 않습니다.
      </div>

      <input
        type="text"
        placeholder="닉네임"
        id="nicknameInput"
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
        <select id="birthdateYear" required></select>

        <select id="birthdateMonth" required></select>

        <select id="birthdateDay" required></select>
      </div>

      <button class="modal-join-btn" @click="performJoin()">회원가입</button>
      <button class="modal-cancel-btn" @click="closeModal('join')">취소</button>
    </div>
  </div>
</template>

<script>
import { userJoin } from "../../api/user"
export default {
  data() {
    return {
      passwordMismatch: false,
      selectedGender: null,
      userId: null,
      password: null,
      name: null,
      gender: null,
      birth: null
    };
  },
  methods: {
    async performJoin() {
      try {
        const response = await userJoin(
          {
            userId: this.email, // 이메일 변수를 사용
            password: this.password, // 패스워드 변수를 사용
          },
          (response) => {
            console.log(response);
            // 로그인 성공 시 처리
            // response.data를 통해 서버의 응답 데이터에 접근할 수 있을 것입니다.
            // 예를 들어, 토큰을 저장하거나 로그인 완료 후의 동작을 정의할 수 있습니다.
          },
          (error) => {
            // 로그인 실패 시 처리
            // error를 통해 실패 이유 등을 확인할 수 있을 것입니다.
            console.error("로그인 실패:", error);
            // 에러 메시지를 표시하거나 다른 처리를 수행하세요.
          }
        );

        // 서버로부터 받은 토큰을 처리하거나 원하는 동작을 수행할 수 있습니다.
      } catch (error) {
        // 예외 처리
        console.error("로그인 오류:", error);
        // 예외 처리를 수행하세요.
      }
    },

    closeModal(modalId) {
      this.$emit("close-modal", modalId);
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
      // 성별 선택 메소드
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
.password-error {
  color: red;
  text-align: top;
  margin-bottom: 10px;
}
.join-text {
  margin-bottom: 20px;
  top: -10px; /* 위로 20픽셀 이동, 필요에 따라 조절 가능 */
  color: white;
  font-family: "Palatino Linotype", "Book Antiqua", palatino, serif; /* Press Start 2P 글꼴 적용 */
}

/* 모달 애니메이션 */
.modal {
  /* 기본 스타일 유지 */
  border-color: black;
  display: flex;
  align-items: center; /* 수직 중앙 정렬 */
  justify-content: center; /* 수평 중앙 정렬 */
  position: fixed;
  left: 0;
  top: 0px;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 3;
}

.modal-content {
  /* 기본 스타일 유지 */
  background-color: #00000035;
  margin: 5% auto;
  padding: 20px;
  border: 2px solid #888;
  border-color: white;
  border-radius: 30px;
  width: 300px;
  text-align: center;
}

.close {
  background-color: #ffffff00;
  position: absolute;
  top: 0;
  right: 0;
  padding: 10px;
  margin-right: 10px;
  color: white;
  z-index: 999; /* 높은 값으로 설정 */
}

/* 입력란 스타일 */
input[type="text"],
input[type="password"] {
  width: 75%;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 30px;
}

.modal-join-btn {
  margin-top: 20px;
  margin-right: 10px;
  text-align: center;
}
.modal-cancel-btn {
  text-align: center;
}

/* 로그인 버튼 호버 효과 */
.modal-login-btn:hover,
.modal-join-btn:hover {
  background-color: #f44336; /* 호버 시 배경색 변경 */
  color: white; /* 호버 시 텍스트 색상 변경 */
}

/* 취소 버튼 호버 효과 */
.modal-cancel-btn:hover {
  background-color: #f44336; /* 호버 시 배경색 변경 */
  color: white; /* 호버 시 텍스트 색상 변경 */

  /* 트랜지션 효과 */
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* 생년월일 선택 옵션 스타일링 */
select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  margin: 5px;
}

.gender-selection {
  text-align: center;
  margin: 10px;
}

.gender-button {
  margin: 5px;
  font-size: 16px;
  cursor: pointer;
  border: none;
  outline: none;
  background-color: #ccc; /* 회색 배경색 */
  color: #000; /* 글자색 */
  border-radius: 50px;
}

.gender-button:hover {
  background-color: #ff0000;
}

.gender-button.selected {
  background-color: #ff0000; /* 빨간색 배경색 */
  color: #fff; /* 글자색 */
}
</style>