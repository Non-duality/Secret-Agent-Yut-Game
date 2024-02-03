<template>
    <!-- 라이브 웹캠 화면을 표시하는 컨테이너 -->
    <div id="liveView" class="videoView">
      <!-- 웹캠 활성화/비활성화 버튼 -->
      <button
        id="webcamButton"
        class="mdc-button mdc-button--raised"
        @click="toggleWebcam"
      >
        <span class="mdc-button__ripple"></span>
        <span class="mdc-button__label">{{ webcamButtonText }}</span>
      </button>
      <div style="position: relative;">
        <!-- 웹캠 비디오 요소 -->
        <video
          id="webcam"
          ref="webcam"
          autoplay
          playsinline
          :style="{ height: videoHeight, width: videoWidth }"
        ></video>
        <!-- 웹캠 비디오 위에 그릴 캔버스 -->
        <canvas
          class="output_canvas"
          id="output_canvas"
          width="1280"
          height="720"
          style="position: absolute; left: 0px; top: 0px;"
        ></canvas>
        <!-- 제스처 결과를 표시할 요소 -->
        <div id="gesture_output" class="output" v-if="gestureOutput">
          <p>손인식 명: {{ categoryName }}</p>
          <p>손인식 정확도: {{ categoryScore }}</p>
          <p>왼손 혹은 오른손: {{ handedness }}</p>
          <p>x축: {{ xAxis }} y축: {{ YAxis }}</p>
          <p></p>
        </div>
      </div>
    </div>
</template>

<script>
  import { GestureRecognizer, FilesetResolver, DrawingUtils } from '@mediapipe/tasks-vision';
  export default {
    data() {
      return {
        gestureRocognizer: GestureRecognizer,
        runningMode: "VEDIO", // 웹캠 실행 모드
        webcamButtonText: "웹 캠 활성화", // 웹캠 활성화 버튼 텍스트
        webcamRunning: false, // 웹캠 실행 여부
        lastVideoTime: -1, // 비디오의 마지막 시간
        results: undefined, // 제스처 인식 결과
        constraints: {
          video: true
        },
        // 아웃풋 결과 보기 위한 것

        gestureOutput: false,
        
        categoryName: "",
        categoryScore: 0,
        handedness: "",
        xAxis: "",
        YAxis: "",

        videoHeight: "360px",
        videoWidth: "480px"

      }
    },
    
    mounted() {
      this.createGestureRecognizer(); // 제스처 인식기 초기화
    },

    methods: {
      // 제스처 인식기 초기화
      async createGestureRecognizer() {
        const vision = await FilesetResolver.forVisionTasks(
          "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3/wasm"
        );
        this.gestureRecognizer = await GestureRecognizer.createFromOptions(
          vision,
          {
            baseOptions: {
              modelAssetPath:
                "/models/gesture_recognizer.task",
              delegate: "GPU"
            },
            runningMode: this.runningMode,
            numHands: 2
          }
        );
      },

      // 유저 웹 캠 접근 여부 확인
      hasGetUserMedia() {
        return !!(navigator.mediaDevices && navigator.mediaDevices.getUserMedia);
      },

      // 웹 캠 토글
      toggleWebcam() {
        // 제스처 인식기 초기화 덜 됐을 경우
        if (!this.gestureRecognizer) {
          alert("제스처 인식기가 로드되길 기다려주세요");
          return;
        }
        if (this.webcamRunning) {
          // 웹 캠 비활성화
          this.webcamRunning = false;
          this.gestureOutput = false
          this.webcamButtonText = "제스처 인식 시작하기";
        } else {
          // 웹 캠 활성화
          this.webcamRunning = true;
          this.gestureOutput = true
          this.webcamButtonText = "제스처 인식 끝내기";
        }

        if (this.webcamRunning) {
          // 웹캠 활성화
          navigator.mediaDevices.getUserMedia(this.constraints).then((stream) => {
            const videoElement = this.$refs.webcam;
            videoElement.srcObject = stream;
            videoElement.addEventListener("loadeddata", this.predictWebcam);
          });
        }
      },
      
      // 모션 인식 시작
      async predictWebcam() {
        // 비디오 요소 가져오기
        const videoElement = this.$refs.webcam;
        // 현재 시간
        let nowInMs = Date.now();
        // 비디오 현재 시간가 마지막 시간과 다르면
        if (videoElement.currentTime !== this.lastVideoTime) {
          // 마지막 시간 현재 시간으로 업데이트
          this.lastVideoTime = videoElement.currentTime;
          this.results = this.gestureRecognizer.recognizeForVideo(
            videoElement,
            nowInMs
          );
        }

        // 손 제스쳐 그릴 준비
        const canvasElement = document.getElementById("output_canvas");
        const canvasCtx = canvasElement.getContext("2d");
        // 현재 캔버스 상태를 저장
        canvasCtx.save();
        // 캔버스 내용 지우기
        canvasCtx.clearRect(0, 0, canvasElement.width, canvasElement.height)
        const drawingUtils = new DrawingUtils(canvasCtx);

        // 제스처 인식 사이즈 조절
        canvasElement.style.height = this.videoHeight;
        videoElement.style.height = this.videoHeight;
        canvasElement.style.width = this.videoWidth;
        videoElement.style.width = this.videoWidth;

        if (this.results.landmarks) {
          for (const landmarks of this.results.landmarks) {
            // 제스처 랜드마크 그리기
            drawingUtils.drawConnectors(
              landmarks,
              GestureRecognizer.HAND_CONNECTIONS,
              {
                color: "#00FF00",
                lineWidth: 5
              }
            );
            drawingUtils.drawLandmarks(landmarks, {
              color: "#FF0000",
              lineWidth: 2
            });
          }
        }
        // 2D 컨텍스트에 대한 상태를 이전 상태로 복원
        canvasCtx.restore();

        // 인식된 손 정보
        if (this.results.gestures.length > 0) {
          this.categoryName = this.results.gestures[0][0].categoryName
          this.categoryScore = parseFloat(this.results.gestures[0][0].score * 100).toFixed(2) 
          this.handedness = this.results.handednesses[0][0].displayName
          // 화면 왼쪽이 0
          this.xAxis = this.results.landmarks[0][9].x
          // 화면 위쪽이 0
          this.YAxis = this.results.landmarks[0][9].y
        } else {
          this.gestureOutputText = false;
        }

        if (this.webcamRunning) {
          window.requestAnimationFrame(this.predictWebcam);
        }
      }
    }

  }
</script>

<style scoped>
.output {
  background-color: white;
}
</style>