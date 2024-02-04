<template>
  <div class="roulette-container">
    <div class="roulette-wheel" :style="wheelStyle">
      <div
        class="sector"
        v-for="(sector, index) in sectors"
        :key="index"
        :style="getSectorStyle(index)"
      >
        {{ sector.name }}
      </div>
    </div>
  </div>
  <button @click="spinRoulette">Spin</button>
</template>

<script>
export default {
  data() {
    return {
      rotation: 0,
      sectors: [
        { name: "항목 1" },
        { name: "항목 2" },
        { name: "항목 3" },
        // 추가 섹터...
      ],
    };
  },
  computed: {
    wheelStyle() {
      return {
        transform: `rotate(${this.rotation}deg)`,
        transition: "transform 4s cubic-bezier(0.33, 1, 0.68, 1)",
      };
    },
  },
  methods: {
    getSectorStyle(index) {
      const rotation = (360 / this.sectors.length) * index;
      return {
        transform: `rotate(${rotation}deg) translateX(-50%)`,
      };
    },
    spinRoulette() {
      const totalDegrees = 360 * 5 + Math.floor(Math.random() * 360);
      this.rotation += totalDegrees;

      // 회전 종료 후의 처리를 위한 타이머 설정
      setTimeout(() => {
        this.handleRotationEnd();
      }, 4000); // 4초 후에 회전이 종료됨을 가정
    },
    handleRotationEnd() {
      const finalAngle = this.rotation % 360; // 최종 각도
      const sectorAngle = 360 / this.sectors.length; // 각 섹터의 각도
      const selectedSectorIndex = Math.floor(finalAngle / sectorAngle);

      // 선택된 섹터의 이름을 가져옵니다.
      const selectedSectorName = this.sectors[selectedSectorIndex].name;

      // 선택된 섹터에 대한 처리를 여기에서 수행합니다.
      alert(`선택된 항목: ${selectedSectorName}`);
    },
  },
};
</script>

<style>
.roulette-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
.roulette-wheel {
  position: relative;
  width: 90%;
  height: 90%;
  border-radius: 50%;
  background-color: #fff;
}
.sector {
  position: absolute;
  width: 50%;
  height: 20%;
  left: 50%;
  top: 40%;
  text-align: center;
  transform-origin: 0% 50%;
}
</style>