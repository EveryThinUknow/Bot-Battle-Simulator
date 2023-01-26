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
    width: 60vw;
    height: 70vh;
    background: rgba(211,211,211,0.8);
    margin-top: 5%;
    margin-left: 20%;
}
</style>
