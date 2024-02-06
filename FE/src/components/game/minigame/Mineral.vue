<template>
  <!-- 라이브 웹캠 화면을 표시하는 컨테이너 -->
  <div id="liveView" class="videoView">
    <div class="media-container" style="position: relative;">
      <!-- 웹캠 비디오 요소 -->
      <video
          class="media"
          id="webcam"
          ref="webcam"
          autoplay
          playsinline
          :width="videoWidth"
          :height="videoHeight"
        ></video>
        <!-- 웹캠 비디오 위에 손동작 그릴 캔버스 -->
        <canvas
          class="media"
          id="motion_canvas"
          ref="motion_canvas"
          style="position: absolute; left: 0px; top: 0px;"
        ></canvas>
        <!-- 웹캠 비디오 위에 파리 그릴 캔버스 -->
        <canvas
          class="media"
          id="mineral_canvas"
          ref="mineral_canvas"
          style="position: absolute; left: 0px; top: 0px;"
        ></canvas>
        <!-- 제스처 결과를 표시할 요소 -->
        <div id="gesture_output" class="output" v-if="gestureOutput">
          <p>카테고리 - 왼손: {{ leftCategoryName }} / 오른손: {{ rightCategoryName }}</p>
          <p>손인식 정확도 - 왼손: {{ leftCategoryScore }} / 오른손: {{ rightCategoryScore }}</p>
          <p>x축 - 왼손: {{ leftXAxis }} / 오른손: {{ rightXAxis }}</p>
          <p>y축 - 왼손: {{ leftYAxis }} / 오른손: {{ rightYAxis }}</p>
        </div>
      </div>
    </div>
</template>

<script>
  // 모션 인식 라이브러리
  import { GestureRecognizer, FilesetResolver, DrawingUtils } from '@mediapipe/tasks-vision';
  // 파리 이미지
  import mineralImage from '@/assets/img/game/minigame/mineral.png';
  export default {
    data() {
      return {
        // 웹 캠 관련
        gestureRocognizer: GestureRecognizer,
        runningMode: "VEDIO", // 웹캠 실행 모드
        webcamButtonText: "웹 캠 활성화", // 웹캠 활성화 버튼 텍스트
        webcamRunning: false, // 웹캠 실행 여부
        lastVideoTime: -1, // 비디오의 마지막 시간
        constraints: {
          video: true
        },

        // 비디오 크기
        videoWidth: 480,
        videoHeight: 360,

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

        // 파리 생성 및 초기 위치, 방항, 속도 지정
        flyImg: new Image(),
        flyPosition: { x: Math.random(), y: Math.random() },
        flyDirection: { x: (Math.random() - 0.5) * 50, y: (Math.random() - 0.5) * 50 },
      }
    },
    
    mounted() {
      // 비디오의 loadedmetadata 이벤트 처리 등록
      const videoElement = this.$refs.webcam;
      videoElement.addEventListener('loadedmetadata', () => {
        // 비디오 크기에 맞게 캔버스 크기 설정
        this.videoWidth = videoElement.videoWidth;
        this.videoHeight = videoElement.videoHeight;
        this.createGestureRecognizer(); // 제스처 인식기 초기화
        this.adjustCanvasSize(); // 캔버스 크기 조정
      });
    },


    methods: {
      // 제스처 인식기 초기화 및 캔버스 크기 설정
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
        const videoElement = this.$refs.webcam;
        videoElement.addEventListener('loadedmetadata', () => {
          // 비디오 크기에 맞게 캔버스 크기 설정
          this.videoWidth = videoElement.videoWidth;
          this.videoHeight = videoElement.videoHeight;
          this.adjustCanvasSize();
        })
      },

      
      // 캔버스 요소들에 비디오 크기 적용
      adjustCanvasSize() {
        const outputCanvas = this.$refs.output_canvas;
        const flyCanvas = this.$refs.fly_canvas
        if (outputCanvas && flyCanvas) {
          outputCanvas.width = this.videoWidth
          outputCanvas.height = this.videoHeight
          flyCanvas.width = this.videoWidth
          flyCanvas.height = this.videoHeight
        }
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
          // 로드된 파리 캔버스 클리어
          // this.clearFlyCanvas();
        } else {
          // 웹 캠 활성화
          this.webcamRunning = true;
          this.gestureOutput = true
          this.webcamButtonText = "제스처 인식 끝내기";

          navigator.mediaDevices.getUserMedia(this.constraints).then((stream) => {
            const videoElement = this.$refs.webcam;
            videoElement.srcObject = stream;
            videoElement.addEventListener("loadeddata", this.predictWebcam);
          });
          // 파리 이미지 로드 및 애니메이션 시작 로직 유지
          this.loadAndAnimateFly();
        }
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
            const timeDiff = currentTime - this.lastGetureTime
            if (timeDiff < 500) {
              this.gestureSequence.push(categoryName)
            } else {
              this.gestureSequence = [categoryName];
            }
            this.lastGetureTime = currentTime;
          })
        // 유효한 상태 변화 확인
        if (this.isValidGestureChange()) {
          if (this.checkFlyCaught()) {
            console.log('파리 잡기 성공')
            this.webcamRunning = false
            this.clearFlyCanvas()
          }
        }
        // // 손 제스쳐 그릴 준비 (확인용)
        // const canvasElement = this.$refs.output_canvas;
        // const canvasCtx = canvasElement.getContext("2d");
        // // 현재 캔버스 상태를 저장
        // canvasCtx.save();
        // // 캔버스 내용 지우기
        // canvasCtx.clearRect(0, 0, canvasElement.width, canvasElement.height)
        // const drawingUtils = new DrawingUtils(canvasCtx);
        // // 제스처 랜드마크 그리기
        // if (this.results.landmarks) {
        //   for (const landmarks of this.results.landmarks) {
        //     drawingUtils.drawConnectors(
        //       landmarks,
        //       GestureRecognizer.HAND_CONNECTIONS,
        //       {
        //         color: "#00FF00",
        //         lineWidth: 5
        //       }
        //     );
        //     drawingUtils.drawLandmarks(landmarks, {
        //       color: "#FF0000",
        //       lineWidth: 2
        //     });
        //   }
        // }
        // // 2D 컨텍스트에 대한 상태를 이전 상태로 복원
        // canvasCtx.restore();
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
        } else {
          const outputCanvasElement = this.$refs.output_canvas;
          const outputCanvasCtx = outputCanvasElement.getContext("2d");
          outputCanvasCtx.clearRect(0, 0, outputCanvasElement.width, outputCanvasElement.height);
        }
      },


      // 파리 이미지 로드 및 애니메이션 시작
      loadAndAnimateFly() {
        this.flyImg.src = flyImage;
        this.flyImg.onload = () => {
          console.log('이미지가 성공적으로 로드되었습니다.');
          this.animateFly();
        };
        this.flyImg.onerror = () => {
          console.log('이미지 로드 중에 오류가 발생했습니다.')
        }
      },
      

      // 파리 이미지 그리기
      drawFly() {
        if (this.webcamRunning) {
          // 파리 그리기
          const canvasElement = this.$refs.fly_canvas;
          if(!canvasElement) {
            return console.log('파리 캔버스 오류')
          }
          const canvasCtx = canvasElement.getContext("2d");
          canvasCtx.clearRect(0, 0, canvasElement.width, canvasElement.height) // 캔버스 클리어
          // 파리의 이동 방향에 따라 이미지 반전
          if (this.flyDirection.x < 0) {
            // 왼쪽으로 이동 중이면 이미지 반전
            canvasCtx.save(); // 현재 캔버스 상태 저장
            canvasCtx.scale(-1, 1); // x축 방향으로 반전
            canvasCtx.drawImage(this.flyImg, -this.flyPosition.x - 50, this.flyPosition.y, 50, 50); // 이미지 그리기 (위치 조정 필요)
            canvasCtx.restore(); // 캔버스 상태 복원
          } else {
            // 오른쪽으로 이동 중이면 정상 방향
            canvasCtx.drawImage(this.flyImg, this.flyPosition.x, this.flyPosition.y, 50, 50); // 파리 그리기
          }
        }
      },
      

      // 파리 움직이는 로직
      animateFly() {
        if (this.webcamRunning) {
          const canvasElement = this.$refs.fly_canvas;
          const canvasWidth = canvasElement.width;
          const canvasHeight = canvasElement.height;
          // 파리의 새 위치 계산
          let newX = this.flyPosition.x + this.flyDirection.x;
          let newY = this.flyPosition.y + this.flyDirection.y;
          // 경계에서 튕기기 로직
          if (newX <= 0 || newX >= canvasWidth - 50) {
            this.flyDirection.x = -this.flyDirection.x;
            newX = this.flyPosition.x + this.flyDirection.x;
          }
          if (newY <= 0 || newY >= canvasHeight - 50) {
            this.flyDirection.y = -this.flyDirection.y;
            newY = this.flyPosition.y + this.flyDirection.y;
          } 
          // 파리 위치 업데이트
          this.flyPosition.x = newX;
          this.flyPosition.y = newY;
          this.drawFly(); // 업데이트 된 위치에 파리 그리기
          requestAnimationFrame(this.animateFly) // 다음 프레임을 위해 재귀 호출
        }
      },


      // 파리와 손의 위치가 가까운지 판단
      checkFlyCaught() {
          const outputCanvas = this.$refs.output_canvas;
          const leftHandX = this.leftXAxis * outputCanvas.width
          const leftHandY = this.leftYAxis * outputCanvas.height
          const rightHandX = this.rightXAxis * outputCanvas.width
          const rightHandY = this.rightYAxis * outputCanvas.height
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
    },
  }
</script>


<style scoped>
/* 화면 중앙 정렬 */
  /* .videoView {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding-top: 30px;
    padding-bottom: 30px;
    margin-left: 100px;
    margin-right: 100px;
    background-color: blueviolet;
  } */

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
    color: rgb(228, 245, 4);
    font-size: 5rem;
    border: 3px solid black;
    border-radius: 50px;
    background-color: black;
    width: 100px;
    height: 100px;
    text-align: center;
    padding-bottom: 12px;
  }
</style>