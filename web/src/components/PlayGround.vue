<template>
    <div class="playground">
        <GameMap></GameMap>
    </div>
</template>

<script>
import GameMap from './GameMap.vue'

export default {
    components: {
        GameMap
    }
}
</script>

<style scoped>
div.playground {
    width: 80vw;
    height: 85vh;
    background: rgba(211,211,211,0.8);
    margin-top: 1%;
    margin-left: 10%;
}
</style>
