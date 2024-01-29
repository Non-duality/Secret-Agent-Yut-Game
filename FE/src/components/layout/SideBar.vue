<template>
  <div>
    <div class="side-bar" v-if="showFlag">
      <div class="side-title">
        <span class="side-text" v-if="showMessageFlag">알림</span>
        <span class="side-text" v-if="showFriendFlag">친구목록</span>
        <button class="btn" @click="closeSidebar">
          <img class="side-close" src="@/assets/icon/close.png" />
        </button>
      </div>
      <div class="side-line"></div>
      <MessageComponents v-if="showMessageFlag" @closeSidebar="closeSidebar" />
      <FriendComponents v-if="showFriendFlag" @closeSidebar="closeSidebar" />
    </div>
    <div class="side-btn">
      <button class="btn" @click="showMessage"> 
        <img class="btn-img" src="@/assets/icon/message.png" />
      </button>
      <button class="btn" @click="showFriend">
        <img class="btn-img" src="@/assets/icon/friend.png" />
      </button>
      <button class="btn" @click="showFriend">
        <img class="btn-img" src="@/assets/icon/setting.png" />
      </button>
      <button class="btn" @click="showFriend">
        <img class="btn-img" src="@/assets/icon/logout.png" />
      </button>
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
      showFriendFlag.value = false;
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
  