import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => {
    return {
      isLogin: false,         // 로그인 체크
      showModalSide: false,   // 네비 바, 사이드 바
      showLoginModal: false,  // 로그인 모달
      showJoinModal: false,   // 회원가입 모달
      showUserInfoNick: false,    // 회원정보 닉네임
    };
  },
  actions: {
    // 로그인, 회원가입 모달 창을 나타내기 위한 함수 매개변수를 입력받아
    // 로그인, 회원가입 차별을 준다.
    openModal(value) {
      if (value === 'login') {
        this.showLoginModal = true;
      } else if (value === 'join') {
        this.showJoinModal = true;
      } else if(value === 'Nick'){
        this.showUserInfoNick = true;
      }
    },
    // 위와 같지만, 닫는 함수.
    closeModal(value) {
      if (value === 'login') {
        this.showLoginModal = false;
      } else if (value === 'join') {
        this.showJoinModal = false;
      } else if(value === 'Nick'){
        this.showUserInfoNick = false;
      }
    },
    // 상단 바와 사이드 바 토글 함수.
    toggleNav() {
      this.showModalSide = !this.showModalSide;
    },
    // 우선은 백이랑 연결전에 테스트하기위한 함수.
    loginTest() {
      this.showLoginModal = false;
      this.isLogin = true;
    },
  },
});
