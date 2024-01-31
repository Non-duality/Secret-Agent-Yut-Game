import { defineStore } from "pinia";

export const useGameStore = defineStore("game", {
  // 반응형 상태 (data)
  state: () => {
    return {
      // 윷 던진 결과
      throwRes: [false, false, false, false],
      yutRes: 0,
      // 말
      redHorses: [
        { id: 1, index: 0, team: 1, status: "wait", img: "horse1", check: 0 },
        { id: 2, index: 0, team: 1, status: "wait", img: "horse2", check: 1 },
        { id: 3, index: 0, team: 1, status: "wait", img: "horse3", check: 2 },
        { id: 4, index: 0, team: 1, status: "wait", img: "horse4", check: 3 },
        { id: 5, index: 0, team: 1, status: "wait", img: "horse5", check: 4 },
      ],
      blueHorses: [
        { id: 1, index: 0, team: 2, status: "wait", img: "horse1", check: 0 },
        { id: 2, index: 0, team: 2, status: "wait", img: "horse2", check: 1 },
        { id: 3, index: 0, team: 2, status: "wait", img: "horse3", check: 2 },
        { id: 4, index: 0, team: 2, status: "wait", img: "horse4", check: 3 },
        { id: 5, index: 0, team: 2, status: "wait", img: "horse5", check: 4 },
      ],
      horsesIndex: [
        [
          { bottom: "160px", left: "70px" },
          { bottom: "160px", left: "140px" },
          { bottom: "90px", left: "70px" },
          { bottom: "90px", left: "140px" },
          { bottom: "30px", left: "105px" },
        ],
        [
          { bottom: "160px", right: "140px" },
          { bottom: "160px", right: "70px" },
          { bottom: "90px", right: "140px" },
          { bottom: "90px", right: "70px" },
          { bottom: "30px", right: "105px" },
        ],
      ],
      // 말이 이동할 위치
      tileHorse: [
        { bottom: "90px", right: "610px" }, // 0
        { bottom: "220px", right: "610px" }, // 1
        { bottom: "310px", right: "610px" }, // 2
        { bottom: "400px", right: "610px" }, // 3
        { bottom: "490px", right: "610px" }, // 4
        { bottom: "600px", right: "610px" }, // 5
        { bottom: "600px", right: "730px" }, // 6
        { bottom: "600px", right: "820px" }, // 7
        { bottom: "600px", right: "910px" }, // 8
        { bottom: "600px", right: "1000px" }, // 9
        { bottom: "600px", right: "1110px" }, // 10
        { bottom: "490px", right: "1110px" }, // 11
        { bottom: "400px", right: "1110px" }, // 12
        { bottom: "310px", right: "1110px" }, // 13
        { bottom: "220px", right: "1110px" }, // 14
        { bottom: "90px", right: "1110px" }, // 15
        { bottom: "90px", right: "1000px" }, // 16
        { bottom: "90px", right: "910px" }, // 17
        { bottom: "90px", right: "820px" }, // 18
        { bottom: "90px", right: "730px" }, // 19
        { bottom: "510px", right: "700px" }, // 20
        { bottom: "440px", right: "770px" }, // 21
        { bottom: "350px", right: "860px" }, // 22
        { bottom: "255px", right: "955px" }, // 23
        { bottom: "190px", right: "1020px" }, // 24
        { bottom: "510px", right: "1020px" }, // 25
        { bottom: "440px", right: "955px" }, // 26
        { bottom: "350px", right: "860px" }, // 27
        { bottom: "255px", right: "770px" }, // 28
        { bottom: "190px", right: "700px" }, // 29
      ],
      // 타일
      tiles: [
        { id: 0, position: "right" },
        { id: 1, position: "right" },
        { id: 2, position: "right" },
        { id: 3, position: "right" },
        { id: 4, position: "right" },
        { id: 5, position: "right" },
        { id: 6, position: "top" },
        { id: 7, position: "top" },
        { id: 8, position: "top" },
        { id: 9, position: "top" },
        { id: 10, position: "top" },
        { id: 11, position: "left" },
        { id: 12, position: "left" },
        { id: 13, position: "left" },
        { id: 14, position: "left" },
        { id: 15, position: "left" },
        { id: 16, position: "bottom" },
        { id: 17, position: "bottom" },
        { id: 18, position: "bottom" },
        { id: 19, position: "bottom" },
        { id: 20, position: "top-right" },
        { id: 21, position: "top-right" },
        { id: 22, position: "center" },
        { id: 23, position: "bottom-left" },
        { id: 24, position: "bottom-left" },
        { id: 25, position: "top-left" },
        { id: 26, position: "top-left" },
        { id: 27, position: "center" },
        { id: 28, position: "bottom-right" },
        { id: 29, position: "bottom-right" },
      ],
    };
  },

  // 메서드 (function)
  actions: {
    moveHorse() {
      const horseInRed = this.redHorses.find((horse) => horse.id === 1);

      horseInRed.index += 1;
      if (horseInRed.status === "wait") {
        for (var i = 1; i < 5; i++) {
          this.redHorses[i].check -= 1;
        }
        horseInRed.status = "ing";
      }
    },

    yutThrow() {
      // 총 4번의 랜덤을 발생
      // false가 뒤집어 진거
      let cnt = 0;
      for (var i = 0; i < 4; i++) {
        const rand = Math.floor(Math.random() * 10);

        // 윷은 4개이므로 각각 결과를 저장.
        if (rand < 4) {
          this.throwRes[i] = true;
          cnt++;
        } else {
          this.throwRes[i] = false;
        }
        console.log("rand = " + rand);
      }

      // 저장된 결과를 계산.
      switch (cnt) {
        case 0:
          this.yutRes = 4;
          break;
        case 1:
          if(this.throwRes[0])
            this.yutRes = -1;
          else
            this.yutRes = 1;
          break;
        case 2:
          this.yutRes = 2;
          break;
        case 3:
          this.yutRes = 3;
          break;
        case 4:
          this.yutRes = 5;
          break;
        default:
          // 기본 처리
          break;
      }
      console.log("res = " + this.yutRes);
    },
  },
});
