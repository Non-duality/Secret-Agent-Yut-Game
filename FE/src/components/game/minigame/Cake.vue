<template>
  <div id="liveView" class="videoView">
    <div class="media-container">
      <video 
      class="media"
      ref="webcam" 
      id="webcam"  
      style="position: absolute;"
      autoplay 
      playsinline
      ></video>
      <canvas 
      ref="outputCanvas" 
      class="media" 
      id="outputCanvas" 
      style="position: absolute; left: 0px; top: 0px;"
      ></canvas>
      <canvas 
      ref="cakeCanvas" 
      class="media" 
      id="cakeCanvas" 
      style="position: absolute; left: 0px; top: 0px;"
      ></canvas>
    </div>
    <!-- 시작 전 타이머 -->
    <div v-if="showCountdown" class="countdown">{{ countdown }}</div>
  </div>
</template>

<script>
import vision from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3";
const { FaceLandmarker, FilesetResolver, DrawingUtils } = vision;
import cakeImage from '@/assets/img/mission/cake.png';
export default {
  data() {
    return {
      webcamRunning: false,
      faceLandmarker: null,
      blendShapes: [],
      webcamButtonText: '웹캠 활성화',
      lastVideoTime: -1,
      results: null,

      // 케이크
      cakeImg: new Image(),
      cakes: [],
      cakeDropInterval: null,
      cakeDropping: false,

      // 카운트다운
      countdown: 3,
      showCountdown: false
    };
  },

  async created() {
    await this.createFaceLandmarker();
  },

  mounted() {
    this.cakeImg.src = cakeImage
  },

  methods: {

    adjustCanvasSizeToVideo() {
      const webcam = this.$refs.webcam
      const faceCanvas = this.$refs.outputCanvas
      const cakeCanvas = this.$refs.cakeCanvas
      if (webcam && FaceCanvas && cakeCanvas) {
        faceCanvas.width = webcam.offsetWidth
        cakeCanvas.width = webcam.offsetWidth
        faceCanvas.height = webcam.offsetHeight
        cakeCanvas.height = webcam.offsetHeight
      }
    },


    async createFaceLandmarker() {
      const filesetResolver = await FilesetResolver.forVisionTasks(
        "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3/wasm"
      );
      this.faceLandmarker = await FaceLandmarker.createFromOptions(filesetResolver, {
        baseOptions: {
          modelAssetPath: `/models/face_landmarker.task`,
          delegate: "GPU"
        },
        outputFaceBlendshapes: true,
        runningMode: "VIDEO",
        numFaces: 1
      });
      const webcam = this.$refs.webcam
      webcam.addEventListener('loadedmetadata', () => {
        this.adjustCanvasSizeToVideo()
      })
      await this.enableCam()
    },


    async enableCam() {
      if (!this.faceLandmarker) {
        console.log("faceLandmarker가 아직 로드되지 않았습니다.");
        return;
      }
      this.webcamRunning = true
      const video = this.$refs.webcam
      const constraints = {
        video: true, 
      };
      try {
        const stream = await navigator.mediaDevices.getUserMedia(constraints)
        video.srcObject = stream
        video.addEventListener("loadeddata", () => {
          this.predictWebcam()
          this.startCountdown()
        });
      } catch (error) {
        console.log(error)
      }
    },


    startCountdown() {
      const countdownInterval = setInterval(() => {
        this.countdown -= 1;
        if (this.countdown <= 0) {
          clearInterval(countdownInterval);
          // 3초 카운트다운 후에 얼굴 인식 시작
          this.showCountdown = false
          this.startCakeDropping()
        }
      }, 1000);
    },

    async predictWebcam() {
      if (!this.webcamRunning || !this.faceLandmarker) return;
      const video = this.$refs.webcam;
      const canvasElement = this.$refs.outputCanvas;
      const canvasCtx = canvasElement.getContext("2d");
      const drawingUtils = new DrawingUtils(canvasCtx);
      // 캔버스랑 비디오 크기 맞추기
      const ratio = video.videoHeight / video.videoWidth;
      canvasElement.style.width = video.videoWidth + "px";
      canvasElement.style.height = video.videoHeight * ratio + "px";
      canvasElement.width = video.videoWidth;
      canvasElement.height = video.videoHeight;

      if (this.lastVideoTime !== video.currentTime) {
        this.lastVideoTime = video.currentTime;
        this.results = await this.faceLandmarker.detectForVideo(video, performance.now());
      }
      if (this.results && this.results.faceLandmarks) {
        for (const landmarks of this.results.faceLandmarks) {
          // 입술 감지
          drawingUtils.drawConnectors(
            landmarks,
            FaceLandmarker.FACE_LANDMARKS_LIPS,
            { color: "red" }
          );
        }
      }
      if (this.webcamRunning === true) {
        window.requestAnimationFrame(this.predictWebcam);
      }
    },


    // 케이크 떨어뜨리기
    startCakeDropping() {
      this.cakes = []
      this.cakeDropping = true
      // 캔버스 크기 조정
      const video = this.$refs.webcam;
      console.log(video.videoHeight)
      const canvasElement = this.$refs.cakeCanvas;
      const ratio = video.videoHeight / video.videoWidth;
      canvasElement.style.width = video.videoWidth + "px";
      canvasElement.style.height = video.videoHeight * ratio + "px";
      canvasElement.width = video.videoWidth;
      canvasElement.height = video.videoHeight;
      if (this.cakeDropInterval) clearInterval(this.cakeDropInterval)
      this.cakeDropInterval = setInterval(() => {
        this.cakes.push({
          x: Math.random() * (canvasElement.width - 50),
          y: -50
        })
      }, 500)

      const cakeDrop = () => {
        if (!this.cakeDropping) {
          clearInterval(this.cakeDropInterval)
          return
        }
        this.drawCake()
        requestAnimationFrame(cakeDrop)
      }
      cakeDrop()
    },


    // 케이크 그리기
    drawCake() {
      const canvasElement = this.$refs.cakeCanvas
      const ctx = canvasElement.getContext('2d')
      ctx.clearRect(0, 0, canvasElement.width, canvasElement.height)
      this.cakes.forEach(cake => {
        cake.y += 6;
        ctx.drawImage(this.cakeImg, cake.x, cake.y, 30, 30)
        // 화면 밖으로 나간 케이크 제거
        if (cake.y > canvasElement.height) {
          this.cakes.shift()
        }
      })
    },


    // 캠 중단
    stopWebcam() {
      // 케이크 떨어지는 것 중단
      this.stopCakeDropping()
      const video = this.$refs.webcam
      if (video.srcObject) {
        const stream = video.srcObject
        const track = stream.getTracks()
        tracks.forEach((track) => {
          track.stop();
        });
        video.srcObject = null
      }
      const tracks = stream.getTracks();
      this.$refs.webcam.srcObject = null;
    },


    // 케이크 떨어뜨리기 중단
    stopCakeDropping() {
      this.cakeDropping = false
      if (this.cakeDropInterval) {
        clearInterval(this.cakeDropInterval)
        this.cakeDropInterval = null
      }
    },

  },

  // 끝내면 다 버리기
  beforeDestroy() {
    this.stopWebcam();
  },
};
</script>

<style scoped>
@import url('@/assets/css/game/minigame/cake.css');
</style>
