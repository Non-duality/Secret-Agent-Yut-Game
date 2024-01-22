var c = document.getElementById("canv"),
  d = c.getContext("2d");
var w = (c.width = window.innerWidth),
  h = (c.height = window.innerHeight);

// 마우스 위치를 저장할 변수
var mouseX = 0;
var mouseY = 0;

// 마우스 이벤트 리스너 등록
c.addEventListener("mousemove", function (e) {
  mouseX = e.clientX - c.getBoundingClientRect().left;
  mouseY = e.clientY - c.getBoundingClientRect().top;
});

// 배경 이미지를 로드합니다.
var backgroundImage = new Image();
backgroundImage.src = "./assets/gameMain/lobby.png"; // 배경 이미지 파일의 경로를 지정하세요.

// 이미지가 로드되면 배경을 그립니다.
backgroundImage.onload = function () {
  Snowy(); // 배경 이미지를 그린 후에 Snowy 함수를 호출합니다.
  d.drawImage(backgroundImage, 0, 0, w, h);
};

// Snowy();
function Snowy() {
  var snow,
    arr = [];
  var num = 300,
    tsc = 0.5,
    sp = 0.4; //num:눈송이 개수 , tsc:눈송이 가로 변화율, sp:내리는 속도
  var sc = 0.5,
    t = 10,
    mv = 20,
    min = 0.3; //sc:눈송이 크기
  for (var i = 0; i < num; ++i) {
    snow = new Flake();
    snow.y = Math.random() * (h + 50);
    snow.x = Math.random() * w;
    snow.t = Math.random() * (Math.PI * 2);
    snow.sz = (100 / (10 + Math.random() * 100)) * sc;
    snow.sp = Math.pow(snow.sz * 0.8, 2) * 0.15 * sp;
    snow.sp = snow.sp < min ? min : snow.sp;
    arr.push(snow);
  }
  go();
  function go() {
    window.requestAnimationFrame(go);
    d.clearRect(0, 0, w, h);
  
    for (var i = 0; i < arr.length; ++i) {
      f = arr[i];
      f.t += 0.05;
      f.t = f.t >= Math.PI * 2 ? 0 : f.t;
  
      // 이동 방향을 마우스 위치로 조절
      var angleToMouse = Math.atan2(mouseY - f.y, mouseX - f.x);
      f.x += Math.cos(angleToMouse) * (f.sz * 0.00005);
      f.y += f.sp; // 눈송이는 아래 방향으로 계속 이동
  
      if (f.y > h + 50) f.y = -10 - Math.random() * mv;
      if (f.x > w + mv) f.x = -mv;
      if (f.x < -mv) f.x = w + mv;
      f.draw();
    }
  }
  function Flake() {
    this.draw = function () {
      this.g = d.createRadialGradient(
        this.x,
        this.y,
        0,
        this.x,
        this.y,
        this.sz + 8
      );
      this.g.addColorStop(0, "hsla(255,255%,255%,0.7)");
      this.g.addColorStop(1, "hsla(255,255%,255%,0.7)");
      d.moveTo(this.x, this.y);
      d.fillStyle = this.g;
      d.beginPath();
      d.arc(this.x, this.y, this.sz, 0, Math.PI * 2, true);
      d.fill();
    };
  }
}

/*________________________________________*/
// window.addEventListener(
//   "resize",
//   function () {
//     c.width = w = window.innerWidth;
//     c.height = h = window.innerHeight;
//   },
//   false
// );
