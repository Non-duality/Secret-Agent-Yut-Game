// 이 코드는 페이지가 로드될 때 이미지를 표시한 다음 3초 후에 이미지를 숨깁니다.
// document.addEventListener("DOMContentLoaded", function () {
//   const imageContainer = document.querySelector(".image-container");

//   setTimeout(function () {
//     imageContainer.style.display = "none";
//   }, 3000);
// });

let Intro = new Array();

Intro.ServerConnecting = (accessToken) => {
  $("#intro-progress-text").text("서버 연결중");
  ServerLogin(accessToken);
};

Intro.init = () => {
  $("#intro-screen").show();
  $("#intro-progress-text").text("서버에 접속하려면 아무 키나 누르세요.");
  window.addEventListener("keydown", Intro.ServerConnecting, { once: true });
};
