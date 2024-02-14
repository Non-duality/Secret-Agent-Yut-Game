import { defineStore } from "pinia";

import { getMyFriend, getMyFriendRequest } from "@/api/friend";

export const useFriendStore = defineStore("friend", {
  state() {
    return {
      friends: [], // 친구 목록
      friendRequests: [], // 친구 요청 목록
      chatMessages: {}, // 친구 별 채팅 메시지 {friendID: [message]}
      gameInvitations: [], // 게임 초대 목록
    };
  },
  actions: {
    // 기존 친구 가져오기
    async getFriend() {
      try {
        await getMyFriend((response) => {
          // 친구 있을 때만 가져오기
          if (response.data.length > 0) {
            this.friends = response.data;
          }
        });
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
    async receiveFriendRequest(request) {
      // 요청 신호 오면 친구 요청 업데이트
      try {
        await getMyFriendRequest((response) => {
          this.friendRequests = response.data
          console.log('친구 요청 왔습니다.', response)
        })
      } catch (error) {
        console.log('친구 요청 오다가 말았습니다.', error)
      }
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
    },
    // 친구 온라인 상태 업데이트
    updateOnlineFriends(friendID) {
      this.friends.forEach(friend => {
        if (friend.email === friendID) {
          friend.online = true
        }
      })
    },
    // 친구 오프라인 상태 업데이트
    updateOfflineFriends(friendID) {
      this.friends.forEach(friend => {
        if (friend.email === friendID) {
          friend.online = false
        }
      })
    },
  },
});