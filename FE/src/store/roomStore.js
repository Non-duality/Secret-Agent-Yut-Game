import { defineStore } from 'pinia';

export const useRoomStore = defineStore('room', {
  // 반응형 상태 (data)
  state: () => ({
    roomListData: [
      { id: 1, currentPlayers: 3, title: '하이룽', isPublic: false, speed: 1, theme: '설날' },
      { id: 2, currentPlayers: 1, title: '메롱', isPublic: true, speed: 3, theme: '설날' },
      { id: 3, currentPlayers: 6, title: '어도비', isPublic: true, speed: 2, theme: '설날' },
      { id: 4, currentPlayers: 6, title: '야호', isPublic: true, speed: 2, theme: '설날' },
      { id: 5, currentPlayers: 6, title: '쿠키쩔', isPublic: false, speed: 1, theme: '설날' },
      { id: 6, currentPlayers: 5, title: '우짜랄미용', isPublic: true, speed: 2, theme: '설날' },
      { id: 7, currentPlayers: 6, title: '떠돌다가 오는', isPublic: true, speed: 2, theme: '설날' },
      { id: 8, currentPlayers: 4, title: '투썸 ㅋㅋ', isPublic: false, speed: 2, theme: '설날' },
      { id: 9, currentPlayers: 6, title: '스타벅스 흑흑', isPublic: true, speed: 2, theme: '설날' },
      { id: 10, currentPlayers: 2, title: '에이바우트', isPublic: true, speed: 2, theme: '설날' },
      { id: 11, currentPlayers: 6, title: '4반애들', isPublic: true, speed: 2, theme: '설날' },
    ]
  }),

  // 메서드 (function)
  actions: {
    // 방 추가
    addRoom(roomData) {
      this.roomListData.push(roomData)
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