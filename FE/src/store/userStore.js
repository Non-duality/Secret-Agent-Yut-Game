import { defineStore } from "pinia";
import { jwtDecode } from "jwt-decode";

import { userConfirm, findById } from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

export const useUserStore = defineStore("user", {
  state: () => {
    return {
      isLogin: sessionStorage.getItem("isLogin") === "true" || false,
      showModalSide:
        sessionStorage.getItem("showModalSide") === "true" || false,
      showLoginModal:
        sessionStorage.getItem("showLoginModal") === "true" || false,
      showJoinModal:
        sessionStorage.getItem("showJoinModal") === "true" || false,
      showUserInfoNick:
        sessionStorage.getItem("showUserInfoNick") === "true" || false,
      showDropOutModal:
        sessionStorage.getItem("showDropOutModal") === "true" || false,
      showSuccessPassword:
        sessionStorage.getItem("showSuccessPassword") === "true" || false,
      userInfo: sessionStorage.getItem("userInfo") || null,
    };
  },

  actions: {
    // 로그인, 회원가입 모달 창을 나타내기 위한 함수 매개변수를 입력받아
    // 로그인, 회원가입 차별을 준다.
    openModal(value) {
      if (value === "login") {
        this.showLoginModal = true;
        sessionStorage.setItem("showLoginModal", "true");
      } else if (value === "join") {
        this.showJoinModal = true;
        sessionStorage.setItem("showJoinModal", "true");
      } else if (value === "Nick") {
        this.showUserInfoNick = true;
        sessionStorage.setItem("showUserInfoNick", "true");
      } else if (value === "out") {
        this.showDropOutModal = true;
        sessionStorage.setItem("showDropOutModal", "true");
      } else if (value === "password") {
        this.showSuccessPassword = true;
        sessionStorage.setItem("showSuccessPassword", "true");
      }
    },

    // 위와 같지만, 닫는 함수.
    closeModal(value) {
      if (value === "login") {
        this.showLoginModal = false;
        sessionStorage.setItem("showLoginModal", false);
      } else if (value === "join") {
        this.showJoinModal = false;
        sessionStorage.setItem("showJoinModal", false);
      } else if (value === "Nick") {
        this.showUserInfoNick = false;
        sessionStorage.setItem("showUserInfoNick", false);
      } else if (value === "Drop") {
        this.showDropOutModal = false;
        sessionStorage.setItem("showDropOutModal", false);
      } else if (value === "password") {
        this.showSuccessPassword = false;
        sessionStorage.setItem("showSuccessPassword", false);
      }
    },

    // 상단 바와 사이드 바 토글 함수.
    toggleNav() {
      this.showModalSide = !this.showModalSide;
      sessionStorage.setItem("showModalSide", this.showModalSide);
    },

    userLogin: async (loginUser) => {
      await userConfirm(
        loginUser,
        (response) => {
          if (response.status === httpStatusCode.OK) {
            let { data } = response;
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];

            sessionStorage.setItem("accessToken", accessToken);
            sessionStorage.setItem("refreshToken", refreshToken);

            sessionStorage.setItem("isLogin", "true");
            sessionStorage.setItem("showLoginModal", "false");
          } else {
            console.log("로그인 실패");
            sessionStorage.setItem("isLogin", "false");
          }
        },
        (error) => {
          // 로그인 실패 구현
          alert("로그인 실패");
          sessionStorage.setItem("isLogin", "false");
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
            sessionStorage.setItem(
              "userInfo",
              JSON.stringify(response.data.userInfo)
            );
          } else {
            console.log("유저 정보 없음!!!!");
          }
        },
        async (error) => {
          console.error(
            "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: "
          );
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
