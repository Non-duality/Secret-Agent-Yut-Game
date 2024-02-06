<template>
  <div 
  id="liveView" 
  class="videoView"
  ref="liveView">
    <video
    class="media"
    id="webcam"
    ref="webcam"
    autoplay
    playsinline>
    </video>
  </div>
</template>

<script>
  // 모션 인식 라이브러리
  import { FaceDetector, FilesetResolver } from '@mediapipe/tasks-vision';
  export default {
    data() {
      return {
        // 웹 캠 관련'
        faceDetector: null,
        webcamRunning: false,
        children: [],
        lastVideoTime: -1,
        constraints: {
          video: true
        },
        
        // 제스처 인식 결과
        results: undefined,

        // 게임 카운트
        countdown: 5,
      }
    },

    mounted() {
      this.createFaceDetector()
    },

    methods: {
      async createFaceDetector() {
        const vision = await FilesetResolver.forVisionTasks(
          "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.0/wasm"
        );
        this.faceDetector = await FaceDetector.createFromOptions(vision, {
          baseOptions: {
            modelAssetPath: `https://storage.googleapis.com/mediapipe-models/face_detector/blaze_face_short_range/float16/1/blaze_face_short_range.tflite`,
            delegate: "GPU"
          },
          runningMode: "VIDEO"
        });
        this.video = this.$refs.webcam;
        this.liveView = this.$refs.liveView;
        // FaceDetector 초기화 후 카운트다운 시작
        this.startCountdown();
      },


      // 카운트다운 시작
      async startCountdown() {
        const countdownInterval = setInterval(() => {
          this.countdown -= 1
          if (this.countdown === 0) {
            clearInterval(countdownInterval)
            this.toggleWebcam()
          }
        }, 1000)
      },


      // 웹 캠 토글
      async toggleWebcam() {
        if (!this.faceDetector) {
          alert("제스처가 인식기가 로드되길 기다려주세요");
          return;
        }
        // 웹 캠활성화
        this.webcamRunning = true
        try {
          const stream = await navigator.mediaDevices.getUserMedia(this.constraints)
          this.video.srcObject = stream
          this.video.addEventListener("loadeddata", this.predictWebcam);
          console.log("캠 로드되었습니다.")
        } catch (error) {
          alert("웹 캠에 접근할 수가 없습니다.")
          console.log(error)
        }
      },


      // 모션 인식 시작
      async predictWebcam() {
        // await this.faceDetector.setOptions({ runningMode: "video" });
        // 현재 시간
        let nowInMs = Date.now()
        // 비디오 현재 시간과 마지막 시간과 다르면
        if (this.video.currentTime != this.lastVideoTime) {
          this.lastVideoTime = this.video.currentTime
          const results = await this.faceDetector.detectForVideo(this.video, nowInMs);
          if (results && results.detections) {
            this.results = results.detections
            this.displayVideoDection(this.results)
          }
          
        }
        if (this.webcamRunning) {
          window.requestAnimationFrame(this.predictWebcam)
        }
      },


      // 얼굴 모션 결과 처리
      displayVideoDection(results) {
        for (let child of this.children) {
          this.liveView.removeChild(child);
        }
        this.children.splice(0);

        for (let result of results) {
          const p = document.createElement("p")
          p.innerText = 
            "정확도: " +
            Math.round(parseFloat(result.categories[0].score) * 100) +
            "%"
          p.style =
            "left: " +
            (this.video.offsetWidth - 
            result.boundingBox.width -
            result.boundingBox.originX) +
            "px;" +
            "top: " +
            (result.boundingBox.originY - 30) +
            "px; " +
            "width: " +
            (result.boundingBox.width - 10) +
            "px;";

            const highlighter = document.createElement("div")
            highlighter.setAttribute("class", "highlighter")
            highlighter.style =
              "left: " +
              (this.video.offsetWidth -
                result.boundingBox.width -
                result.boundingBox.originX) +
              "px;" +
              "top: " +
              result.boundingBox.originY +
              "px;" +
              "width: " +
              (result.boundingBox.width - 10) +
              "px;" +
              "height: " +
              result.boundingBox.height +
              "px;";

            this.liveView.appendChild(highlighter)
            this.liveView.appendChild(p)

            this.children.push(highlighter)
            this.children.push(p)

            for (let keypoint of result.keypoints) {
              const keypointEl = document.createElement("span")
              keypointEl.className = "key-point"
              keypointEl.style.top = `${keypoint.y * this.video.offsetHeight - 3}px`;
              keypointEl.style.left = `${this.video.offsetWidth - keypoint.x * this.video.offsetWidth - 3}px`;
              this.liveView.appendChild(keypointEl)
              this.children.push(keypointEl)
            }
        }
      }
    }
  }
</script>

<style scoped>
@import url('@/assets/css/mission/cham.css');
</style>