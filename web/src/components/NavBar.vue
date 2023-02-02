<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
        <router-link class="navbar-brand" :to="{name: 'home'}">Bot Battle Simulator</router-link>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
        </button>   
        <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
                <router-link :class="route_name == 'battle_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'battle_index'}">对战页面</router-link>
            </li>
            <li class="nav-item">
                <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index'}">对战记录</router-link>
            </li>
            <li class="nav-item">
                <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'ranklist_index'}">Bot排行</router-link>
            </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.is_login">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                {{ $store.state.user.username }}
                </a>
                <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <li><a class="dropdown-item" href="/user/bot/">查看我的BOT</a></li>
                    <li><a class="dropdown-item" href="#">退出账号</a></li>
                </ul>
            </li>
        </ul>

        <ul class="navbar-nav" v-else>
            <li class="nav-item">
                <router-link class="nav-link" :to="{name: 'user_account_login'}" role="button">登录</router-link>
            </li>
            <li class="nav-item">
                <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">注册</router-link>
            </li>
        </ul>

        </div>
    </div>
</nav>
</template>

<script>
/**
route_name返回鼠标指针指向的<li>的name
*/
import { useRoute } from 'vue-router'
import { computed } from 'vue'

//import { useStore } from 'vuex';

export default {
    setup() {
        const route = useRoute();
        //const store = useStore();
        let route_name = computed(() => route.name)
        return {
            route_name
        }
    }
}

</script>

<style scoped>

</style>