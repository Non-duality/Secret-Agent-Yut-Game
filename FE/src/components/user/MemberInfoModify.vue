<template>

    <div>
        <h1>회원 정보 수정 페이지</h1>
        <img src="@/assets/img/profile_picture.png" alt="profile-img">

        <h3>{{ user.nickname }}</h3>
        <button @click="changeNickname('change')">닉네임 변경</button>

        <!-- 닉네임 변경 모달 -->
        <transition name="fade">
            <ChangeNicknameModal 
                v-if="showChangeNicknameModal"
            />
        </transition>

        <h4>이메일</h4><span>{{ user.email }}</span>
        <h4>비밀번호</h4><span>{{ user.password }}</span>
        <h4>생년월일</h4><span>{{ user.birthdate }}</span>
        <!-- 성별 체크박스로 넣어서 completed 정보로 표시하기 -->
        <div>
            <h4>성별</h4>
            <input type="checkbox" >
        </div>


        <button @click="withdraw">회원 탈퇴</button>
    </div>

</template>

<script>
import ChangeNicknameModal from '@/components/user/ChangeNicknameModal.vue';

    export default {
        components: {
            ChangeNicknameModal,
        },
        data() {
            return {
                user: {
                    nickname: '간지작살',
                    email: 'ssafy@ssafy.com',
                    password: '********',
                    birthdate: '1998. 02. 01.',
                    gender: '여성',
                    showChangeNicknameModal: false,
                },
            };
        },

        methods: {
            // 닉네임 변경 메서드 >> 누르면 닉네임 변경 모달창 뜸
            changeNickname(value) {
                if (value === "change") {
                    this.showChangeNicknameModal = true;
                }
            },

            // 회원 탈퇴 메서드
            withdraw() {

            },
        },
    };
</script>

<style scoped>
.fade-enter-from {
  /* 시작시 효과 */
  opacity: 0;
}

.fade-enter-active {
  /* 전체 단계에서 적용될 부분*/
  transition: all 0.5s;
}

.fade-enter-to {
  /* 끝나는 효과 */
  opacity: 1;
}

.fade-leave-from {
  opacity: 1;
}

.fade-leave-active {
  transition: all 0.5s;
}

.fade-leave-to {
  opacity: 0;
}
</style>