<template>
    <PlayGround v-if="$store.state.battle.status === 'playing'">
        Battle
    </PlayGround>
    <MatchGround v-if="$store.state.battle.status === 'matching'">
        Battle
    </MatchGround>
    <ResultGround v-if="$store.state.battle.loser != 'no result yet'"> Battle </ResultGround>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import ResultGround from '../../components/ResultGround.vue'
import { onMounted, onUnmounted } from 'vue';//测试，组件被加载/断开时，可执行具体函数
import { useStore } from 'vuex';

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultGround,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;//注意，此处是 ` 不是单引号，字符串中带$时用`

        let socket = null;
        onMounted(() => {
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://pic1.zhimg.com/v2-abed1a8c04700ba7d72b45195223e0ff_l.jpg?source=1940ef5c",
            })
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connect successfully!");
                store.commit("updateSocket", socket);
            }
            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === "start-matching")//获取开始匹配的信息
                {
                    store.commit("updateOpponent",{
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2023);
                    
                    //上传给store/battle.js中updateGame接口,存储接收到的数据
                    store.commit("updateGame", data.game);
                }
                else if (data.event === "move") //获取双方移动信息
                {
                    //查询两个bots，bots在gameObject中
                    console.log(data);
                    const game = store.state.battle.gameObject;
                    const [simplebot0, simplebot1] = game.bots;
                    simplebot0.set_direction(data.a_direction);
                    simplebot1.set_direction(data.b_direction);
                } 
                
                else if (data.event === "result") //获取结果
                {
                    console.log(data);
                    const game = store.state.battle.gameObject;
                    const [simplebot0, simplebot1] = game.bots;

                    if (data.loser === "all" || data.loser === "A") {
                        simplebot0.status = "end";
                    }
                    if (data.loser === "all" || data.loser === "B") {
                        simplebot1.status = "end";
                    }
                    store.commit("updateLoser", data.loser);//结果返回，供resultground判断
                }
                

            }
            socket.onclose = () => {
                console.log("disconnect successfully!");
            }
        });

        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");
        });

    }
}

</script>

<style scoped> 

</style>