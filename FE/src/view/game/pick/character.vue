<template>
  <div class="background">
    <div class="content">
      <p class="web-rtc">web RTC 자리</p>
      <div class="characters-wrapper">
        <div class="character-container">
          <div v-for="user in users" :key="user" @click="toggleCharacterSelection(user)">
            <div class="character-item" v-if="userHasPermission('user')">
              <img src="@/assets/img/sample.png" alt="sample-img" class="sample-img">
              <p class="select">{{ isCharacterSelected(user) ? '선택완료' : '선택중...' }}</p>
            </div>
          </div>
        </div>
        <div class="character-box">
          <div v-for="character in characters" :key="character" @click="selectCharacter(character)">
            <div class="character">
              <p class="character-detail" :class="{ selected: isCharacterSelected('현재 사용자', character) }" :style="{ border: `2px solid ${borderColor}` }" @click="changeBorderColor">{{ character }}</p>
            </div>
          </div>
        </div>
        <button @type="submit" @click="openModal('spy')" class="ready">준비완료</button>
        <spyModal v-if="showSpyModal" @close="showSpyModal = false" class="spy-modal"/>
        </div>
    </div>
  </div>
</template>

<script>
import spyModal from '@/view/game/pick/spyModal.vue';
import { useUserStore } from '@/store/userStore';
import { storeToRefs } from 'pinia';

export default {
  components: {
    spyModal,
  },
  setup() {
    const store = useUserStore();
    const { showSpyModal } = storeToRefs(store);

    const users = ["준희", "지훈", "성규", "수빈", "희웅"];
    const characters = ["캐릭터1", "캐릭터2", "캐릭터3", "캐릭터4", "캐릭터5", "캐릭터6", "캐릭터7", "캐릭터8", "캐릭터9", "캐릭터10", "캐릭터11", "캐릭터12", '캐릭터13', '캐릭터14'];
    const selectedCharacters = [];

    return {
      showSpyModal,
      openModal: store.openModal,
      users, 
      characters,
      selectedCharacters,
      borderColor: 'initialBorderColor'
    };
  },

  methods: {
    userHasPermission(user) {
      // 사용자의 권한을 확인하는 로직 추가해야함
      return true;
    },

    // 캐릭터를 선택했는지 확인하는 로직
    isCharacterSelected(user) {
      return this.selectedCharacters.some(
        entry => entry.user === user && entry.character !== null
      );
    },

    // 캐릭터를 선택 후 다시 해제 할 수 있는 로직
    toggleCharacterSelection(user, character) {
      const existingIndex = this.selectedCharacters.findIndex(
        entry => entry.user === user
      );

      if (existingIndex !== -1) {
        // 이미 선택한 캐릭터면 선택 해제
        this.selectedCharacters.splice(existingIndex, 1);
      } else {
        // 아직 캐릭터 선택 안했으면 선택
        const userHasSelectedCharacter = this.selectedCharacters.some(
          entry => entry.user === user && entry.character !== null
        );

        if (!userHasSelectedCharacter) {
          this.selectedCharacters.push({ user, character: null });
        }
      }
    },

    // 캐릭터 선택하는 로직 >> 캐릭터 하나만 선택할 수 있도록 하면 됨 !
    selectCharacter(character) {
      const user = "현재 사용자";     // 사용자 정보 가져오는 곳
      const existingIndex = this.selectedCharacters.findIndex(
        entry => entry.user === user
      );

      // 이미 선택한 캐릭터면 선택 해제
      if (existingIndex !== -1) {
        this.selectedCharacters[existingIndex].character = character;
      } else {
        // 아직 캐릭터 선택 안했으면 선택
        const userHasSelectedCharacter = this.selectedCharacters.some(
          entry => entry.user === user && entry.character !== null
        );

        if (!userHasSelectedCharacter) {
          console.log(`User ${user} selected character ${character}`);
          this.selectedCharacters.push({ user, character })
        }
      }
    },
    changeBorderColor() {
      this.borderColor = 'red';
    },
  },
};
</script>


<style scoped>
.background {        
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(44, 43, 43, 0.8);
    margin-top: 20px;
    width: 90%;
    max-width: 1200px;
    height: 700px;
    border-radius: 20px;
    margin-left: 11%;
}

.content {
    text-align: center;
}

.select-character-page {
    color: white;
}

.web-rtc {
    border: 2px solid white;
    width: 600px;
    height: 150px;
    color: white;
    margin: 0 auto;
}

.character-container {
  display: flex;
  justify-content: center;
  flex-direction: row;
}

.character-item {
  text-align: center;
  margin: 15px;
}

.sample-img {
    width: 60px;
    height: 60px;
    border: 2px solid white;
    border-radius: 50px;
}

.select {
    color: white;
    font-size: 12px;
}

.character-box {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(13%, 1fr));
  gap: 9px;
  justify-content: center;
  margin: 20px 0px;
  background-color: rgba(158, 152, 152, 0.8);
  width: 90%;
  max-width: 700px;
  padding: 10px;
  border-radius: 20px;
}

.character {
  width: 70px;
  height: 70px; 
  border: 2px solid white;
  margin: 0 10px; 
  font-size: 10px;
  margin-bottom: 10px;
  margin-top: 20px;
  background-color: rgba(88, 86, 86, 0.8);
  border-radius: 10px;
}
 .ready {
    border: 2px solid white;
    border-radius: 20px;
    margin-top: 10px;
 }

 .spy-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
 }
</style>