import { defineStore } from "pinia";

import { getRoomList, getRoomDetail } from "@/api/room";

export const useRoomStore = defineStore("room", {
  // 반응형 상태 (data)
  state: () => ({
    stompClient: null,
    isConnected: false,

    /* 게임 방 요약 정보 */
    roomList: [],

    /* 게임 방 상세 정보 */
    roomDetailData: null,

    /* 모달 관련 */
    showRoomMakingModal: false, // 방 생성
    showRoomPasswordCheckModal: false, // 비공개방 비밀번호 체크 모달
  }),

  // 메서드 (function)
  actions: {
    connectWS() {},

    // 여는 모달
    openModal(value) {
      if (value === "roomMaking") {
        this.showRoomMakingModal = true;
      } else if (value === "roomPasswordCheck") {
        this.showRoomPasswordCheckModal = true;
      }
    },

    // 닫는 모달
    closeModal(value) {
      if (value === "roomMaking") {
        this.showRoomMakingModal = false;
      } else if (value === "roomPasswordCheck") {
        this.showRoomPasswordCheckModal = false;
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
