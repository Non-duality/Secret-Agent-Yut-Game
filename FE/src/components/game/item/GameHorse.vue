<template>
  <div :style="horseStyle">
    <button
      @mouseover="MouseOver"
      @mouseleave="MouseLeave"
      :style="horseBtnStyle"
      @click="selectHorse"
    >
      <img :src="imgSrc" :style="horseImgStyle" />
    </button>
    <div v-if="isMouseOver" :style="horseModalStyle">{{ horse }}</div>
  </div>
</template>

<script>
import { useGameStore } from "@/store/gameStore";

export default {
  props: {
    horse: Object,
  },
  data() {
    return {
      imgSrc: "",
      isMouseOver: false, // 마우스 오버
    };
  },
  computed: {
    // 말 스타일
    horseStyle() {
      // 피니아 가져오기
      const gameStore = useGameStore();
      const horsesIndex = gameStore.horsesIndex;

      // 기본 스타일 설정.
      let styles = {
        position: "fixed",
        zIndex: "1000",
        transition: "all 0.5s",
      };

      // 말이 대기 상태이면.
      if (this.horse.status === "wait") {
        // 홍팀
        if (this.horse.team == 1) {
          // 앞에 대기 중인 말이 몇개 인지 체크
          const temp = this.horse.id - this.horse.check;
          let sub = 0;
          if (temp > 1) {
            sub = temp - 1;
          }

          // 위치를 미리 저장한 배열에서 가져온다.
          styles.bottom = horsesIndex[0][this.horse.id - 1 - sub].bottom;
          styles.left = horsesIndex[0][this.horse.id - 1 - sub].left;
        }
        // 청팀
        else {
          // 앞에 대기 중인 말이 몇개 인지 체크
          const temp = this.horse.id - this.horse.check;
          let sub = 0;
          if (temp > 1) {
            sub = temp - 1;
          }

          // 위치를 미리 저장한 배열에서 가져온다.
          styles.bottom = horsesIndex[1][this.horse.id - 1 - sub].bottom;
          styles.right = horsesIndex[1][this.horse.id - 1 - sub].right;
        }
      }
      // 말이 이동 중이면.
      else if (this.horse.status === "ing") {
        const tileHorse = gameStore.tileHorse;

        styles.bottom = tileHorse[this.horse.index].bottom;
        styles.right = tileHorse[this.horse.index].right;
      }
      // 말이 들어 왔다면.
      else {
      }

      return styles;
    },

    // 말 버튼 스타일
    horseBtnStyle() {
      let styles = {
        background: "none",
        border: "none",
        padding: "0",
        margin: "0",
        cursor: "pointer",
      };

      return styles;
    },

    // 말 정보 모달 스타일
    horseModalStyle() {
      let styles = {
        position: "fixed",
        zIndex: "1000",
        top: "500px",
        width: "200px",
        height: "200px",
      };
      if (this.horse.team === 1) {
        styles.left = "250px";
        styles.backgroundColor = "rgba(255, 2, 2, 0.05)";
      } else {
        styles.right = "250px";
        styles.backgroundColor = "rgba(36, 2, 255, 0.05)";
      }
      return styles;
    },

    // 말 이미지 스타일
    horseImgStyle() {
      let styles = {
        width: "50px",
      };
      if (this.isMouseOver) {
        styles.transform = "scale(1.2)"; // 확대
        styles.transition = "transform 0.3s ease";
      }
      return styles;
    },
  },

  // 이미지를 동적으로 가져오기.
  async mounted() {
    try {
      switch (this.horse.img) {
        case "horse1":
          this.imgSrc = (await import("@/assets/img/horse/horse1.png")).default;
          break;
        case "horse2":
          this.imgSrc = (await import("@/assets/img/horse/horse2.png")).default;
          break;
        case "horse3":
          this.imgSrc = (await import("@/assets/img/horse/horse3.png")).default;
          break;
        case "horse4":
          this.imgSrc = (await import("@/assets/img/horse/horse4.png")).default;
          break;
        case "horse5":
          this.imgSrc = (await import("@/assets/img/horse/horse5.png")).default;
          break;
        default:
          this.imgSrc = "/path/to/default-image.png"; // 기본 이미지 경로
          break;
      }
    } catch (error) {
      console.error("Error loading image:", error);
      this.imgSrc = "/path/to/default-image.png"; // 오류 발생 시 기본 이미지 경로
    }
  },
  methods: {
    MouseOver() {
      this.isMouseOver = true;
    },
    MouseLeave() {
      this.isMouseOver = false;
    },
    selectHorse() {
      this.$emit("selectHorse", this.horse);
    },
  },
};
</script>

<style>
</style>
