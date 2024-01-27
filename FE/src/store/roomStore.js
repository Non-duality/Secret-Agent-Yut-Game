import { defineStore } from 'pinia';

export const useRoomStore = defineStore('room', {
  // 반응형 상태 (data)
  state: () => ({
    roomListData: [
      { id: 1, currentPlayers: 3, roomName: '하이룽', isPublic: false, speed: 1, theme: '설날' },
      { id: 2, currentPlayers: 1, roomName: '메롱', isPublic: true, speed: 3, theme: '설날' },
      { id: 3, currentPlayers: 6, roomName: '어도비', isPublic: true, speed: 2, theme: '설날' },
      { id: 3, currentPlayers: 6, roomName: '야호', isPublic: true, speed: 2, theme: '설날' },
      { id: 3, currentPlayers: 6, roomName: '쿠키쩔', isPublic: false, speed: 1, theme: '설날' },
      { id: 3, currentPlayers: 5, roomName: '우짜랄미용', isPublic: true, speed: 2, theme: '설날' },
      { id: 3, currentPlayers: 6, roomName: '떠돌다가 오는', isPublic: true, speed: 2, theme: '설날' },
      { id: 3, currentPlayers: 4, roomName: '투썸 ㅋㅋ', isPublic: false, speed: 2, theme: '설날' },
      { id: 3, currentPlayers: 6, roomName: '스타벅스 흑흑', isPublic: true, speed: 2, theme: '설날' },
      { id: 3, currentPlayers: 2, roomName: '에이바우트', isPublic: true, speed: 2, theme: '설날' },
      { id: 3, currentPlayers: 6, roomName: '4반애들', isPublic: true, speed: 2, theme: '설날' },
    ]
  }),

  // 메서드 (function)
  actions: {
    addRoom(room) {
      this.roomListData.push(room)
    },
  },

  // 계산된 값 (computed)
  getters: {
  }
});