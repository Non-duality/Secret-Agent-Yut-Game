import { defineStore } from "pinia";
import { getRoomList, getRoomDetail, getCanEnterRoom } from "@/api/room";

import { Client } from "@stomp/stompjs";
import { useUserStore } from "./userStore";
const { VITE_WSS_API_URL } = import.meta.env;

export const useRoomStore = defineStore("room", {

  /* 반응형 DATA */
  state: () => ({

    /* 소켓 통신을 위한 DATA */
    stompClient: null,
    isConnected: false,
    password: null,
    jsonPassword: null,

    /* 게임 방 요약 정보 */
    roomList: [],

    /* 게임 방 상세 정보 */
    roomDetailData: null,

    /* 모달 관련 */
    showRoomMakingModal: false, // 방 생성
    showRoomPasswordCheckModal: false, // 비공개방 비밀번호 체크 모달
  }),

  actions: {
    /* 로그인을 하게 되면, 바로 소켓 통신 */
    connectWS() {
      return new Promise((resolve, reject) => {
        let token = useUserStore().accessToken;

        useRoomStore().stompClient = new Client({
          brokerURL: VITE_WSS_API_URL,

          connectHeaders: {
            Authorization: `Bearer ${token}`,
          },

          beforeConnect: () => {},

          onConnect: () => { 
            useRoomStore().isConnected = true;
            resolve();
          },

          onDisconnect: () => {
            useRoomStore().isConnected = false;
            useUserStore().initData();
            reject(new Error("WebSocket disconnected"));
          },

          onWebSocketClose: (closeEvent) => {
            console.log("WebSocket closed", closeEvent);
          },

          onWebSocketError: (error) => {
            useRoomStore().stompClient.deactivate();
            console.log("WebSocket error: ", error);
            reject(error);
          },

          // STOMP 수준의 오류 처리
          onStompError: (frame) => {
            useRoomStore().stompClient.deactivate();
            console.error("STOMP Error:", frame);
            reject(new Error("STOMP error"));
          },
        });

        useRoomStore().stompClient.activate();
      });
    },

    /* 방에 들어갈 수 있는지 확인 */
    canEnterRoom() {
      return new Promise((resolve, reject) => {
        const roomInfo = useRoomStore().roomDetailData;
        if (roomInfo.currentUserCount === 6) {
          alert("방의 빈 자리가 없습니다.");
        } else {
          if (!roomInfo.public) {
            useRoomStore().jsonPassword = {
              password: useRoomStore().password,
            };
          } else {
            useRoomStore().jsonPassword = {
              password: "",
            };
          }
          
          getCanEnterRoom(
            useUserStore().roomId,
            useRoomStore().jsonPassword,
            ({ data }) => {useUserStore
              
              useUserStore().roomCode = data;
              useRoomStore().stompClient.subscribe(
                "/sub/room/" + useUserStore().roomCode
              );

              useRoomStore().stompClient.publish({
                headers: {
                  Authorization: `Bearer ${useUserStore().accessToken}`,
                },
                destination:
                  "/pub/room/" + useUserStore().roomCode + "/enter",
              });

              console.log("/pub/room/" + useUserStore().roomCode + "/enter")
              resolve();
            }
          );
        }
      });
    },

    // 여는 모달
    openModal(value) {
      if (value === "roomMaking") {
        useRoomStore().showRoomMakingModal = true;
      } else if (value === "roomPasswordCheck") {
        useRoomStore().showRoomPasswordCheckModal = true;
      }
    },

    // 닫는 모달
    closeModal(value) {
      if (value === "roomMaking") {
        useRoomStore().showRoomMakingModal = false;
      } else if (value === "roomPasswordCheck") {
        useRoomStore().showRoomPasswordCheckModal = false;
      }
    },

    // 방 리스트 조회
    getRoomSomeListData: async function () {
      return new Promise((resolve, reject) => {
        getRoomList(
          (response) => {
            const { data } = response;
            this.roomList = data;
            resolve();
          },
          (error) => {
            console.log(error);
            console.log("방 데이터 조회 실패");
            reject(error);
          }
        );
      });
    },

    // 방 상세 정보 조회
    getRoomDetailData: async function (roomId) {
      return new Promise((resolve, reject) => {
        getRoomDetail(
          roomId,
          (response) => {
            const { data } = response;
            this.roomDetailData = data;
            resolve();
          },
          (error) => {
            console.log(error);
            console.log("방 상세 정보 조회 실패");
            reject(error);
          }
        );
      });
    },

    // 플레이어 수 증가
    // increasePlayerCount(roomId) {
    //   const room = this.roomListData.find((room) => room.id === roomId);
    //   if (room && room.currentPlayers < 6) {
    //     room.currentPlayers += 1;
    //   }
    // },

    // 플레이어 수 감소
    // decreasePlayerCount(roomId) {
    //   const room = this.roomListData.find((room) => room.id === roomId);
    //   if (room && room.currentPlayers > 0) {
    //     room.currentPlayers -= 1;
    //   }
    // },

    // 방 삭제 (플레이어 수가 0일 때)
    // removeEmptyRoom(roomId) {
    //   const roomIndex = this.roomListData.findIndex(
    //     (room) => room.id === roomId
    //   );
    //   if (
    //     roomIndex !== -1 &&
    //     this.roomListData[roomIndex].currentPlayers === 0
    //   ) {
    //     this.roomListData.splice(roomIndex, 1);
    //   }
    // },
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

  // 계산된 값 (computed)
  getters: {},
});
