<template>
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
  import axios from 'axios';
  
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
  

      return {
        loginModalVisible,
        joinModalVisible,
        loginEmail,
        loginPassword,
        login,
        signup,
        openModal,
        closeModal,
      };
    },
  };
  </script>
