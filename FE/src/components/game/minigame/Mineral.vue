<template>
  <!-- 웹캠 화면 -->
  <div id="liveView" class="videoView">
    <div class="media-container" style="position: relative;">
      <!-- 웹캠 비디오 요소 -->
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
// 모션 인식 라이브러리
import { GestureRecognizer, FilesetResolver } from '@mediapipe/tasks-vision';
// 광물 이미지
import jewelImage from '@/assets/img/mission/mineral.png';
export default {
  data() {
    return {
      // 웹 캠 관련
      gestureRecognizer: GestureRecognizer,
        webcamRunning: false, // 웹캠 실행 여부
        lastVideoTime: -1, // 비디오의 마지막 시간

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
        lastGestureTime: Date.now(),

        // 광물 띄우기, 초기 위치 지정
        jewelImg: new Image(),
        jewelPosition: {x: null, y: null},
        initialJewelPositionSet: false,

        // 게임 결과
        gameResultDp: false,    // 게임 결과 표시 여부
        gameResult: false,      // 게임 승패 여부
    } 
  },

  mounted() {
    this.createGestureRecognizer();     // 제스처 인식기 초기화
    // this.predictWebcam()
  },
  
  // 메모리 정리
  beforeDestroy() {
  },

  methods: {

    // 모션 인식 작업을 위한 설정
    async createGestureRecognizer() {
      // 모션 인식 작업을 위한 WASM 파일 로드
      const vision = await FilesetResolver.forVisionTasks(
          "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.3/wasm"
      )
      // 제스처 인식 옵션
      this.gestureRocognizer = await GestureRecognizer.createFromOptions(
        vision,
        {
          baseOptions: {
            modelAssetPath:
              "/models/gesture_recognizer.task",    // 모델 저장 경로
            delegate: "GPU"                         // 모션 인식 처리 방식
          },
          runningMode: "VIDEO",       // 비디오 인식
          numHands: 2                 // 두 손 사용
        }
      )
      this.loadVideo()
    },

    
    
    
    // 비디오 로드 함수
    async loadVideo() {
      // 제스처 인식기가 로드되지 않았을 때, 알림 표시 후 함수 종료
      if (!this.gestureRocognizer) {
        alert("제스처 인식기가 로드되길 기다려주세요");
        return;
      }
      try {
        // 비디오 스트림 제약 조건: 비디오만 사용
        const constraints = {
          video: true
        }
        const stream = await navigator.mediaDevices.getUserMedia(constraints)     // 비디오 스트림 요청
        const videoEl = this.$refs.webcam;
        videoEl.srcObject = stream
      } catch (error) {
        alert("웹 캠에 접근할 수가 없습니다.")
        console.log(error)
      }
      const videoEl = this.$refs.webcam
      await videoEl.addEventListener('loadedmetadata', () => {
        // 로드되면 비디오 사이즈에 맞추어 캔버스 사이즈 맞추기
        this.adjustCanvasSizeToVideo()
        this.webcamRunning = true;
      })
      this.initializerJewel()             // 보석 이미지 초기화
    },

    
    // 캔버스 크기를 비디오 크기에 맞게 조정
    adjustCanvasSizeToVideo() {
      const videoEl = this.$refs.webcam
      const jewelCanvasEl = this.$refs.jewel_canvas
      // 비디오 요소와 보석 캔버스 요소가 모두 존재하면 크기 맞추기
      if (videoEl && jewelCanvasEl) {
        jewelCanvasEl.width = videoEl.offsetWidth
        jewelCanvasEl.height = videoEl.offsetHeight  
      }
    },
    

    // 보석 이미지 초기화
    initializerJewel() {
      this.jewelImg.src = jewelImage;
        this.jewelImg.onload = () => {
          console.log('이미지가 성공적으로 로드되었습니다.');
          this.drawJewel()
        };
        this.jewelImg.onerror = () => {
          console.log('이미지 로드 중에 오류가 발생했습니다.')
        }
    },


    // 보석 이미지 그리기
    async drawJewel() {
        const jewelCanvasEl = this.$refs.jewel_canvas;
        // 캔버스가 없는 경우 "캔버스가 오류났습니다!"
        if (!jewelCanvasEl) {
            return console.log("캔버스가 오류났습니다!")
        }
        const canvasCtx = await jewelCanvasEl.getContext("2d");   // 2d로 그리겠습니다
        // 이미지를 그리기 전에 캔버스 초기화
        canvasCtx.clearRect(0, 0, jewelCanvasEl.width, jewelCanvasEl.height);
        // 보석 이미지의 위치 설정
        let jewelX, jewelY;
        if (!this.initialJewelPositionSet) {
            // 보석 이미지의 초기 위치를 랜덤으로 설정
            jewelX = Math.random() * (jewelCanvasEl.width - 100); // 캔버스 너비 내에서 랜덤한 x 위치
            jewelY = Math.random() * (jewelCanvasEl.height - 100); // 캔버스 높이 내에서 랜덤한 y 위치
            this.jewelPosition.x = jewelX
            this.jewelPosition.y = jewelY
            this.initialJewelPositionSet = true;
        } else {
            // 이후에는 고정된 위치에 그리기
            jewelX = this.jewelPosition.x; // 고정된 x 위치
            jewelY = this.jewelPosition.y; // 고정된 y 위치
        }

        // 보석 이미지 그리기
        canvasCtx.drawImage(this.jewelImg, jewelX, jewelY, 100, 100);

        // 주기적으로 drawJewel 메서드 호출하여 보석 이미지를 계속해서 유지
        setTimeout(() => this.drawJewel(), 1000);
    },


    // 모션 인식 시작
    async predictWebcam() {
      // 비디오 요소 가져오기
      const videoElement = this.$refs.webcam;
      // 현재 시간
      let nowInMs = Date.now();
      // 비디오 현재 시간과 마지막 시간과 다르면
      if (videoElement.currentTime !== this.lastVideoTime) {
        // 마지막 시간 현재 시간으로 업데이트
        this.lastVideoTime = videoElement.currentTime;
        this.results = this.gestureRecognizer.recognizeForVideo(videoElement, nowInMs);
      }
      // 손제스쳐 결과 처리
      this.results.gestures.forEach((gesture, index) => {
          const categoryName = gesture[0].categoryName
          const currentTime = Date.now()
          const timeDiff = currentTime - this.lastGestureTime
          if (timeDiff < 100) {
            this.gestureSequence.push(categoryName)
          } else {
            this.gestureSequence = [categoryName];
          }
          this.lastGetureTime = currentTime;
        })
      // 유효한 상태 변화 확인
      if (this.isValidGestureChange()) {
        if (this.checkBreakJewel()) {
          console.log('광물 부수기 성공')
          this.webcamRunning = false
          this.clearJewelCanvas()
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
        this.gestureOutput = false;
      }
      // 손모양 계속 인식을 위해 계속 호출
      if (this.webcamRunning) {
        window.requestAnimationFrame(this.predictWebcam);
      }
    },

    // 파리와 손의 위치가 가까운지 판단
    checkFlyCaught() {
        const vedioElement = this.$refs.webcam;
        const leftHandX = this.leftXAxis * vedioElement.width
        const leftHandY = this.leftYAxis * vedioElement.height
        const rightHandX = this.rightXAxis * vedioElement.width
        const rightHandY = this.rightYAxis * vedioElement.height
        const flyX = this.flyPosition.x
        const flyY = this.flyPosition.y
        const leftDistance = Math.sqrt(Math.pow(leftHandX - flyX, 2) + Math.pow(leftHandY - flyY, 2))
        const rightDistance = Math.sqrt(Math.pow(rightHandX - flyX, 2) + Math.pow(rightHandY - flyY, 2))
        if (leftDistance < 50 || rightDistance < 50) {
          console.log('가까워요')
          return true
      }
    },


    // 움켜잡는 상태 관리
    isValidGestureChange() {
      const validSequence = ['Open_Palm', 'None', 'Closed_Fist']
      return validSequence.every((el) => this.gestureSequence.includes(el))
    },


    // 파리 캔버스 클리어
    clearFlyCanvas() {
      const flyCanvasElement = this.$refs.fly_canvas;
      if (flyCanvasElement) {
        const flyCanvasCtx = flyCanvasElement.getContext("2d");
        flyCanvasCtx.clearRect(0, 0, flyCanvasElement.width, flyCanvasElement.height)
      }
    },

  }
};
</script>

<style scoped>
.videoView {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding-top: 30px;
  padding-bottom: 30px;
  background-color: blueviolet;
  border-radius: 20px;
  width: 50%;
  height: 70%;
  margin: 0 auto;
  margin-top: 50px;
}

.media-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* 거울모드 */
.media {
  transform: rotateY(180deg);
  -webkit-transform:rotateY(180deg); 
}

#webcam {
  width: 70%;
  height: auto;
}

.start-timer, .victory {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: greenyellow;
  font-size: 5rem;
}

/* 캔버스 스타일링 */
canvas.media {
  position: absolute;
  left: 0;
  top: 0;
  z-index: 1; /* 필요한 경우 z-index 설정 */
}
</style>