import { defineStore } from "pinia";

import { getMyFriend } from "@/api/friend";

export const useFriendStore = defineStore("friend", {
  state() {
    return {
      friends: [], // 친구 목록
      onlineFriends: [], // 온라인 상태인 친구 목록
      friendRequests: [], // 친구 요청 목록
      chatMessages: {}, // 친구 별 채팅 메시지 {friendID: [message]}
      gameInvitations: [], // 게임 초대 목록
    };
  },
  actions: {
    // 기존 친구 가져오기
    async getFriend() {
      try {
        const response = await getMyFriend();
        this.friends = response.data;
      } catch (error) {
        console.log(error);
      }
    },
    searchUser(searchName){
      console.log("아직 서버 없음. " + searchName);
      this.findUser.email = "test@test.com";
      this.findUser.nickName = searchName;
    },
    // 친구 요청
    receiveFriendRequest(request) {
      this.friendRequests.push(request)
    },
    // 친구 채팅
    receiveChatMessage({ friendID, message }) {
      if (!this.chatMessages[friendID]) {
        this.chatMessages[friendID] = []
      }
      this.chatMessages[friendID].push(message)
    },
    // 게임 초대
    receiveGameInvitation(invitation) {
      this.gameInvitations.push(invitation)
    }
  },
});