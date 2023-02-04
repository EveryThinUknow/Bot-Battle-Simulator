<template>
    <CommonContent>
        <div class = "row justify-content-md-center">
            <div class = "col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="Input your username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="Input your password">
                    </div>
                    <div class="mb-3">
                        <label for="confirm_password" class="form-label">确认密码</label>
                        <input v-model="confirm_password" type="password" class="form-control" id="confirm_password" placeholder="Input your password again">
                    </div>
                    <div class="error-message">{{ receive_message }}</div>
                    <button type="submit" class="btn btn-primary">注册</button>
                </form>
            </div>
        </div>
    </CommonContent>
</template>

<script>
import CommonContent from '../../../components/CommonContent.vue'
import { ref } from 'vue'
import router from '../../../router/index'
import $ from 'jquery'

export default{
    components: {
        CommonContent
    },
    
    setup() {
        let username = ref('');
        let password = ref('');
        let confirm_password = ref('');
        let receive_message = ref('');

        const register = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/register/",
                type: "post",
                data: {
                    //:左边是自定义vue本地变量名，右边是数据库中对应的名称
                    username: username.value,
                    password: password.value,
                    confirm_password: confirm_password.value,
                },
                success: function(resp) {
                    //如果注册成功，跳转到登录界面
                    if(resp.receive_message === "success")
                    {
                        router.push({ name: 'user_account_login' });
                    }
                    else
                    {
                        //后端编写了各种错误类型，会返回对应的注册错误提示(我不该用英文，后期把后端的receive_message改成中文信息)
                        receive_message.value = resp.receive_message;
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }
        return {
            username,
            password,
            confirm_password,
            receive_message,
            register,
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