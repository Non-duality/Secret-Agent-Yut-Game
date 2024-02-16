# S10P12D205 배고프면 생각이 안나조 프로젝트

## 🔍프로젝트 소개

### 📌서비스 요약

- 기존 윷놀이 말에 밀정 시스템을 추가하여 색다르게 즐길 수 있음
- 맵의 특정 위치마다 미션을 설정하여 색다르게 즐길 수 있음

### 📌기획 의도

- 두 명 이상의 플레이어가 화상과 음성을 이용하여 상대방과 직접 소통하며 전략 시뮬레이션 게임인 윷놀이를 즐길 수 있다.
- 전통 놀이인 윷놀이에 전략적 요소를 가미하여 더욱 즐거운 게임이 가능하다.
- 경쟁과 협력이 공존하는 게임으로 서로 간의 유대감 증진이 가능하다.

### 📌기술 스택

<img src="https://img.shields.io/badge/typescript-3178C6?styleflat&logo=typescript&logoColor=white">
<img src="https://img.shields.io/badge/vite-646CFF?styleflat&logo=vite&logoColor=white">
<img src="https://img.shields.io/badge/react-61DAFB?styleflat&logo=react&logoColor=white">
<img src="https://img.shields.io/badge/tailwindcss-06B6D4?styleflat&logo=tailwindcss&logoColor=white">

<img src="https://img.shields.io/badge/springboot-6DB33F?styleflat&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?styleflat&logo=springsecurity&logoColor=white">

### 📌협업 및 배포 툴

<img src="https://img.shields.io/badge/figma-F24E1E?styleflat&logo=figma&logoColor=white">
<img src="https://img.shields.io/badge/gitlab-FC6D26?styleflat&logo=gitlab&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?styleflat&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/jenkins-D24939?styleflat&logo=jenkins&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?styleflat&logo=Notion&logoColor=white">
<img src="https://img.shields.io/badge/mattermost-0058CC?styleflat&logo=mattermost&logoColor=white">
<img src="https://img.shields.io/badge/jira-0052CC?styleflat&logo=jira&logoColor=white">

### 📌프로젝트 기간

- 2023.12.28 ~ 2024.02.16

## 🙍팀원 소개

- 김수빈 : 팀장, 프론트엔드,
- 양성규 : 프론트엔드,
- 이주미 : 프론트엔드, 
- 이희웅 : 프론트엔드, 
- 이준희 : 백엔드, 
- 박지훈 : 백엔드,

## 📑Convention

### 📌Git Commit Convention

#### 1. 커밋 유형

- 커밋 제목 첫 글자는 대문자로 작성하기

  | 커밋 유형 | 의미 |
  | --- | --- |
  | Feat | 새로운 기능 추가 |
  | Fix | 버그 수정 |
  | Docs | 문서 수정 |
  | Style | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
  | Refactor | 코드 리팩토링 |
  | Test | 테스트 코드, 리팩토링 테스트 코드 추가 |
  | Chore | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
  | Design | CSS 등 사용자 UI 디자인 변경 |
  | Comment | 필요한 주석 추가 및 변경 |
  | Rename | 파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우 |
  | Remove | 파일을 삭제하는 작업만 수행한 경우 |
  | !BREAKING CHANGE | 커다란 API 변경의 경우 |
  | !HOTFIX | 급하게 치명적인 버그를 고쳐야 하는 경우 |

#### 2. 제목

- `커밋 유형 태그 : 제목`의 형태이며 제목 첫 글자는 대문자로 작성
- 제목은 최대 50자 이내로 작성하고 마침표 및 특수기호는 사용 금지
- 커밋 유형 이후 제목과 본문은 한글로 작성하여 내용이 잘 전달될 수 있도록 할 것

#### 3. 본문

- 본문에는 변경한 내용과 이유 설명 (How 보다는 What & Why 위주 설명)
- 본문 내용은 양에 구애받지 않고 최대한 상세하게 작성

#### 4. 꼬리말

- 꼬리말은 옵션이므로 `이슈 트래커 ID`를 명시하고 싶을 때 작성
- 꼬리말은 `“유형: #이슈 번호”` 형식으로 사용
- 여러 개의 이슈 번호를 적을 때는 쉼표로 구분
- 이슈 트래커 유형은 다음 중 하나 사용
    - `Fixes`: 이슈 수정중 (아직 해결되지 않은 경우)
    - `Resolveds`: 이슈 해결했을 때 사용
    - `Ref`: 참고할 이슈가 있을 때 사용
    - `Related to`: 해당 커밋에 관련된 이슈 변호 (아직 해결되지 않은 경우)

### 📌Git Branch Convention

#### 규칙

- `master`에서 각자의 기능 브랜치를 분기
- 브랜치 이름 규칙
  - `작업종류/기능` 으로 브랜치 만들기
  - (지라 스토리 이슈 = 기능 명세서 소 분류 = 브랜치 1개)
  - (지라 작업 이슈 = 커밋 1개)
  - `design/FE-logout` `feat/BE-signup` `fix/FE-not-render-nav`
- 해당 기능의 브랜치에 작업이 완료 되면 해당 브랜치를 원격 저장소에 `push`하고 git Lab 페이지에서 `merge request(source : 본인 기능 브랜치, target : master)`(로컬에서 그냥 `merge` X)
  - `merge request` 오픈 이벤트 발생 시 EC2에서 빌드, 배포 실행 ⇒ MM으로 결과 알림
- `merge request` 위 결과에 따라 승인 여부 결정, 필요한 경우 코드 리뷰 및 토의
- `merge request`가 승인되면 `merge된 master` 브랜치에대하여 다시 EC2에서 빌드, 배포 실행 ⇒ MM으로 결과 알림
  - EC2에서 테스트 코드 실행 / 빌드(번들링 등의 파일 빌드) / 배포 진행 ⇒ 메신저로 결과 알림

#### 주의

- `master` 브랜치로 부터 개발하는 용도 외의 복사본 용을 git clone하여 `master` 브랜치로 부터 계속 pull 받으면서 복사본으로 사용\*\*
- `merge request` 승인 후 에러 생길 시 `git revert` 사용 권장\*\*
- **원격 저장소 `master`에 `merge` 이후 꼭 잊지말고 `master` 에서 `pull` 하기**

## 📌Code Convention

### 백, 프론트 공통 코드 컨벤션

- 메소드 파라미터는 최대 4개 이하

- 함수명, 변수명은 카멜케이스

- 주석은 설명하려는 구문 상단에 맞춰 쓰기
```java
function example() {
...
	// something에 관한 주석
	something ...
}
```

- 공백으로 구분 잘해주기

### 프론트엔드 코드 컨벤션
**[ 코드 ]**

- 문장마다 Enter하여 가독성 높이기

- 화살표 함수 사용

- 작은 따옴표 사용

- 세미콜론 사용

- 멀티 워드 컴포넌트 이름 사용하기
```java
<!-- 사전 컴파일된 템플릿에서 -->
<TodoItem />

<!-- in-DOM 템플릿에서 -->
<todo-item></todo-item>
```

- prop 정의는 상세하게, 최소한 타입을 명시하기
```java
// 잘못된 예
const props = defineProps(['status'])
```
```java
// 좋은 예
// 1. 첫번째 예시
const props = defineProps({
	status: String
})

// 2. 두번째 예시 (상세)
const props = defineProps({
	status: {
		type: String,
		required: true,

		validator: (value) => {
				return ['syncing', 'synced', 'version-conflict', 'error'].includes(
					value
				)
			}
		}
	})
```

**[ 파일명 ]**

- 기초 컴포넌트명에 이어쓰기
    
    ex) 
    
    Todo.vue
    
    TodoList.vue
    
    TodoDetailList.vue
    
- 파스칼 케이스

#### 백엔드 코드 컨벤션

- 모든 변수는 하나의 한개만 선언하기

  ```java
  int a, b; // X

  int a;
  int b; // O
  ```

- package 이름 : 소문자와 숫자만 사용 `ex) com.example.deepspace`

- Class 이름 : 첫 번째 문자를 대문자로 시작하며, 명사로 작성

  ```java
  class Test { }

  //단어가 2개 이상 혼합되어 있다면,
  //각 단어의 첫 번째 문자를 대문자로 표현
  class UserInfo { }
  ```

- Interface 이름 : 첫 번째 문자를 대문자 시작

  ```java
  interface Runnable { }
  interface ActionListener { }
  ```

- C style 배열선언 금지
  ```java
  String[] args // O
  String args[] // X
  ```
- 구현부가 없거나 한 줄의 구문을 포함해도 중괄호를 사용한다.

  ```java
  if(a > b) {
  a = b;
  }
  ```

- 상수는 모두 대문자로 작성하며, 단어 사이를 밑줄(\_)로 구분
- 클래스의 멤버와 initializer 의 순서는 따로 없지만, 최대한 논리적인 순서를 따름 새 메서드를 끝에 추가하는 것은 시간순이지 논리적인 순서가 아님
- 빈 블록은 줄바꿈하지 않고 {} 로 사용 multi-block(if/else, try/catch/finally) 은 줄바꿈
- 같은 동일한 이름의 메서드, 생성자는 연속적으로 작성
- @Override: 항상 사용

![system.drawio](/uploads/c5ff7077913bb566d2c39ca95509cfa9/system.drawio.png)

![게임입장gif](/uploads/05aa12ee41ac55d8459a19205f05232c/게임입장gif.gif)
