let Init = new Array();
let accessToken;

Init.login = (userId, password) => {
  // 요청할 서버 URL
  const loginUrl = "http://localhost:8080/auth/login";

  // 요청 데이터 생성 (JSON 형식)
  const requestData = {
    userId: userId,
    password: password,
  };

  // HTTP POST 요청 옵션 설정
  const requestOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(requestData),
  };

  // 서버로 POST 요청 보내기
  fetch(loginUrl, requestOptions)
    .then((response) => {
      if (!response.ok) {
        throw new Error("HTTP Error " + response.status);
      }
      return response.text();
    })
    .then((data) => {
      // 서버로부터 받은 데이터 처리
      accessToken = data; // 서버 응답에서 accessToken 추출
      // console.log(accessToken);
    })
    .catch((error) => {
      // 에러 처리
      console.error("오류:", error);
    });
};

// 모달 열기
function openModal(value) {
  var modal;

  if (value === "login") {
    modal = document.getElementById("loginModal");
  } else {
    modal = document.getElementById("joinModal");
  }

  modal.style.display = "block";
  setTimeout(function () {
    modal.classList.add("show"); // 열 때 애니메이션 클래스 추가
  }, 100); // 애니메이션 지속 시간 (1초) 후에 display 속성 변경
}

// 모달 닫기
function closeModal(value) {
  var modal;

  if (value === "login") {
    modal = document.getElementById("loginModal");
  } else {
    modal = document.getElementById("joinModal");
  }

  modal.classList.remove("show"); // 애니메이션 클래스 제거
  // 모달이 완전히 사라질 때까지 기다린 후 display 속성 설정
  setTimeout(function () {
    modal.style.display = "none";
  }, 500); // 애니메이션 지속 시간 (0.5초) 후에 display 속성 변경
}

// 년도, 월, 일 옵션을 동적으로 생성하는 함수
function populateDateOptions() {
  const birthdateYear = document.getElementById("birthdateYear");
  const birthdateMonth = document.getElementById("birthdateMonth");
  const birthdateDay = document.getElementById("birthdateDay");

  // 년도 옵션 추가 (예: 1980부터 2024까지)
  for (let year = 1980; year <= 2024; year++) {
    const option = document.createElement("option");
    option.value = year;
    option.textContent = year;
    birthdateYear.appendChild(option);
  }

  // 월 옵션 추가 (1월부터 12월까지)
  for (let month = 1; month <= 12; month++) {
    const option = document.createElement("option");
    option.value = month;
    option.textContent = month;
    birthdateMonth.appendChild(option);
  }

  // 일 옵션 추가 (1일부터 31일까지)
  for (let day = 1; day <= 31; day++) {
    const option = document.createElement("option");
    option.value = day;
    option.textContent = day;
    birthdateDay.appendChild(option);
  }
}

function signup() {
  // 가입 로직을 처리하는 코드를 여기에 추가
  // 생년월일, 성별 등의 입력값을 가져와서 처리합니다.
}

// 페이지 로드 시 년도, 월, 일 옵션 생성
populateDateOptions();
