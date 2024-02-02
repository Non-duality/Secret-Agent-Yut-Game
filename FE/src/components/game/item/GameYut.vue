<template>
  <div ref="threeJsContainer" style="width: 300px; height: 300px"></div>
  <button @click="throwYut">윷 던지기</button>
</template>
    
    <script>
import * as THREE from "three";

export default {
  mounted() {
    this.initThreeJS();
  },
  data() {
    return {
      velocityY: 0.01, // 초기 속도 => 얼마나 높게 올라가는지 결정
      accelerationY: -0.002, // 중력 => 윷에 작용하는 중력.
      animationFrameId: null,
    };
  },
  methods: {
    initThreeJS() {
      this.scene = new THREE.Scene();

      // 카메라 위치와 회전 설정
      this.camera = new THREE.PerspectiveCamera(75, 400 / 400, 0.1, 1000);
      this.camera.position.set(0, 2, 3); // 카메라를 살짝 위와 앞쪽으로 이동
      this.camera.lookAt(this.scene.position); // 카메라가 씬의 중심을 바라보도록 설정

      this.renderer = new THREE.WebGLRenderer({ alpha: true });
      this.renderer.setSize(400, 400);
      this.$refs.threeJsContainer.appendChild(this.renderer.domElement);

      // 윷 모델 설정
      const geometry = new THREE.BoxGeometry(1, 0.5, 0.3);
      const materials = [
      new THREE.MeshBasicMaterial({ color: 0xff0000 }), // 앞면: 빨강
        new THREE.MeshBasicMaterial({ color: 0x00ff00 }), // 뒷면: 초록
        new THREE.MeshBasicMaterial({ color: 0x0000ff }), // 상단: 파랑
        new THREE.MeshBasicMaterial({ color: 0xffff00 }), // 하단: 노랑
        new THREE.MeshBasicMaterial({ color: 0x00ffff }), // 오른쪽: 청록
        new THREE.MeshBasicMaterial({ color: 0xff00ff })  // 왼쪽: 자주
      ];
      this.yut = new THREE.Mesh(geometry, materials);
      this.scene.add(this.yut);

      // 초기 렌더링
      this.renderer.render(this.scene, this.camera);
    },
    throwYut() {
      // 윷 던지기 시작 위치와 속도 초기화
      this.yut.position.y = 0;
      this.velocityY = 0.07; // 위로 던지는 초기 속도를 더 낮게 설정
      this.rotationSpeedY = 0.1; // y축 회전 속도
      this.rotationSpeedX = 0.1; // y축 회전 속도

      const animate = () => {
        this.animationFrameId = requestAnimationFrame(animate);

        // 속도를 위치에 적용
        this.yut.position.y += this.velocityY;
        // 가속도를 속도에 적용
        this.velocityY += this.accelerationY;

        // 회전 속도 적용
        this.yut.rotation.y += this.rotationSpeedY;
        this.yut.rotation.x += this.rotationSpeedX;

        // 바닥에 닿으면 반사 효과 및 회전 속도 조절
        if (this.yut.position.y <= 0) {
            console.log(this.yut.rotation);
          this.yut.position.y = 0;
          this.velocityY = -this.velocityY * 0.5; // 반사 시 속도 감소
          this.rotationSpeedY *= 0.5; // 바닥에 닿을 때마다 회전 속도 감소
          this.rotationSpeedX *= 0.5; // 바닥에 닿을 때마다 회전 속도 감소
          this.rotationSpeedY = 0; // 회전 멈춤
          this.rotationSpeedX = 0; // 회전 멈춤
          cancelAnimationFrame(this.animationFrameId);
        }

        this.renderer.render(this.scene, this.camera);
      };
      animate();
    },
  },
  beforeDestroy() {
    if (this.animationFrameId) {
      cancelAnimationFrame(this.animationFrameId);
    }
  },
};
</script>