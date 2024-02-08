import { defineStore } from "pinia";

export const usePickStore = defineStore("pick", {
  state: () => ({
    code: "",

    userInfo: [],

    unitInfo: [],

    subscription: {},

    receivedMessage: {},

    publisher: {},
  }),

  actions: {},

  persist: {
    enabled: true, //storage 저장유무
    strategies: [
      {
        key: "pick",
        storage: localStorage,
      },
    ],
  },
});
