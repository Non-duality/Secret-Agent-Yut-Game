import { defineStore } from "pinia";
import {
  getRoomList,
  getRoomDetail,
  getCanEnterRoom,
  axiosCreateRoom,
} from "@/api/room";

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

    /* 게임 방 생성 정보 */
    createRoomInfo: null,

    /* 입장 시, 받을 정보들 */
    seatInfo: {
      // 방 정보
      seatnum1: {
        nickname: "",
        ready: false,
      },
      seatnum2: {
        nickname: "",
        ready: false,
      },
      seatnum3: {
        nickname: "",
        ready: false,
      },
      seatnum4: {
        nickname: "",
        ready: false,
      },
      seatnum5: {
        nickname: "",
        ready: false,
      },
      seatnum6: {
        nickname: "",
        ready: false,
      },
    },

    /* 메시지 관련 상태값 */
    receivedMessage: null,
    roomChatMessages: [],

    /* 구독 관리 */
    subscription: {
      room: null,
    },

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
          },

          onWebSocketClose: (closeEvent) => {
            console.log("WebSocket closed", closeEvent);
          },

          onWebSocketError: (error) => {
            useRoomStore().isConnected = false;
            useUserStore().initData();
            useRoomStore().stompClient.deactivate();
            console.log("WebSocket error: ", error);
            reject(error);
          },

          // STOMP 수준의 오류 처리
          onStompError: (frame) => {
            useRoomStore().isConnected = false;
            useRoomStore().stompClient.deactivate();
            console.error("STOMP Error:", frame);
            reject(new Error("STOMP error"));
          },
        });

        try {
          useRoomStore().stompClient.activate();
        } catch (error) {
          console.log("소켓 에러: " + error);
        }
      });
    },

    /* 방에 들어갈 수 있는지 확인하고 가능하다면 구독과 발행 실행 */
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

          // 방에 들어갈 수 있는지 확인
          // 입력값 (roomId, jsonPassword)
          getCanEnterRoom(
            useUserStore().roomId,
            useRoomStore().jsonPassword,
            ({ data }) => {
              useUserStore().roomCode = data;
              (useRoomStore().subscription.room =
                useRoomStore().stompClient.subscribe(
                  "/sub/room/" + useUserStore().roomCode,

                  // 구독 메시지 이벤트 처리
                  (message) => {
                    console.log(message.body);
                    useRoomStore().receivedMessage = JSON.parse(message.body);

                    if (receivedMessage.value.type === "ROOM_ENTER_INFO") {
                      useRoomStore().roomChatMessages.push(
                        useRoomStore().receivedMessage.data.message
                      );
                      useRoomStore().receivedMessage.data.currentSeatDtoList.forEach(
                        (seat, index) => {
                          const seatKey = `seatnum${index + 1}`;
                          if (seatInfo[seatKey]) {
                            useRoomStore().seatInfo[seatKey].nickname =
                              seat.nickname;
                            useRoomStore().seatInfo[seatKey].ready = seat.ready;
                          }
                        }
                      );
                    } else if (
                      useRoomStore().receivedMessage.type === "ROOM_EXIT_INFO"
                    ) {
                      useRoomStore().roomChatMessages.push(
                        useRoomStore().receivedMessage.data.message
                      );
                      useRoomStore().receivedMessage.data.currentSeatDtoList.forEach(
                        (seat, index) => {
                          const seatKey = `seatnum${index + 1}`;
                          if (seatInfo.value[seatKey]) {
                            useRoomStore().seatInfo[seatKey].nickname =
                              seat.nickname;
                            useRoomStore().seatInfo[seatKey].ready = seat.ready;
                          }
                        }
                      );
                    } else if (
                      useRoomStore().receivedMessage.type === "ROOM_CHAT"
                    ) {
                      useRoomStore().roomChatMessages.push(
                        useRoomStore().receivedMessage.data.nickname +
                          " : " +
                          useRoomStore().receivedMessage.data.message
                      );
                    }
                  }
                )),
                useRoomStore().stompClient.publish({
                  headers: {
                    Authorization: `Bearer ${useUserStore().accessToken}`,
                  },
                  destination:
                    "/pub/room/" + useUserStore().roomCode + "/enter",
                });

              // 현재, 자신의 방의 정보를 넣는다.
              // (나갈 때 정보 삭제 필요)
              useUserStore().currentRoomInfo = useRoomStore().roomDetailData;
              useUserStore().currentRoomInfo.roomCode = data;
              resolve();
            }
          );
        }
      });
    },

    /* 메시지 보내기 */
    sendMessage() {
      sendRoomMessage.value.message = tempMessage.value;
      stompClient.value.publish({
        destination: "/pub/room/" + currentRoomCode.value + "/chat",
        body: JSON.stringify(sendRoomMessage.value),
      });
    },

    /* 나가기 */
    exitRoom() {
      useRoomStore().stompClient.publish({
        destination:
          "/pub/room/" + useUserStore().currentRoomInfo.roomCode + "/exit",
        headers: {
          Authorization: `Bearer ${useUserStore().accessToken}`,
        },
      });

      useRoomStore().subscription.room.unsubscribe();
    },

    /* 방 생성 */
    async createRoom() {
      try {
        await new Promise((resolve, reject) => {
          axiosCreateRoom(useRoomStore().createRoomInfo, ({ data }) => {
            // 방 생성 코드 받기.
            useRoomStore().createRoomInfo.roomCode = data;
            resolve();
          });
        });
      } catch (error) {
        // 토큰이 만료된 경우
        if (error.response.status === 406) {
          await useUserStore().tokenRegenerate();
        }
      }
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
            useRoomStore().roomList = data;
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
            useRoomStore().roomDetailData = data;
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
  },

  persist: {
    enabled: true, //storage 저장유무
    strategies: [
      {
        key: "roomState",
        storage: localStorage,
        reducer: (state) => ({
          isConnected: state.isConnected,
          // 기타 필요한 상태들...
        }),
      },
    ],
  },
});
