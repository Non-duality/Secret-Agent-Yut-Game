<template>
  <div class="modal">
    <div id="work-npc-main">
      <img
        class="work-npc-avatar"
        src="../../assets/img/home/test06.png"
        alt="이미지"
      />
      <div class="work-npc-box">
        <div class="work-npc-name">도움말</div>
        <div class="work-npc-content">{{ npcMessage }}</div>
        <section class="work-npc-buttons">
          <button class="modal-btn">게임 설명</button>
          <!-- <button onclick="workNPC.onFishiButton()">낚시하기</button> -->
          <button @click="closeModal('help')" class="modal-btn">닫기</button>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      npcMessage: "",
      messageDelay: [],
    };
  },

  methods: {
    closeModal(modalId) {
      this.$emit("close-modal", modalId);
    },

    Setmessage(message) {
      this.clearMessageDelay();

      for (let i = 0; i < message.length; i++) {
        let id = setTimeout(() => {
          this.npcMessage += message[i];
        }, 90 * i);

        this.messageDelay.push(id);
      }
    },

    clearMessageDelay() {
      if (this.messageDelay.length > 0) {
        this.messageDelay.forEach((element) => {
          clearTimeout(element);
        });
        this.messageDelay = [];
        this.npcMessage = "";
      }
    },
  },

  mounted() {
    this.Setmessage("게임에 대해서 설명해줄까?");
  },
};
</script>

<style scoped>
@import url("../../assets/css/home/help.css");
</style>