<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
import { GameMap } from '@/assets/scripts/GameMap';
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';

export default {
    //生成画布
    setup() {
        let parent = ref(null);
        let canvas = ref(null);
        const store = useStore();

        onMounted(() => {
            store.commit(
                "updateGameObject",
                new GameMap(canvas.value.getContext('2d'), parent.value, store)
            );
        });

        return {
            parent,
            canvas
        }
    }
}
</script>

<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex; /*居中*/
    justify-content: center; /*垂直居中*/
    align-items: center; /*水平居中*/
}
</style>