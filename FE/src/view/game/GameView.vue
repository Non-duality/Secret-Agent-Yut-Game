<template>
    <div class="game-main">
        <div class="game-left">
            <GameLeft/>
        </div>
        <div class="game-mid">
            <GameMid/>
        </div>
        <div class="game-right">
            <GameRight/>
        </div>
    </div>
</template>

<script>
import GameLeft from '@/components/game/GameLeft.vue';
import GameMid from '@/components/game/GameMid.vue';
import GameRight from '@/components/game/GameRight.vue';

export default {
    components:{
        GameLeft,
        GameMid,
        GameRight,
    }
}
</script>

<style>
@import "@/assets/css/game/gameView.css";
</style>