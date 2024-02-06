<template>
  <!-- 웹캠 화면 -->
  <div id="liveView" class="videoView">
    <div class="media-container" style="position: relative;">
      <!-- 웹캠 : 광물 랜덤으로 배치 -->
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
    <!-- 게임 타이머 -->
    <div class="game-timer" v-if="gestureOutput">
        {{ gameTimer }}
      </div>
      <!-- 시작 전 타이머 -->
      <div class="start-timer" v-else>
        {{ countdown }}
      </div>
      <!-- 게임 승리 -->
      <div class="victory" v-if="gameResultDp">
        <div v-if="gameResult">게임승리</div>
        <div v-else>게임패배</div>
      </div>
  </div>
</template>

<script>
// 모션 인식 라이브러리
import { GestureRecognizer, FilesetResolver, DrawingUtils } from '@mediapipe/tasks-vision';
// 광물 이미지
import jewelImage from '@/assets/img/game/minigame/mineral.png'

export default {
  data() {
    return {
      // 웹 캠 관련
      gestureRocognizer: GestureRecognizer,
        runningMode: "VEDIO", // 웹캠 실행 모드
        webcamButtonText: "제스처 인식 로드 전", // 웹캠 활성화 버튼 텍스트
        webcamRunning: false, // 웹캠 실행 여부
        lastVideoTime: -1, // 비디오의 마지막 시간
        constraints: {
          video: true
        },

        // 제스쳐 인식 결과
        results: undefined,

        // 아웃풋 결과
        gestureOutput: false,
        leftCategoryName: "",
        leftCategoryScore: 0,
        leftXAxis: "",
        leftYAxis: "",
        rightCategoryName: "",
        rightCategoryScore: 0,
        rightXAxis: "",
        rightYAxis: "",
        gestureSequence: [],
        lastGetureTime: Date.now(),

        // 광물 띄우기, 초기 위치, 방향, 속도 지정
        jewelImg: new Image(),
        jewelPosition: { x: Math.random(), y: Math.random() },
        jewelDirection: { x: (Math.random() - 0.5) * 50, y: (Math.random() - 0.5) * 50 },
        jewels: [],
        numOfJewels: 3,

        // 게임 카운트
        countdown: 5,
        gameTimer: 10,

        // 게임 결과
        gameResultDp: false,    // 게임 결과 표시 여부
        gameResult: false,      // 게임 승패 여부
    } 
  },

  mounted() {
    this.createGestureRecognizer();   // 제스처 인식기 초기화
    this.startCountdown();            // 게임 시작 카운트
    window.addEventListener('resize', this.adjustCanvasSizeToVideo)
  },
  
  // 메모리 정리
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

  methods: {
    // 캔버스 크기 조정
    adjustCanvasSizeToVideo() {
      const videoElement = this.$refs.webcam
      const jewelCanvasElement = this.$refs.jewel_canvas
      if (videoElement && jewelCanvasElement) {
        jewelCanvasElement.width = videoElement.offsetWidth
        jewelCanvasElement.height = videoElement.offsetHeight
      }
    },

    // 제스처 인식기 초기화 및 캔버스 크기 설정
    async createGestureRecognizer() {
      const vision = await FilesetResolver.forVisionTasks(
        "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@latest/wasm"
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

      const videoElement = this.$refs.webcam;
      videoElement.addEventListener('loadedmetadata', () => {
        // 로드되면 비디오 사이즈에 맞추어 캔버스 사이즈 맞추기
        this.adjustCanvasSizeToVideo()
      })
    },

    // 카운트다운 시작
    startCountdown() {
      const countdownInterval = setInterval(() => {
        this.countdown -= 1
        if (this.countdown === 0) {
          clearInterval(countdownInterval)
          this.startGameTimer();
        }
      }, 1000)
    },


    // 게임시작
    startGameTimer() {
      this.toggleWebcam()
      const gameInterval = setInterval(() => {
        this.gameTimer -= 1
        if (this.gameTimer === 0) {
          clearInterval(gameInterval)
          this.webcamRunning = false
          this.clearFlyCanvas()
          this.gameResultDp = true
        }
      }, 1000)
    },

    // 웹 캠 토글
    async toggleWebcam() {
      // 제스처 인식기 초기화 덜 됐을 경우
      if (!this.gestureRocognizer) {
        alert("제스처 인식기가 로드되길 기다려주세요");
        return;
      }
      // 웹 캠 활성화
      this.webcamRunning = true;
      this.gestureOutput = true
      this.webcamButtonText = "제스처 인식 로드 완료";
      try {
        const stream = await navigator.mediaDevices.getUserMedia(this.constraints)
        const videoElement = this.$refs.webcam;
        videoElement.srcObject = stream
        videoElement.addEventListener("loadeddata", this.predictWebcam)
        this.initializeJewels();
      } catch (error) {
        alert("웹 캠에 접근할 수가 없습니다.")
        console.log(error)
      }
    },


    // 모션 인식 시작
    async predictWebcam() {
      // 비디오 요소 가져오기
      const videoElement = this.$refs.webcam;
      // 현재 시간
      let nowInMs = Date.now();
      // 비디오 현재 시간과 마지막 시간이 다르면
      if (videoElement.currentTime !== this.lastVideoTime) {
        // 마지막 시간 현재 시간으로 업데이트
        this.lastVideoTime = videoElement.currentTime;
        this.results = this.gestureRocognizer.recognizeForVideo(videoElement, nowInMs);
      }
      // 손제스처 결과 처리
      this.results.gestures.forEach((gesture, index) => {
        const categoryName = gesture[0].categoryName
        const currentTime = Date.now()
        const timeDiff = currentTime - this.lastGetureTime
        if (timeDiff < 100) {
          this.gestureSequence.push(categoryName)
        } else {
          this.gestureSequence = [categoryName];
        }
        this.lastGetureTime = currentTime;
      })
    
      // 유효한 상태 변화 확인
      if (this.isValidGestureChange()) {
        if (this.checkDigJewel()) {
          console.log('광물 캐기 성공')
          this.webcamRunning = false
          this.clearFlyCanvas()
          this.gameResult = true
          this.gameResultDp = true
        } 
      }
      // 인식된 손 정보
      if (this.results.gestures.length == 1) {
        this.leftCategoryName = this.results.gestures[0][0].categoryName
        this.leftCategoryScore = parseFloat(this.results.gestures[0][0].score * 100).toFixed(2) 
        this.leftXAxis = this.results.landmarks[0][9].x
        this.leftYAxis = this.results.landmarks[0][9].y
      } else if (this.results.gestures.length == 2) {
        this.leftCategoryName = this.results.gestures[0][0].categoryName
        this.leftCategoryScore = parseFloat(this.results.gestures[0][0].score * 100).toFixed(2) 
        this.leftXAxis = this.results.landmarks[0][9].x
        this.leftYAxis = this.results.landmarks[0][9].y
        this.rightCategoryName = this.results.gestures[1][0].categoryName
        this.rightCategoryScore = parseFloat(this.results.gestures[1][0].score * 100).toFixed(2) 
        this.rightXAxis = this.results.landmarks[1][9].x
        this.rightYAxis = this.results.landmarks[1][9].y
      } else {
        this.gestureOutputText = false;
      }
      // 손모양 계속 인식을 위해 계속 호출
      if (this.webcamRunning) {
        window.requestAnimationFrame(this.predictWebcam);
      }
    },

    // 광물 초기화
    initializeJewels() {
      this.jewels = Array.from({ length: this.numOfJewels}).map(() => ({
        position: { x: Math.random(), y: Math.random() },
        direction: { x: (Math.random() - 0.5) * 30, y: (Math.random() - 0.5) * 30 },
      }))

      this.jewelImg.src = jewelImage;
      this.jewelImg.onload = () => {
        console.log('이미지가 성공적으로 로드되었습니다.');
        this.animateJewel();
      };
      this.jewelImg.onerror = () => {
        console.log('이미지 로드 중에 오류가 발생했습니다.')
      }
    },

    // 광물 이미지 고정하기
    drawJewel() {
      if (this.webcamRunning) {
        // 광물 이미지를 오른쪽에 고정하기
        const canvasElement = this.$refs.jewel_canvas;
        if (!canvasElement) {
          return console.log('캔버스 오류')
        }
        const canvasCtx = canvasElement.getContext("2d");

        // 광물 이미지의 폭과 높이
        const jewelWidth = 50;
        const jewelHeight = 50;

        // 캔버스의 폭과 높이
        const canvasWidth = canvasElement.width;
        const canvasHeight = canvasElement.height;

        // 오른쪽에 위치한 좌표 계산
        const xCoordinate = canvasWidth - jewelWidth;
        const yCoordinate = (canvasHeight - jewelHeight) / 2;

        // 광물 이미지 그리기
        canvasCtx.clearRect(0, 0, canvasWidth, canvasHeight);
        canvasCtx.drawImage(this.jewelImg, xCoordinate, yCoordinate, jewelWidth, jewelHeight);
      }
    },


    // 광물에 손이 닿였는지 판단





    // 광물 캔버스 클리어
    clearJewelCanvas() {
      const jewelCanvasElement = this.$refs.jewel_canvas;
      if (jewelCanvasElement) {
        const jewelCanvasCtx = jewelCanvasElement.getContext("2d");
        jewelCanvasCtx.clearRect(0, 0, jewelCanvasElement.width, jewelCanvasElement.height)
      }
    },
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