<template>
  <div ref="threeJsContainer" style="width: 300px; height: 400px"></div>
  <button @click="throwYut">윷 던지기</button>
</template>
    
    <script>
import * as THREE from "three";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader.js";

export default {
  mounted() {
    this.initThreeJS();
  },
  data() {
    return {
      yut: [],                // 윷 배열
      velocityY: 0.07,        // 초기 속도 => 얼마나 높게 올라가는지 결정
      accelerationY: -0.001,  // 중력 => 윷에 작용하는 중력.
      animationFrameId: null,
      modelLoaded: false,     // 모델 로드 상태를 확인하는 변수
    };
  },
  methods: {
    // 초기 설정.
    initThreeJS() {
      this.scene = new THREE.Scene();

      // 카메라 위치와 회전 설정
      this.camera = new THREE.PerspectiveCamera(75, 400 / 400, 0.1, 1000);
      this.camera.position.set(0, 0, 3); // 카메라를 살짝 위와 앞쪽으로 이동
      this.camera.lookAt(this.scene.position); // 카메라가 씬의 중심을 바라보도록 설정

      this.renderer = new THREE.WebGLRenderer({ alpha: true });
      this.renderer.setSize(400, 400);
      this.$refs.threeJsContainer.appendChild(this.renderer.domElement);

      // 윷 만들기 호출.
      this.loadYuts();

      // 조명.
      const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
      this.scene.add(ambientLight);

      const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
      directionalLight.position.set(0, 1, 1);
      this.scene.add(directionalLight);

      // 초기 렌더링
      this.renderer.render(this.scene, this.camera);
    },
    // 4개의 윷을 만든다.
    loadYuts(){
      const loader = new GLTFLoader();
      // 하나의 백을 위해 따로 만들어준다.
      loader.load('/yut/yut_textured_back.glb', gltf => {
          const yut = gltf.scene;
          this.scene.add(yut);
          this.yuts.push(yut);
        }, undefined, error => {
          console.error(error);
        });

      // 나머지는 일반 윷으로 만든다.
      for (let i = 0; i < 3; i++) {
        loader.load('/yut/yut_textured_normal.glb', gltf => {
          const yut = gltf.scene;
          this.scene.add(yut);
          this.yuts.push(yut);
          if (this.yuts.length === 4) { // 모든 윷 모델이 로드되었는지 확인
            this.modelLoaded = true;
          }
        }, undefined, error => {
          console.error(error);
        });
      }
    },
    // 윷던지기
    throwYut(){
    }
    ,

    // 윷 던지기
  //   throwYut() {
  //     if (!this.modelLoaded) return;
  //     // 윷 던지기 시작 위치와 속도 초기화
  //     this.yut.position.x = Math.random() * 1;
  //     this.yut.rotation.x = 0;
  //     this.yut.position.y = 0;
  //     this.yut.rotation.z = Math.random() * 3; // 회전 방향 랜덤.
  //     this.velocityY = 0.07; // 위로 던지는 초기 속도를 더 낮게 설정
  //     this.rotationSpeedX = 0.07; // y축 회전 속도
  //     this.positionSpeedX = 0.02; // y축 회전 속도

  //     const animate = () => {
  //       this.animationFrameId = requestAnimationFrame(animate);

  //       if (this.yut.position.y < 0) {
  //         this.yut.position.x += this.positionSpeedX;
  //         this.yut.rotation.y += 0.07;

  //         this.positionSpeedX -= 0.0002;

  //         if (this.positionSpeedX <= 0)
  //           cancelAnimationFrame(this.animationFrameId);
  //       } else {
  //         // 속도를 위치에 적용s
  //         this.yut.position.y += this.velocityY;
  //         // 가속도를 속도에 적용
  //         this.velocityY += this.accelerationY;
  //         // 회전
  //         this.yut.rotation.x += this.rotationSpeedX;
  //       }

  //       this.renderer.render(this.scene, this.camera);
  //     };
  //     animate();
  //   },
  },
};
</script>