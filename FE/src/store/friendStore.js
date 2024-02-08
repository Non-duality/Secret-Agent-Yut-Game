import { defineStore } from "pinia";

import { getMyFriend } from "@/api/friend";
export const useFriendStore = defineStore("friend", {
  state() {
    return {
        friends: [],
        findUser:{},
    };
  },
  actions: {
    getFriend: async () => {
      await getMyFriend(
        (response) => {
          console.log(response);
          useFriendStore().friends = response.data;
        },
        (error) => {
          console.log(error);
        }
      );
    },
    searchUser(searchName){
      console.log("아직 서버 없음. " + searchName);
      this.findUser.email = "test@test.com";
      this.findUser.nickName = searchName;
    },
  },
});
