<template>
  <div>
    <div id="liveView" class="videoView">
      <button id="webcamButton" class="mdc-button mdc-button--raised" @click="toggleWebcam">
        <span class="mdc-button__ripple"></span>
        <span class="mdc-button__label">{{ webcamButtonText }}</span>
      </button>
      <div style="position: relative;">
        <video ref="webcam" id="webcam"  style="position: absolute;" autoplay playsinline></video>
        <canvas ref="outputCanvas" class="output_canvas" id="outputCanvas" style="position: absolute; left: 0px; top: 0px;"></canvas>
      </div>
    </div>
    <ul v-if="blendShapes.length" class="blend-shapes-list" id="video-blend-shapes">
      <li v-for="(shape, index) in blendShapes[0]" :key="index" class="blend-shapes-item">
        <span class="blend-shapes-label">{{ shape.categoryName }}</span>
        <span class="blend-shapes-value" :style="{ width: `calc(${shape.score * 100}% - 120px)` }">
          {{ shape.score.toFixed(4) }}
        </span>
      </li>
    </ul>
  </div>
</template>

<script>
import vision from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3";
const { FaceLandmarker, FilesetResolver, DrawingUtils } = vision;

export default {
  data() {
    return {
      webcamRunning: false,
      faceLandmarker: null,
      blendShapes: [],
      webcamButtonText: '웹캠 활성화',
      lastVideoTime: -1,
      results: null,
      videoWidth: 480,
    };
  },
  async created() {
    await this.createFaceLandmarker();
  },
  methods: {
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
    },
    toggleWebcam() {
      if (!this.faceLandmarker) {
        console.log("faceLandmarker가 아직 로드되지 않았습니다.");
        return;
      }
      this.webcamRunning = !this.webcamRunning;
      this.webcamButtonText = this.webcamRunning ? '웹캠 비활성화' : '웹캠 활성화';
      if (this.webcamRunning) {
        this.startWebcam();
      } else {
        this.stopWebcam();
      }
    },
    async startWebcam() {
      const constraints = {
        video: true,
      };
      const stream = await navigator.mediaDevices.getUserMedia(constraints)
      const video = this.$refs.webcam
      video.srcObject = stream
      video.addEventListener("loadeddata", this.predictWebcam)
    },
    stopWebcam() {
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
    async predictWebcam() {
      if (!this.webcamRunning || !this.faceLandmarker) return;
      const video = this.$refs.webcam;
      const canvasElement = this.$refs.outputCanvas;
      const canvasCtx = canvasElement.getContext("2d");
      const drawingUtils = new DrawingUtils(canvasCtx);

      const radio = video.videoHeight / video.videoWidth;
      video.style.width = this.videoWidth + "px";
      video.style.height = this.videoWidth * radio + "px";
      canvasElement.style.width = this.videoWidth + "px";
      canvasElement.style.height = this.videoWidth * radio + "px";
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
      if (this.results && this.results.faceBlendshapes && this.results.faceBlendshapes.length > 0) {
        this.blendShapes = [this.results.faceBlendshapes[0].categories];
      } else {
        this.blendShapes = [];
      }
      if (this.webcamRunning === true) {
        window.requestAnimationFrame(this.predictWebcam);
      }
    },
  },
  beforeDestroy() {
    this.stopWebcam();
  },
};
</script>

<style scoped>

body {
  font-family: helvetica, arial, sans-serif;
  margin: 2em;
  color: #3d3d3d;
  --mdc-theme-primary: #007f8b;
  --mdc-theme-on-primary: #f1f3f4;
}


video {
  clear: both;
  display: block;
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
}


.videoView,
.detectOnClick,
.blend-shapes {
  position: relative;
  float: left;
  width: 48%;
  margin: 2% 1%;
  cursor: pointer;
}

.videoView p,
.detectOnClick p {
  position: absolute;
  padding: 5px;
  background-color: #007f8b;
  color: #fff;
  border: 1px dashed rgba(255, 255, 255, 0.7);
  z-index: 2;
  font-size: 12px;
  margin: 0;
}

.highlighter {
  background: rgba(0, 255, 0, 0.25);
  border: 1px dashed #fff;
  z-index: 1;
  position: absolute;
}

.canvas {
  z-index: 1;
  position: absolute;
  pointer-events: none;
}

.output_canvas {
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg);
  -moz-transform: rotateY(180deg);
}

.detectOnClick {
  z-index: 0;
}

.detectOnClick img {
  width: 100%;
}

.blend-shapes-item {
  display: flex;
  align-items: center;
  height: 20px;
}

.blend-shapes-label {
  display: flex;
  width: 120px;
  justify-content: flex-end;
  align-items: center;
  margin-right: 4px;
}

.blend-shapes-value {
  display: flex;
  height: 16px;
  align-items: center;
  background-color: #007f8b;
}
</style>
