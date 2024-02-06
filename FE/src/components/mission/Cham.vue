<template>
  <div id="liveView" class="videoView">
    <video ref="webcam" autoplay playsinline @loadedmetadata="onVideoLoaded"></video>
    <div v-if="showCountdown" class="countdown">{{ countdown }}</div>
  </div>
</template>

<script>
import { FaceDetector, FilesetResolver } from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.0";

export default {
  data() {
    return {
      children: [],
      faceDetector: null,
      showCountdown: false,
      countdown: 3,
    };
  },

  methods: {
    // 사용자 카메라 준비
    hasGetUserMedia() {
      return !!navigator.mediaDevices?.getUserMedia;
    },

    // 초기화
    async initializeFaceDetector() {
      const vision = await FilesetResolver.forVisionTasks(
        "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.0/wasm"
      );
      this.faceDetector = await FaceDetector.createFromOptions(vision, {
        baseOptions: {
          modelAssetPath: `/models/blaze_face_short_range.tflite`,
          delegate: "GPU"
        },
        runningMode: "VIDEO"
      });

      // 초기화 후 비디오 활성화
      this.enableCam();
    },


    // 캠 사용
    async enableCam() {
      if (!this.faceDetector) {
        alert("Face Detector is still loading. Please try again.");
        return;
      }
      const webcam = this.$refs.webcam;
      const constraints = { video: true };
      try {
        const stream = await navigator.mediaDevices.getUserMedia(constraints);
        webcam.srcObject = stream;
        webcam.addEventListener('loadedmetadata', () => {
          this.predictWebcam();
        })
      } catch (err) {
        console.error(err);
      }
    },

    // 비디오 로딩 완료 핸들러
    onVideoLoaded() {
      // 비디오 메타데이터 로딩 시점에서 카운트다운 시작
      this.showCountdown = true;
      this.startCountdown();
    },

    // 카운트다운 시작
    startCountdown() {
      const countdownInterval = setInterval(() => {
        this.countdown -= 1;
        if (this.countdown === 0) {
          clearInterval(countdownInterval);
          // 3초 카운트다운 후에 얼굴 인식 시작
          this.predictWebcam();
        }
      }, 1000);
    },

    // 제스처 인식 시작
    async predictWebcam() {
      const webcam = this.$refs.webcam;
      if (!webcam || webcam.readyState !== HTMLMediaElement.HAVE_ENOUGH_DATA) {
        window.requestAnimationFrame(this.predictWebcam);
        return;
      }

      let startTimeMs = performance.now();
      if (webcam.videoWidth > 0 && webcam.videoHeight > 0) {
        try {
          const detections = await this.faceDetector.detectForVideo(webcam, startTimeMs).detections;
          this.displayVideoDetections(detections);
        } catch (error) {
          console.error("얼굴 인식 중 오류 발생:", error);
        }
      } else {
        console.log("유효하지 않은 비디오 프레임");
      }
      window.requestAnimationFrame(this.predictWebcam);
    },

    // 화면상 얼굴점 그리기
    displayVideoDetections(detections) {
      const webcam = this.$refs.webcam;
      this.children.forEach(child => {
        child.remove();
      });
      this.children = [];

      const liveView = document.getElementById("liveView")

      detections.forEach(detection => {
        const p = document.createElement("p");
        p.innerText = `Confidence: ${Math.round(parseFloat(detection.categories[0].score) * 100)}%`;
        p.style.left = `${webcam.offsetWidth - detection.boundingBox.width - detection.boundingBox.originX}px`;
        p.style.top = `${detection.boundingBox.originY - 30}px`;
        p.style.width = `${detection.boundingBox.width - 10}px`;
        p.style.position = 'absolute';
        p.style.backgroundColor = '#007f8b';
        p.style.color = '#fff';
        p.style.fontSize = '12px';
        p.style.padding = '5px';
        p.style.border = '1px dashed rgba(255, 255, 255, 0.7)';
        p.style.zIndex = '2';

        const highlighter = document.createElement("div");
        highlighter.setAttribute("class", "highlighter");
        highlighter.style.left = `${webcam.offsetWidth - detection.boundingBox.width - detection.boundingBox.originX}px`;
        highlighter.style.top = `${detection.boundingBox.originY}px`;
        highlighter.style.width = `${detection.boundingBox.width - 10}px`;
        highlighter.style.height = `${detection.boundingBox.height}px`;
        highlighter.style.position = 'absolute';
        highlighter.style.border = '1px dashed #fff';
        highlighter.style.zIndex = '1';
        highlighter.style.backgroundColor = 'rgba(0, 255, 0, 0.25)';

        liveView.appendChild(highlighter);
        liveView.appendChild(p);
        this.children.push(highlighter, p);

        detection.keypoints.forEach(keypoint => {
          const keyPointEl = document.createElement("span");
          keyPointEl.className = "key-point";
          keyPointEl.style.top = `${keypoint.y * webcam.offsetHeight - 3}px`;
          keyPointEl.style.left = `${webcam.offsetWidth - keypoint.x * webcam.offsetWidth - 3}px`;
          keyPointEl.style.position = 'absolute';
          keyPointEl.style.width = '6px';
          keyPointEl.style.height = '6px';
          keyPointEl.style.border = '1px solid #ffffff';
          keyPointEl.style.backgroundColor = '#ff0000';
          keyPointEl.style.borderRadius = '50%';
          keyPointEl.style.display = 'block';

          liveView.appendChild(keyPointEl);
          this.children.push(keyPointEl);

        });
      });
    },
  },

  mounted() {
    if (this.hasGetUserMedia()) {
      this.initializeFaceDetector();
    } else {
      console.warn("getUserMedia() is not supported by your browser");
    }
  },
};
</script>

<style scoped>
@import url('@/assets/css/mission/cham.css');
</style>
