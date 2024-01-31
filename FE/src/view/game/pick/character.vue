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
          <div v-for="character in characters" :key="character" @click="selectCharacter(user, character)">
            <div class="character">
              <p>{{ character }}</p>
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
    const selectedCharacters = {};

    return {
      showSpyModal,
      openModal: store.openModal,
      users, 
      characters,
      selectedCharacters,
    };
  },

  methods: {
    userHasPermission(user) {
      // 사용자의 권한을 확인하는 로직 추가해야함
      return true;
    },

    isCharacterSelected(user) {
      return this.selectedCharacters[user] !== undefined;
    },

    toggleCharacterSelection(user) {
      // 이미 선택된 캐릭터를 해제
      if (this.selectedCharacters[user] !== undefined) {
        delete this.selectedCharacters[user];
      } else {
        // 다른 사용자가 이미 선택한 캐릭터는 선택할 수 없게 하는 로직
        const selectedCharacter = Object.values(this.selectedCharacters).find(char => char !== undefined);
        if (selectedCharacter === undefined) {
          // 캐릭터 선택
          this.selectedCharacters[user] = user;
          console.log('Selected Characters:', this.selectedCharacters);
        }
      }
    },

    selectCharacter(user, selectedCharacter) {
      // 이미 선택된 캐릭터 해제
      if (this.selectedCharacters[user] !== undefined) {
        delete this.selectedCharacters[user];
      } else {
          // 다른 사용자가 이미 선택한 캐릭터는 선택할 수 없게 하는 로직
          const selectedCharacterValues = Object.values(this.selectedCharacters);
          if (!selectedCharacterValues.includes(selectedCharacter)) {
            // 캐릭터 선택
            this.selectedCharacters[user] = selectedCharacter;
            console.log('Selected Characters:', this.selectedCharacters);
          }
        }
      }
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
    width: 1200px;
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
  grid-template-columns: repeat(auto-fill, minmax(70px, 1fr));
  gap: 9px;
  justify-content: center;
  margin: 20px 0px;
  background-color: rgba(158, 152, 152, 0.8);
  width: 700px;
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