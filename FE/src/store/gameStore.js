import { faRProject } from "@fortawesome/free-brands-svg-icons";
import { defineStore } from "pinia";

export const useGameStore = defineStore("game", {
  // 반응형 상태 (data)
  state: () => {
    return {
      // 들어온 말의 개수.
      redEnd: 0,
      blueEnd: 0,
      isHorseEnd: false,
      // 윷 던진 결과
      throwRes: [false, false, false, false],
      yutText: "도",
      yutRes: 0,
      // 내 팀 정보.
      myTeam: 1,
      // 말 선택 flag.
      isSelect: false,
      // 대각선으로 갈지 말지 선택
      isGoDiagonal: false,
      // 가운데 방향 선택
      isCenterDir: false,
      // 말
      redHorses: [
        {
          id: 1,
          index: 0,
          team: 1,
          status: "wait",
          img: "horse1",
          check: 0,
          endOrder: 0,
        },
        {
          id: 2,
          index: 0,
          team: 1,
          status: "wait",
          img: "horse2",
          check: 1,
          endOrder: 0,
        },
        {
          id: 3,
          index: 0,
          team: 1,
          status: "wait",
          img: "horse3",
          check: 2,
          endOrder: 0,
        },
        {
          id: 4,
          index: 0,
          team: 1,
          status: "wait",
          img: "horse4",
          check: 3,
          endOrder: 0,
        },
        {
          id: 5,
          index: 0,
          team: 1,
          status: "wait",
          img: "horse5",
          check: 4,
          group: false,
        },
      ],
      blueHorses: [
        {
          id: 1,
          index: 0,
          team: 2,
          status: "wait",
          img: "horse1",
          check: 0,
          endOrder: 0,
        },
        {
          id: 2,
          index: 0,
          team: 2,
          status: "wait",
          img: "horse2",
          check: 1,
          endOrder: 0,
        },
        {
          id: 3,
          index: 0,
          team: 2,
          status: "wait",
          img: "horse3",
          check: 2,
          endOrder: 0,
        },
        {
          id: 4,
          index: 0,
          team: 2,
          status: "wait",
          img: "horse4",
          check: 3,
          endOrder: 0,
        },
        {
          id: 5,
          index: 0,
          team: 2,
          status: "wait",
          img: "horse5",
          check: 4,
          endOrder: 0,
        },
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
          { bottom: "160px", left: "1356px" },
          { bottom: "160px", left: "1426px" },
          { bottom: "90px", left: "1356px" },
          { bottom: "90px", left: "1426px" },
          { bottom: "30px", left: "1391px" },
        ],
        [
          { bottom: "10px", left: "380px" },
          { bottom: "10px", left: "430px" },
          { bottom: "10px", left: "480px" },
          { bottom: "10px", left: "530px" },
          { bottom: "10px", left: "580px" },
        ],
        [
          { bottom: "10px", rigth: "620px" },
          { bottom: "10px", rigth: "670px" },
          { bottom: "10px", rigth: "720px" },
          { bottom: "10px", rigth: "770px" },
          { bottom: "10px", rigth: "820px" },
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
        { bottom: "90px", right: "610px" }, // 30
      ],
      // 타일
      tiles: [
        { id: 0, position: "right", horse: [] },
        { id: 1, position: "right", horse: [] },
        { id: 2, position: "right", horse: [] },
        { id: 3, position: "right", horse: [] },
        { id: 4, position: "right", horse: [] },
        { id: 5, position: "right", horse: [] },
        { id: 6, position: "top", horse: [] },
        { id: 7, position: "top", horse: [] },
        { id: 8, position: "top", horse: [] },
        { id: 9, position: "top", horse: [] },
        { id: 10, position: "top", horse: [] },
        { id: 11, position: "left", horse: [] },
        { id: 12, position: "left", horse: [] },
        { id: 13, position: "left", horse: [] },
        { id: 14, position: "left", horse: [] },
        { id: 15, position: "left", horse: [] },
        { id: 16, position: "bottom", horse: [] },
        { id: 17, position: "bottom", horse: [] },
        { id: 18, position: "bottom", horse: [] },
        { id: 19, position: "bottom", horse: [] },
        { id: 20, position: "top-right", horse: [] },
        { id: 21, position: "top-right", horse: [] },
        { id: 22, position: "center", horse: [] },
        { id: 23, position: "bottom-left", horse: [] },
        { id: 24, position: "bottom-left", horse: [] },
        { id: 25, position: "top-left", horse: [] },
        { id: 26, position: "top-left", horse: [] },
        { id: 27, position: "center", horse: [] },
        { id: 28, position: "bottom-right", horse: [] },
        { id: 29, position: "bottom-right", horse: [] },
        { id: 30, position: "right", horse: [] },
      ],
    };
  },

  // 메서드 (function)
  actions: {
    // 말 이동
    moveHorse(selectedHorse) {
      const horseInfo =
        selectedHorse.team === 1
          ? this.redHorses.find((horse) => horse.id === selectedHorse.id)
          : this.blueHorses.find((horse) => horse.id === selectedHorse.id);

      // 목적지 설정.
      let target = horseInfo.index + this.yutRes;

      // 처음 출발할때는 상태를 바꿔야한다.
      if (horseInfo.status === "wait") {
        // 처음 출발한 말을 0번에 넣어 시작한다.
        this.tiles[0].horse.push(horseInfo);

        // 대기 중인 말이 빠지면 대기 말 재배열.
        for (var i = horseInfo.id; i < 5; i++) {
          if (selectedHorse.team === 1) this.redHorses[i].check -= 1;
          else this.blueHorses[i].check -= 1;
        }

        // 출발한 말의 상태를 바꾼다.
        horseInfo.status = "ing";
      }

      // 5, 10 모서리 출발
      if (this.isGoDiagonal) target += 14;

      // 가운데 방향 설정.
      if (horseInfo.index == 22 && !this.isCenterDir) target += 5;
      if (horseInfo.index == 27 && this.isCenterDir) target -= 5;

      if (
        horseInfo.index <= 24 &&
        target > 24 &&
        target === horseInfo.index + this.yutRes
      )
        target -= 10;
      if (horseInfo.index == 27 && this.isCenterDir && target > 24)
        target -= 10;

      // 말이 들어왔을 때
      // 마지막 위치에서 출발할 때는 그냥 끝.
      if (horseInfo.index === 30) {
        for (var i = 0; i < this.tiles[horseInfo.index].horse.length; i++) {
          const horseTemp =
            horseInfo.team === 1
              ? this.redHorses.find(
                  (horse) =>
                    horse.id === this.tiles[horseInfo.index].horse[i].id
                )
              : this.blueHorses.find(
                  (horse) =>
                    horse.id === this.tiles[horseInfo.index].horse[i].id
                );
          // 말 상태를 바꾼다.
          horseTemp.status = "end";
          // 카운트 한다.
          if (horseTemp.team === 1) horseTemp.endOrder = this.redEnd++;
          else horseTemp.endOrder = this.blueEnd++;
        }
        // 비워준다.
        this.tiles[horseInfo.index].horse = [];
        // 그냥 끝낸다.
        return;
      }
      // 15번 부터 마지막 타일 또는 들어왔다면
      if (
        (horseInfo.index >= 15 && target > 19 && horseInfo.index < 20) ||
        ((horseInfo.index === 22 || horseInfo.index >= 25) && target > 29)
      ) {
        // 마지막 타일
        if (target === 20 || target === 30) target = 30;
        // 그냥 들어와서 끝나면.
        else {
          this.isHorseEnd = true;
          // 카운트 한다.
          if (horseInfo.team === 1) horseInfo.endOrder = this.redEnd++;
          else horseInfo.endOrder = this.blueEnd++;
        }
      }

      // 만약 백도가 나왔을 때.
      if(this.yutRes === -1){
        if(horseInfo.index === 20 || horseInfo.index === 25){
          target -= 14;
        } else if(horseInfo.index == 1){
          target = 30;
        }
      }
      // 다른 말 체크
      if (!this.isHorseEnd) this.horseCheck(horseInfo, target);
      // 말 이동
      this.moveTo(horseInfo.index, target);
      this.isHorseEnd = false;
      this.isGoDiagonal = false;
      this.isCenterDir = false;
    },

    // 이동하는 곳에 다른 말이 있나 체크.
    horseCheck(horseInfo, target) {
      // console.log(horseInfo + " " + target);
      // 만약 팀이 다르다면.
      if (
        this.tiles[target].horse.length != 0 &&
        this.tiles[horseInfo.index].horse[0].team !=
          this.tiles[target].horse[0].team
      ) {
        for (var i = 0; i < this.tiles[target].horse.length; i++) {
          const horsedel =
            this.tiles[target].horse[i].team === 1
              ? this.redHorses.find(
                  (horse) => horse.id === this.tiles[target].horse[i].id
                )
              : this.blueHorses.find(
                  (horse) => horse.id === this.tiles[target].horse[i].id
                );

          horsedel.index = 0;
          horsedel.status = "wait";
          for (var j = horsedel.id; j < 5; j++) {
            if (horsedel.team === 1) this.redHorses[j].check += 1;
            else this.blueHorses[j].check += 1;
          }
        }
        this.tiles[target].horse = [];
      }

      this.tiles[target].horse.push(...this.tiles[horseInfo.index].horse);
      // 전에 있던 타일의 말 정보를 초기화한다.
      this.tiles[horseInfo.index].horse = [];
    },

    // 말 이동 함수
    moveTo(from, to) {
      // 이동 전 타일에서 말 배열의 크기를 가져온다.
      const len = !this.isHorseEnd
        ? this.tiles[to].horse.length
        : this.tiles[from].horse.length;
      // 팀 정보.
      const team = !this.isHorseEnd
        ? this.tiles[to].horse[0].team
        : this.tiles[from].horse[0].team;

      // 말이 들어왔다면.
      if (this.isHorseEnd) {
        for (var i = 0; i < len; i++) {
          const horseInfo =
            team === 1
              ? this.redHorses.find(
                  (horse) => horse.id === this.tiles[from].horse[i].id
                )
              : this.blueHorses.find(
                  (horse) => horse.id === this.tiles[from].horse[i].id
                );
          horseInfo.index = 30;
        }
        setTimeout(() => {
          for (var i = 0; i < len; i++) {
            const horseInfo =
              team === 1
                ? this.redHorses.find(
                    (horse) => horse.id === this.tiles[from].horse[i].id
                  )
                : this.blueHorses.find(
                    (horse) => horse.id === this.tiles[from].horse[i].id
                  );
            horseInfo.status = "end";
          }
          this.tiles[from].horse = [];
        }, 300);
      }
      // 모서리를 통과할때.
      else if (
        this.yutRes != -1 &&
        Math.trunc(from / 5) != Math.trunc(to / 5) &&
        from < 20
      ) {
        // 순간이동을 방지하기 위해 모서리를 찍고 목적지로 이동한다.
        for (var i = 0; i < len; i++) {
          const horseInfo =
            team === 1
              ? this.redHorses.find(
                  (horse) => horse.id === this.tiles[to].horse[i].id
                )
              : this.blueHorses.find(
                  (horse) => horse.id === this.tiles[to].horse[i].id
                );
          horseInfo.index = Math.trunc(to / 5) * 5;
          console.log(to / 5 + " " + Math.trunc(from / 5));
        }
        setTimeout(() => {
          for (var i = 0; i < len; i++) {
            const horseInfo =
              team === 1
                ? this.redHorses.find(
                    (horse) => horse.id === this.tiles[to].horse[i].id
                  )
                : this.blueHorses.find(
                    (horse) => horse.id === this.tiles[to].horse[i].id
                  );

            horseInfo.index = to;
          }
        }, 300);
      } else if (this.yutRes != -1 && from >= 20 && to < 20) {
        // 순간이동을 방지하기 위해 24,15번 찍고 목적지로 이동한다.
        for (var i = 0; i < len; i++) {
          const horseInfo =
            team === 1
              ? this.redHorses.find(
                  (horse) => horse.id === this.tiles[to].horse[i].id
                )
              : this.blueHorses.find(
                  (horse) => horse.id === this.tiles[to].horse[i].id
                );
          horseInfo.index = 15;
        }
        setTimeout(() => {
          for (var i = 0; i < len; i++) {
            const horseInfo =
              team === 1
                ? this.redHorses.find(
                    (horse) => horse.id === this.tiles[to].horse[i].id
                  )
                : this.blueHorses.find(
                    (horse) => horse.id === this.tiles[to].horse[i].id
                  );

            horseInfo.index = to;
          }
        }, 300);
      }

      // 평범한 이동
      else {
        // 그룹을 다 이동 시킨다.
        for (var i = 0; i < len; i++) {
          const horseInfo =
            team === 1
              ? this.redHorses.find(
                  (horse) => horse.id === this.tiles[to].horse[i].id
                )
              : this.blueHorses.find(
                  (horse) => horse.id === this.tiles[to].horse[i].id
                );
          horseInfo.index = to;
        }
      }
    },

    // 윷 던지기
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
        // console.log("rand = " + rand);
      }

      // 저장된 결과를 계산.
      switch (cnt) {
        case 0:
          this.yutRes = 4;
          this.yutText = "윷";
          break;
        case 1:
          this.yutText = "도";
          if (this.throwRes[0]) {
            this.yutText = "백도";
            this.yutRes = -1;
          } else this.yutRes = 1;
          break;
        case 2:
          this.yutText = "개";
          this.yutRes = 2;
          break;
        case 3:
          this.yutText = "걸";
          this.yutRes = 3;
          break;
        case 4:
          this.yutText = "모";
          this.yutRes = 5;
          break;
        default:
          break;
      }
      console.log("res = " + this.yutRes);
    },
  },
});
