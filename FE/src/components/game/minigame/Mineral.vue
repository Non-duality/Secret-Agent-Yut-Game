<template>
  <!-- 웹캠 화면 -->
  <div id="liveView" class="videoView">
    <div class="media-container" style="position: relative;">
      <!-- 웹캠 -->
      <video
        class="media"
        id="webcam"
        ref="webcam"
        autoplay
        playsinline
      ></video>
      <!-- 캔버스 -->
      <canvas
        class="media"
        id="jewel-canvas"
        ref="jewel_canvas"
        style="position: absolute; left: auto; top: 0px;"
      ></canvas>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hands: null,
      webcam: null,
      canvas: null,
    }
  },

  mounted() {
    this.initializeMedia();
  },
  

  methods: {
    async initializeMedia() {
      this.webcam = this.$refs.webcam;
      this.canvas = this.$refs.jewel_canvas;


      // 웹 캠 활성화
      const stream = await navigator.mediaDevices.getUserMedia({ video: true});
      this.webcam.srcObject = stream;

      // 미디어파이프 손 모델 초기화
      this.hands = new window.Hands({ locateFile: (file) => `https://cdn.jsdelivr.net/npm/@mediapipe/hands/${file}` });
      this.hands.setOptions({
        selfieMode: true,
        maxNumHands: 1,
      });
      this.hands.onResults(this.onHandResults);

      // 미디어파이프 프로세싱 시작
      this.hands.initialize().then(() => {
        this.hands.start();
      });
    },
    onHandResults(results) {
      // 손 랜드마크 사용하는 게임 로직
      const landmarks = results.multiHandLandmarks;
      if (landmarks) {
        for (const landmark of landmarks[0]) {
          // 랜드마크 좌표 출력
          const x = landmark.x * this.canvas.width;
          const y = landmark.y * this.canvas.height;
          console.log(`Landmark: (${x}, ${y})`)

          // 게임 로직 추가
        }
      }
    },
  },
  beforeDestroy() {
    // 컴포넌트 파기 시 미디어파이프 정지 및 리소스 해제
    if (this.hands) {
      this.hands.stop();
      this.hands.close();
    }
    if (this.webcam) {
      const stream = this.webcam.srcObject;
      if (stream) {
        const tracks = stream.getTracks();
        tracks.forEach((track) => track.stop());
      }
    }
  },
};
</script>

<style scoped>
/* 거울모드 */
.media {
  transform: rotateY(180deg);
  -webkit-transform:rotateY(180deg); 
}
</style>