<template>
    <CommonContent>
        <div class = "row justify-content-md-center">
            <div class = "col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="Input your username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="Input your password">
                    </div>
                    <div class="error-message">{{receive_message}}</div>
                    <button type="submit" class="btn btn-primary">登录</button>
                </form>
            </div>
        </div>
    </CommonContent>
</template>

<script>
import CommonContent from '../../../components/CommonContent.vue'
import {useStore} from 'vuex'
import {ref} from 'vue' //获取变量的字符串
import router from '../../../router/index'

export default{
    components: {
        CommonContent
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let receive_message = ref('');

        const login = () => {
            receive_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    router.push({name: 'home' });
                },
                error() {
                    receive_message.value = "用户名或密码错误"; //在页面显示错误
                }
            })
        }

        return {
            username,
            password,
            receive_message,
            login,
        }
    }
}

</script>

<style scoped>
button {
    width: 100%;
}
div.error-message {
    color:tomato;
}
</style>