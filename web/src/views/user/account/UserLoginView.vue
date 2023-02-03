<template>
    <CommonContent v-if="is_show">
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
                    <div class="error-message">{{ receive_message }}</div>
                    <button type="submit" class="btn btn-primary">登录</button>
                </form>
            </div>
        </div>
    </CommonContent>
</template>

<script>
import CommonContent from '../../../components/CommonContent.vue'
import {useStore} from 'vuex'//全局变量
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

        //刷新会自动跳转登陆页面，如果是登录状态，不必展示登录页面的内容，因为会自动跳转到'home'
        let is_show = ref(false);

        //发现即使存储了cookies(token)，刷新依旧会退出登录状态，因此需要在每次跳转到登录界面时，判断是否存有cookies
        const cookies = localStorage.getItem("cookies");
        if(cookies)
        {
            store.commit("updateToken", cookies);
            store.dispatch("getinfo", {
                success() {
                    router.push({name: "home"});
                },
                error(resp) {
                    console.log(resp);
                    is_show.value = true; //如果数据出错，展示登陆界面
                }
            });
        }
        else
        {
            is_show = true;//如果未登录(没有cookies,即token)，展示登陆界面
        }

        const login = () => {
            receive_message.value = "";
            //存储全局变量数据
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: 'home' });
                            console.log(store.state.user);
                        }
                    })
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
            is_show,
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