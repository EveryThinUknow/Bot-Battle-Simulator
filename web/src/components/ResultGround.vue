<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.battle.loser === 'all'"> 
            平局
        </div>
        <div class="result-board-text" v-else-if="$store.state.battle.loser === 'A' && $store.state.battle.a_id === parseInt($store.state.user.id)">
            惜败
        </div>
        <div class="result-board-text" v-else-if="$store.state.battle.loser === 'B' && $store.state.battle.b_id === parseInt($store.state.user.id)">
            惜败
        </div>
        <div class="result-board-text" v-else>
            获胜
        </div>
        <div class="result-board-btn">
            <button @click="rematching" type="button" class="btn btn-danger btn-lg"> 重新匹配 </button>
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        const rematching = () => {
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", "no result yet");
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://pic1.zhimg.com/v2-abed1a8c04700ba7d72b45195223e0ff_l.jpg?source=1940ef5c",
            })
        }

        return {
            rematching
        };
    }
}

</script>

<style scoped>
div.result-board {
    height: 200px;
    width: 400px;
    background-color: rgba(02, 13, 23, 0.8);;
    position: absolute;
    top: 30vh;
    left: 35vw;
}
div.result-board-text {
    color: whitesmoke;
    font-size: 45px;
    font-weight: 600;
    font-style: italic;
    padding-top: 3vh;
    text-align: center;
}
div.result-board-btn {
    padding-top: 7vh;
    text-align: center;
}
</style>