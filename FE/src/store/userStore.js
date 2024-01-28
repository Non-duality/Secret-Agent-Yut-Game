import { defineStore } from "pinia";
import { jwtDecode } from "jwt-decode";

import { userConfirm, findById } from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

export const useUserStore = defineStore("user", {
  state: () => {
    return {
      isLogin: false, // 로그인 체크
      showModalSide: false, // 네비 바, 사이드 바
      showLoginModal: false, // 로그인 모달
      showJoinModal: false, // 회원가입 모달
      showUserInfoNick: false, // 회원정보 닉네임
      showDropOutModal: false, // 탈퇴 모달
      showSuccessPassword: false, // 비밀번호 변경 모달
    };
  },

  actions: {
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

    // 우선은 백이랑 연결전에 테스트하기위한 함수.
    // login() {
    //   this.showLoginModal = false;
    //   this.isLogin = true;
    // },

    userLogin: async (loginUser) => {
      await userConfirm(
        loginUser,
        (response) => {
          const store = useUserStore();

          if (response.status === httpStatusCode.OK) {
            let { data } = response;

            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];

            store.isLogin = true;
            store.showLoginModal = false;
            sessionStorage.setItem("accessToken", accessToken);
            sessionStorage.setItem("refreshToken", refreshToken);
          } else {
            console.log("로그인 실패");
            store.isLogin = false;
          }
        },
        (error) => {
          console.error(error);
        }
      );
    },

    getUserInfo: (token) => {
      let decodeToken = jwtDecode(token);
      
      findById(
        decodeToken.userId,
        (response) => {
          if (response.status === httpStatusCode.OK) {
            userInfo.value = response.data.userInfo;
          } else {
            console.log("유저 정보 없음!!!!");
          }
        },

        async (error) => {
          console.error(
            "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
            error.response.status
          );
          isValidToken.value = false;

          await tokenRegenerate();
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
});
