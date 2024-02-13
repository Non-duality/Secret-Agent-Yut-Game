<template>
    <div class="spy-pick-container">
        <h2 class="spy-pick">밀정을 선택하세요.</h2>
        <div class="spy-container-wrapper">
            <div v-for="character in characters" :key="character.id" @click="openModal(character)">
                <p class="spy-container"></p>
                <img :src="character.image" class="character-list">
                <p class="character-name">{{ character.name }}</p>
            </div>
        </div>

        <div v-if="showSpyModal" class="modal">
            <div class="modal-content">
                <button @click="closeModal" class="select-ready">선택 완료</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            showSpyModal: false,
            isHost: true,           // 방장 여부
            selectedCharcter: null,
            characters: [
                { id: 1, name: '노름꾼', image: '@/assets/img/sample.png' },
                { id: 2, name: '기병', image: '@/assets/img/sample.png' },
                { id: 3, name: '창병', image: '@/assets/img/sample.png' },
                { id: 4, name: '노비', image: '@/assets/img/sample.png' },
                { id: 5, name: '기생', image: '@/assets/img/sample.png' },
            ],
        };
    },
    methods: {
        openModal(character) {
            // 방장인 경우에만 모달 열기
            if (this.isHost) {
                this.selectedCharcter = character;
                this.showSpyModal = true;
            }
            console.log(character)
        },
        closeModal() {
            if (this.isHost) {
                this.showSpyModal = false;
                // 게임방 router 생기면 연결해줘야함
                this.$router.push({ path: '/game', query: { character: this.selectedFCharacter.id } });
            }
        },
    },
};
</script>

<style scoped>
.spy-pick-container {
    position: relative;
    background-color: rgba(252, 243, 243, 1.0);
    border-radius: 20px;
    width: 750px;
    height: 300px;
    border: 5px solid black;
}

.spy-pick {
    color: black;
    margin: 10px;
    padding-top: 20px;
    padding-bottom: 20px;
}

.spy-container-wrapper {
    display: flex;
    justify-content: space-around;
}

.spy-container {
    text-align: center;
}

.character-list {
    width: 70px;
    height: 70px;
    margin: 0px 20px;
    border: 3px solid black;
    border-radius: 50px;
}

.character-name {
    color: black;
    font-weight: bold;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  border-radius: 10px;
  margin-top: 230px;
  margin-bottom: 10px;
}

.select-ready {
    border: 3px solid rgb(20, 20, 20);
    border-radius: 20px;
    font-weight: bold;
} 
</style>