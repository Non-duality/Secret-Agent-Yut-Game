<template>
  <div>
    <div class="side-bar" v-if="showFlag">
      <MessageComponents v-if="showMessageFlag" @closeSidebar="closeSidebar" />
      <FriendComponents v-if="showFriendFlag" @closeSidebar="closeSidebar" />
    </div>
    <div class="side-btn">
      <button @click="showMessage">메시지</button>
      <button @click="showFriend">친구</button>
    </div>
  </div>
</template>
  
  <script>
import MessageComponents from "./MessageComponents.vue";
import FriendComponents from "./FriendComponents.vue";
import { ref } from "vue";

export default {
  components: {
    MessageComponents,
    FriendComponents,
  },
  setup() {
    const showFlag = ref(false);
    const showMessageFlag = ref(false);
    const showFriendFlag = ref(false);

    const showMessage = () => {
      console.log("me");
      if (!showFlag.value) showFlag.value = true;
      showFriendFlag.value = false;
      showMessageFlag.value = true;
    };

    const showFriend = () => {
      console.log("fr");
      if (!showFlag.value) showFlag.value = true;
      showMessageFlag.value = false;
      showFriendFlag.value = true;
    };
    const closeSidebar = () => {
      showMessageFlag.value = false;
      showFlag.value = false;
    };
    return {
      showFlag,
      showMessageFlag,
      showFriendFlag,
      showMessage,
      showFriend,
      closeSidebar,
    };
  },
};
</script>
  
  <style>
@import "@/assets/css/layout/sideBar.css";
</style>
  