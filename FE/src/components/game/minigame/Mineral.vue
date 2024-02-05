<template>
  <div>
    <video ref="video" width="640" height="480" autoplay></video>
    <canvas ref="canvas" width="640" height="480"></canvas>
    
    <!-- 웹 캠에 광물 띄우기 -->
    <img 
      v-if="mineralImageLoaded"
      ref="mineralImage"
      src="@/assets/img/game/minigame/mineral.png" 
      alt="Mineral"
      style="position: absolute; top: 150px; left: 350px; width: 250px; height: 250px;"
      >
  </div>
</template>

<script>
import * as posenet from '@tensorflow-models/posenet';

export default {
  data() {
    return {
      mineralImageLoaded: true,
    };
  },

  mounted() {
    this.initPosenet();
  },

  methods: {
    async initPosenet() {
      const video = this.$refs.video;
      const canvas = this.$refs.canvas;
      const ctx = canvas.getContext('2d');

      // 웹캠 스트림 가져오기
      const stream = await navigator.mediaDevices.getUserMedia({ video: true });
      video.srcObject = stream;

      // Posenet 모델 로드
      const net = await posenet.load();

      // 실시간으로 동작 감지
      this.detectPose(net, video, ctx);
    },

    async detectPose(net, video, ctx) {
      while (true) {
        const pose = await net.estimateSinglePose(video);
        this.drawPose(pose, ctx);
        await new Promise((resolve) => requestAnimationFrame(resolve));
      }
    },

    drawPose(pose, ctx) {
      // 감지된 손의 위치
      const hand = pose.keypoints.find((point) => point.part === 'leftWrist');

      if (hand) {
        // 화면 좌표에서 캔버스 좌표로 변환
        const canvasX = hand.position.x * (canvas.width / video.width);
        const canvasY = hand.position.y * (canvas.height / video.height);

        // '손이다'를 콘솔에 출력
        console.log('손이다');

        // 캔버스에 감지된 손목 좌표에 점 그리기
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = 'red';
        ctx.beginPath();
        ctx.arc(canvasX, canvasY, 5, 0, 2 * Math.PI);
        ctx.fill();

        // 주먹 이미지 위치
        const mineralImage = this.$refs.mineralImage;
        const mineralPosition = mineralImage.getBoundingClientRect();

        // 주먹이 광물과 충돌하는지 확인 (충돌 검사)
        if (
          hand.position.x > mineralPosition.left ||
          hand.position.x < mineralPosition.left + mineralPosition.width ||
          hand.position.y > mineralPosition.top ||
          hand.position.y < mineralPosition.top + mineralPosition.height
        ) {
          // 광물을 부수는 로직을 추가 (예: 광물을 화면에서 제거)
          console.log('광물 부수기!');
        }

      }
    },
  },
};
</script>

<style scoped>
/* 필요한 스타일링 추가 */
</style>
