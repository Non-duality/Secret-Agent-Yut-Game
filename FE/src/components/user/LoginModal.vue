<template>
<<<<<<< HEAD
    <div>
      <div class="button-container">
        <button class="login-btn" @click="openModal('login')">
          <span class="login-text">로그인</span>
        </button>
      </div>
  
      <!-- 로그인 모달 -->
      <div v-if="loginModalVisible" class="modal" @click="closeModal('login')">
        <div class="modal-content" @click.stop>
          <span class="close" @click="closeModal('login')">&times;</span>
          <h2>Login</h2>
          <form @submit.prevent="login">
  
            <input 
            type="text" 
            placeholder="이메일" 
            v-model="loginEmail" required>
  
            <input 
            type="password" 
            placeholder="비밀번호" 
            v-model="loginPassword" required>
  
            <button type="submit">로그인</button>
  
          </form>
        </div>
      </div>
  
    </div>
  </template>
  
  <script>
  import { ref } from 'vue';
  // import axios from 'axios';
  
  export default {
    setup() {
      // 데이터 초기화 
      const accessToken = ref(null);
  
      // 로그인 로직
      const login = async (userId, password) => {
        try {
          // 1. 요청할 서버 URL
          const loginUrl = "http://localhost:8080/auth/login";
  
          // 2. 요청 데이터 생성 (JSON 형식)
          const requestData = {
            userId: userId,
            password: password,
          };
  
          // 3. Axios를 사용한 HTTP POST 요청
          const response = await axios.post(loginUrl, requestData);
  
          // HTTP 상태 코드 확인
          if (response.status !== 200) {
            throw new Error("HTTP Error " + response.status);
          }
  
          // 서버로부터 받은 데이터 처리
          const data = response.data;
          accessToken.value = data; // 서버 응답에서 accessToken 추출
        } catch (error) {
          // 에러 처리
          console.error("오류:", error);
        }
      };
  
      const loginModalVisible = ref(false);
      const joinModalVisible = ref(false);
  
      const loginEmail = ref('');
      const loginPassword = ref('');
  
  
      // 모달 열기
      const openModal = (modalType) => {
        if (modalType === 'login') {
          loginModalVisible.value = true;
        } else {
          joinModalVisible.value = true;
        }
        modal.style.display = 'block';
        setTimeout(() => {
          modal.classList.add('show'); // 열 때 애니메이션 클래스 추가
        }, 100); // 애니메이션 지속 시간 (0.1초) 후에 display 속성 변경
      };
  
      // 모달 닫기
      const closeModal = (modalType) => {
        if (modalType === 'login') {
          loginModalVisible.value = false;
        } else {
          joinModalVisible.value = false;
        }
        modal.classList.remove("show"); // 애니메이션 클래스 제거
        // 모달이 완전히 사라질 때까지 기다린 후 display 속성 설정
        setTimeout(function () {
          modal.style.display = "none";
        }, 500); // 애니메이션 지속 시간 (0.5초) 후에 display 속성 변경
      };
  
=======
  <!-- <link rel="stylesheet" href="../../assets/css/user/modal.css" /> -->
>>>>>>> design/FE-InitScreen

  <!-- 로그인 모달 창 -->
  <div id="loginModal" class="modal">
    <div class="modal-content">
      <button class="close" @click="closeModal('login')">&times;</button>
      <h2 class="login-text">Login</h2>
      <input type="text" placeholder="이메일" v-model="email" />
      <input type="password" placeholder="패스워드" v-model="password" />
      <br />
      <label for="rememberMeCheckbox" @click="toggleCheckbox()">
        <input class="login-text" type="checkbox" id="rememberMeCheckbox" />
        <span style="color: white">로그인 유지</span>
      </label>
      <br />
      <button @click="performLogin()" class="modal-login-btn">로그인</button>
      <button @click="closeModal('login')" class="modal-cancel-btn">
        취소
      </button>
    </div>
  </div>
</template>
  
<script>
import { userConfirm } from "../../api/user"

export default {
  data() {
    return {
      email: null,
      password: null,
    };
  },
  methods: {
    closeModal(modalId) {
      this.$emit("close-modal", modalId);
    },
    
    async performLogin() {
      try {
        const response = await userConfirm(
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
  },
};
</script>

<style scoped>
.login-text {
  margin-bottom: 20px;
  top: -10px; /* 위로 20픽셀 이동, 필요에 따라 조절 가능 */
  color: white;
  font-family: "Palatino Linotype", "Book Antiqua", palatino, serif; /* Press Start 2P 글꼴 적용 */
}

.button-container {
  margin: 20px;
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
  /* 애니메이션 설정 */
  transform: translateY(-50px);
  transition: transform 0.3s ease-in-out, opacity 0.3s ease-in-out;
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

.modal-login-btn {
  margin-right: 10px;
}
.modal-cancel-btn {
  margin-left: 10px;
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
</style>