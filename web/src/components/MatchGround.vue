<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4">
                <div class="user-tips" style="text-align: center;">
                    手动操作，或者选择你编译好的BOT
                </div>
                <div class="user-bot">
                    <select v-model="select_bot" class="form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                        <option value="-1">手动对战</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id"> {{ bot.botName }} </option>
                    </select>
                </div>
            </div>
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.battle.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.battle.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align: center;">
                    <button @click="click_match_btn"  type="button" class="btn btn-danger btn-lg">{{ match_btn_info }}</button>
            </div>
            <div class="tips" style="text-align: center;">
                请配置好Bot后进行匹配,或者在离线模式下用WASD和方向键控制移动
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
    setup() {
        const store = useStore();
        let match_btn_info = ref("开始匹配");
        let bots = ref([]); //从云端读取user的所有bot
        let select_bot = ref(-1); //最终选的bot，该变量用来给后端发送信息，获取该bot的指令

        const click_match_btn = () => {
            if (match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消匹配";
                store.state.battle.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value,
                }));
            }
            else
            {
                match_btn_info.value = "开始匹配";
                store.state.battle.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        }

        //从后端获取user对应的bot数据，注意：获取的变量的名称是后端声明的变量名，不是数据库中的名称
        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    console.log(resp);
                    bots.value = resp;
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }
        refresh_bots();

        return {
            match_btn_info,
            click_match_btn,
            bots,
            select_bot,
        }
    }

}
</script>

<style scoped>
div.matchground {
    width: 80vw;
    height: 85vh;
    background: rgba(50, 50, 50, 0.5);
    margin-top: 1%;
    margin-left: 10%;
}
div.user-photo {
    margin-top: 10%;
    text-align: center;
}
div.user-photo > img {
    border-radius: 50%;
    width: 230px;
    height: 230px;
}
div.user-username {
    text-align: center;
    font-size: 23px;
    padding-top: 3vh;
    color: whitesmoke;
}
div.tips{
    margin-top: 5vh;
    color: whitesmoke;
}
div.user-bot {
    padding-top: 3vh;
}
div.user-bot > select {
    width: 66%;
    margin: 0 auto;
}
div.user-tips {
    margin-top: 23vh;
    color: whitesmoke;
}

</style>
