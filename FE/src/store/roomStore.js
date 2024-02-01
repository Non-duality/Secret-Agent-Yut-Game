import { defineStore } from 'pinia';

import { getRoomSomeList } from "@/api/room";

export const useRoomStore = defineStore('room', {
  // 반응형 상태 (data)
  state: () => ({
    // 게임 방 전체 정보
    roomListData: [
      { id: 1, currentPlayers: 3, title: '방가방가 햄토리', isPublic: false, password: 'qwer1234', speed: 1, theme: 'lunaNewYear' },
    ],

    // 게임 방 생략 정보
    roomListSomeData:[

    ],

    // 모달 관련
    showRoomMakingModal: false, // 방 생성
    showRoomPasswordCheckModal: false // 비공개방 비밀번호 체크 모달
  }),

  // 메서드 (function)
  actions: {
    // 여는 모달
    openModal(value) {
      if (value === 'roomMaking') {
        this.showRoomMakingModal = true;
      } else if (value === 'roomPasswordCheck') {
        this.showRoomPasswordCheckModal = true;
      }
    },

    // 닫는 모달
    closeModal(value) {
      if (value === 'roomMaking') {
        this.showRoomMakingModal = false;
      } else if (value === 'roomPasswordCheck') {
        this.showRoomPasswordCheckModal = false;
      }
    },

    // 방 일부 정보 DB에서 받아오기
    getRoomSomeListData: async function() {
      await getRoomSomeList(
        (response) => {
          const { data } = response;
          this.roomListSomeData = data;
          console.log('방 데이터 조회')
        },
        (error) => {
          console.log(error)
          console.log('방 데이터 조회 실패')
        }
      )
    },

    // 방 추가
    addRoom(roomData) {
      this.roomListData.push(roomData)
    },

    // 플레이어 수 증가
    increasePlayerCount(roomId) {
      const room = this.roomListData.find(room => room.id === roomId)
      if (room && room.currentPlayers < 6) {
        room.currentPlayers += 1;
      }
    },

    // 플레이어 수 감소
    decreasePlayerCount(roomId) {
      const room = this.roomListData.find(room => room.id === roomId);
      if (room && room.currentPlayers > 0) {
        room.currentPlayers -= 1;
      }
    },

    // 방 삭제 (플레이어 수가 0일 때)
    removeEmptyRoom(roomId) {
      const roomIndex = this.roomListData.findIndex(room => room.id === roomId);
      if (roomIndex !== -1 && this.roomListData[roomIndex].currentPlayers === 0) {
        this.roomListData.splice(roomIndex, 1);
      }
    }
  },

  // 계산된 값 (computed)
  getters: {

  }
});