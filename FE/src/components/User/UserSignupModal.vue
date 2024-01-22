<template>
  <div id="joinModal" class="modal">
    <div class="modal-content">
      <span class="close" onclick="closeModal('join')">&times;</span>
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

      <label for="genderRadio">성별 선택:</label>
      <input type="radio" name="gender" id="maleRadio" value="남자" /> 남자
      <input type="radio" name="gender" id="femaleRadio" value="여자" />
      여자<br />

      <button>회원가입</button>
      <button onclick="closeModal('join')">취소</button>
    </div>
  </div>
</template>

<script setup>
const loginUrl = "http://localhost:8080/user";


</script>

<style scoped>
.button-container {
  position: absolute;
  right: 10px;
  bottom: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.login-btn,
.join-btn {
  display: block;
  margin: 5px 0;
  padding: 10px 20px;
  background: transparent;
  color: #fff;
  border: none;
  text-align: center; /* 텍스트 가운데 정렬 */
  font-size: 3em;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  margin-right: 80%; /* 왼쪽 마진 추가 */
  font-family: "Palatino Linotype", "Book Antiqua", palatino, serif; /* Press Start 2P 글꼴 적용 */
}

.join-btn {
  padding: 10px 40px;
  margin-bottom: 80%;
}

/* 버튼 호버 효과 */
.login-btn:hover,
.join-btn:hover {
  color: #46c000; /* 글자 색상 변경 */
}

.login-text, .join-text{
  position: relative; /* 상대 위치로 설정 */
  top: -20px; /* 위로 20픽셀 이동, 필요에 따라 조절 가능 */
  color: white;
  font-family: "Palatino Linotype", "Book Antiqua", palatino, serif; /* Press Start 2P 글꼴 적용 */
}

/* 모달 애니메이션 */
.modal {
  /* 기본 스타일 유지 */
  border-color: black;
  display: none;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  /* 애니메이션 설정 */
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
}

.modal-content {
  /* 기본 스타일 유지 */
  background-color: #00000035;
  margin: 15% auto;
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

.modal.show {
  /* 모달이 나타날 때의 스타일 */
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

.modal.show .modal-content {
  /* 모달 컨텐츠가 나타날 때의 스타일 */
  transform: translateY(0);
}
.close {
  position: absolute;
  top: 0;
  right: 0;
  padding: 10px;
  margin-right: 10px;
  cursor: pointer;
  color: white;
}

/* 입력란 스타일 */
input[type="text"],
input[type="password"] {
  width: 75%;
  padding: 10px;
  margin-bottom: 40px;
  border-radius: 30px;
}

button {
  background-color: #0000008a;
  color: #fff;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
}

/* 생년월일 선택 옵션 스타일링 */
select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  margin: 5px;
}
</style>