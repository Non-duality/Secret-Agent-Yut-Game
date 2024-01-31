import { defineStore } from "pinia";
import { cloneDeep } from "lodash";
import { jwtDecode } from "jwt-decode";

import { userConfirm, userJoin, findById } from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

export const useUserStore = defineStore("user", {
  id: "myStore",

  state: () => {
    return {
      isLogin: false, // 로그인 체크
      showModalSide: false, // 네비 바, 사이드 바
      showLoginModal: false, // 로그인 모달
      showJoinModal: false, // 회원가입 모달
      showUserInfoNick: false, // 회원정보 닉네임
      showDropOutModal: false, // 탈퇴 모달
      showSuccessPassword: false, // 비밀번호 변경 모달
      userInfo: null, // 회원정보
      accessToken: null, // 어세스 토큰
      refreshToken: null, // 리프레쉬 토큰
    };
  },

  actions: {
    // 데이터 초기화 로직
    initData() {
      console.log("모든 데이터 초기화")
      const initialState = this.$reset();
      Object.assign(this, initialState);
    },

    // 로그인, 회원가입 모달 창을 나타내기 위한 함수 매개변수를 입력받아
    // 로그인, 회원가입 차별을 준다.
    openModal(value) {
      if (value === "login") {
        this.showLoginModal = true;
      } else if (value === "join") {
        this.showJoinModal = true;
      } else if (value === "Nick") {
        this.showUserInfoNick = true;
      } else if (value === "out") {
        this.showDropOutModal = true;
      } else if (value === "password") {
        this.showSuccessPassword = true;
      }
    },

    // 위와 같지만, 닫는 함수.
    closeModal(value) {
      if (value === "login") {
        this.showLoginModal = false;
      } else if (value === "join") {
        this.showJoinModal = false;
      } else if (value === "Nick") {
        this.showUserInfoNick = false;
      } else if (value === "Drop") {
        this.showDropOutModal = false;
      } else if (value === "password") {
        this.showSuccessPassword = false;
      }
    },

    // 상단 바와 사이드 바 토글 함수.
    toggleNav() {
      this.showModalSide = !this.showModalSide;
    },

    userJoin: async (joinUser) => {
      await userJoin(
        joinUser,
        (response) => {
          // 확인용
          alert('회원가입 성공')
          console.log(response)
          console.log("회원가입 성공")
        },

        (error) => {
          // 확인용
          alert('회원가입 실패')
          console.log(error)
          console.log("회원가입 실패")
        }
      )
    },

    userLogin: async (loginUser) => {
      await userConfirm(
        loginUser,
        (response) => {
          const store = useUserStore();
          console.log("로그인 성공")
          if (response.status === httpStatusCode.OK) {
            let { data } = response;

            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];

            useUserStore().accessToken = accessToken;
            useUserStore().refreshToken = refreshToken;

            useUserStore().isLogin = true;
            useUserStore().showLoginModal = false;
          } else {
            console.log("로그인 실패");
            useUserStore().isLogin = false;
          }
        },

        (error) => {
          // 로그인 실패 구현
          alert("로그인 실패");
          useUserStore().isLogin = false;
        }
      );
    },

    getUserInfo: (token) => {
      let decodeToken = jwtDecode(token);

      findById(
        decodeToken.userId,
        (response) => {
          if (response.status === httpStatusCode.OK) {
            useUserStore().userInfo = response.data.userInfo;
          } else {
            console.log("유저 정보 없음!!!!");
          }
        },

        async (error) => {
          //error.response.status
          console.error(
            "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: "
          );
          // this.isValidToken = false;
          // await tokenRegenerate();
        }
      );
    },

    tokenRegenerate: async () => {
      console.log(
        "토큰 재발급 >> 기존 토큰 정보 : {}",
        sessionStorage.getItem("accessToken")
      );

      await tokenRegeneration(
        JSON.stringify(userInfo.value),
        (response) => {
          if (response.status === httpStatusCode.CREATE) {
            let accessToken = response.data["access-token"];
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            sessionStorage.setItem("accessToken", accessToken);
            isValidToken.value = true;
          }
        },
        async (error) => {
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
          if (error.response.status === httpStatusCode.UNAUTHORIZED) {
            console.log("갱신 실패");
            // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
            await logout(
              userInfo.value.userid,
              (response) => {
                if (response.status === httpStatusCode.OK) {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
                isLogin.value = false;
                userInfo.value = null;
                isValidToken.value = false;
                router.push({ name: "user-login" });
              },
              (error) => {
                console.error(error);
                isLogin.value = false;
                userInfo.value = null;
              }
            );
          }
        }
      );
    },
  },

  persist: {
    enabled: true, //storage 저장유무
    strategies: [
      {
        key: "counter", //storage key값 설정
        storage: localStorage, // localStorage, sessionStorage storage 선택 default sessionStorage
      },
    ],
  },
});
