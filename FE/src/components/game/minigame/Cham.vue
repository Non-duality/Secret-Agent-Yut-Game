<template>
  <div id="liveView" class="videoView">
    <div class="media-container" id="media-container" style="position: relative;">
      <video 
        class="media" 
        ref="webcam" 
        id="webcam"
        autoplay 
        playsinline 
        @loadedmetadata="onVideoLoaded"
      ></video>
      <canvas
        ref="pointerCanvas"
        class="media"
        style="position: absolute; left: auto; top: 0px;"
      ></canvas>
    </div>
    <!-- 시작 전 타이머 -->
    <div v-if="showCountdown" class="countdown">{{ countdown }}</div>
    <!-- 게임 승리 -->
    <div v-if="showResult">
      <p class="game-result victory" v-if="gameResult">승리</p>
      <p class="game-result fail" v-else>패배</p>
    </div>
  </div>
</template>

<script>
import { FaceDetector, FilesetResolver } from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.0";
import pointerImage from '@/assets/img/mission/pointer.png';
export default {
  data() {
    return {
      children: [],
      faceDetector: null,
      showCountdown: false,
      countdown: 3,

      // 손 모양
      pointerImg: new Image(),
      pointerPosition: { x: null, y: null, angle: null },
      isPointerMove: false,

      // 게임 결과
      showResult: false,
      gameResult: false


    };
  },

  methods: {
    // 사용자 카메라 준비
    hasGetUserMedia() {
      return !!navigator.mediaDevices?.getUserMedia;
    },


    // 캔버스 크기 비디오 크기에 맞게 조정
    adjustCanvasSizeToVideo() {
      const webcam = this.$refs.webcam;
      const pointerCanvas = this.$refs.pointerCanvas;
      if (webcam && pointerCanvas) {
        pointerCanvas.width = webcam.offsetWidth
        pointerCanvas.height = webcam.offsetHeight
      }
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
      // 로드되면 캔버스 사이즈 조절
      const webcam = this.$refs.webcam
      webcam.addEventListener('loadedmetadata', () => {
        this.adjustCanvasSizeToVideo()
      })
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
      } catch (err) {
        console.error(err);
      }
    },

    // 비디오 로딩 완료 핸들러
    onVideoLoaded() {
      // 비디오 메타데이터 로딩 시점에서 카운트다운 시작
      this.showCountdown = true;
      this.startCountdown();
      this.initializePointer()
    },


    // 카운트다운 시작
    startCountdown() {
      const countdownInterval = setInterval(() => {
        this.countdown -= 1;
        if (this.countdown === 0) {
          clearInterval(countdownInterval);
          // 3초 카운트다운 후에 얼굴 인식 시작
          this.showCountdown = false
          this.predictWebcam();
        }
      }, 1000);
    },


    // 포인터 초기화
    initializePointer() {
      console.log("포인터 초기화")
      this.pointerImg.src = pointerImage
      const webcam = this.$refs.webcam
      const webcamWidth = webcam.offsetWidth
      const webcamHeight = webcam.offsetHeight

      // 포인터의 초기 위치 설정 (캔버스 중앙 하단)
      this.pointerPosition.x = webcamWidth / 2;
      this.pointerPosition.y = webcamHeight - 100;
      this.pointerPosition.angle = 0;

      this.updatePointer();
    },


    // 포인터 업데이터
    updatePointer() {
      const pointerCanvas = this.$refs.pointerCanvas
      const ctx = pointerCanvas.getContext('2d')
      const imgWidth = 100
      const imgHeight = 100

      ctx.clearRect(0, 0, pointerCanvas.width, pointerCanvas.height)
      ctx.save()

      ctx.translate(this.pointerPosition.x , this.pointerPosition.y);
      ctx.rotate(this.pointerPosition.angle)
      ctx.drawImage(this.pointerImg, -imgWidth / 2, -imgHeight / 2, imgWidth, imgHeight); // 이미지 그리기

      ctx.restore()

      if (!this.isPointerMove) {
        requestAnimationFrame(this.updatePointer);
      }
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
          this.checkFaceDirection(detections)
        } catch (error) {
          console.error("얼굴 인식 중 오류 발생:", error);
        }
      } else {
        console.log("유효하지 않은 비디오 프레임");
      }
    },


    // 화면상 얼굴점 그리기
    displayVideoDetections(detections) {
      const webcam = document.getElementById("webcam");
      this.children.forEach(child => {
        child.remove();
      });
      this.children = [];
      const mediaContainer = document.getElementById("media-container")
      // console.log(detections)
      detections.forEach(detection => {
        const p = document.createElement("p");
        p.innerText = `Confidence: ${Math.round(parseFloat(detection.categories[0].score) * 100)}%`;
        p.style.left = `${mediaContainer.offsetWidth - detection.boundingBox.width - detection.boundingBox.originX}px`;
        p.style.top = `${detection.boundingBox.originY - 150}px`;
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
        highlighter.style.left = `${mediaContainer.offsetWidth - detection.boundingBox.width - detection.boundingBox.originX}px`;
        highlighter.style.top = `${detection.boundingBox.originY - 100}px`;
        highlighter.style.width = `${detection.boundingBox.width - 10}px`;
        highlighter.style.height = `${detection.boundingBox.height}px`;
        highlighter.style.position = 'absolute';
        highlighter.style.border = '1px dashed #fff';
        highlighter.style.zIndex = '1';
        highlighter.style.backgroundColor = 'rgba(0, 255, 0, 0.25)';

        mediaContainer.appendChild(highlighter);
        mediaContainer.appendChild(p);
        this.children.push(highlighter, p);

        detection.keypoints.forEach((keypoint, index) => {
          const keyPointEl = document.createElement("span");
          keyPointEl.className = "key-point";
          keyPointEl.style.top = `${keypoint.y * mediaContainer.offsetHeight - 3}px`;
          keyPointEl.style.left = `${mediaContainer.offsetWidth - keypoint.x * mediaContainer.offsetWidth - 3}px`;
          keyPointEl.style.position = 'absolute';
          keyPointEl.style.width = '6px';
          keyPointEl.style.height = '6px';
          keyPointEl.style.border = '1px solid #ffffff';
          if (index == 0) {
            keyPointEl.style.backgroundColor = 'red';
          } else if (index == 1) {
            keyPointEl.style.backgroundColor = 'orange';
          } else if (index == 2) {
            keyPointEl.style.backgroundColor = 'yellow';
          } else if (index == 3) {
            keyPointEl.style.backgroundColor = 'green';
          } else if (index == 4) {
            keyPointEl.style.backgroundColor = 'blue';
          } else if (index == 5) {
            keyPointEl.style.backgroundColor = 'purple';
          } 
          keyPointEl.style.borderRadius = '50%';
          keyPointEl.style.display = 'block';

          mediaContainer.appendChild(keyPointEl);
          this.children.push(keyPointEl);

        });
      });
    },


    // 사용자의 방향 확인
    async checkFaceDirection(detections) {
      // 기계 방향 (0.5 보다 크면 오른쪽, 작으면 왼쪽)
      const machineDirection = Math.random() > 0.5;
      const machineRight = machineDirection;
      const machineLeft = !machineDirection;
      // 사람 방향
      const keypoints = detections[0].keypoints
      const rightEyeX = keypoints[0].x
      const leftEyeX = keypoints[1].x
      const noseX = keypoints[2].x
      const mouseX = keypoints[3].x
      const rightEarX = keypoints[4].x
      const leftEarX = keypoints[5].x

      // 왼쪽 귀와 코 사이
      const leftDistance = Math.abs(leftEarX - noseX)
      // 오른쪽 귀와 코 사이
      const rightDistance = Math.abs(rightEarX - noseX)

      // 고개에 따라 평가
      const userDirection = rightDistance < leftDistance ? 'right' : 'left';

      // 포인터 중단
      this.isPointerMove = true

      // 포인터 각도 조절
      if (machineRight) {
        this.pointerPosition.angle = - Math.PI / 4
      } else {
        this.pointerPosition.angle = Math.PI / 4
      }

      // 기계와의 게임 승리 여부 판단
      this.isPointerMove = true
      if ((machineRight && userDirection === 'left') || (machineLeft && userDirection === 'right')) {
        this.gameResult = true
      }
      this.showResult = true
    }
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
@import url('@/assets/css/game/minigame/cham.css');
</style>
