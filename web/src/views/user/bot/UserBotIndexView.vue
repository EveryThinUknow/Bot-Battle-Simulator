<template>
    <CommonContent>
        我的BOT
    </CommonContent>
</template>

<script>
import CommonContent from '../../../components/CommonContent.vue'
import $ from 'jquery'
import { useStore } from 'vuex'


export default{
    components: {
        CommonContent
    },
    setup() {
        const store = useStore();
        /*
        $.ajax({
            url: "http://127.0.0.1:3000/user/bot/add/",
            type: "post",
            data: {
                bot_name: "bot1",
                characterization: "botchar",
                details: "botdetails",
            },
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        });
            */
        /*
        $.ajax({
            url: "http://127.0.0.1:3000/user/bot/remove/",
            type: "post",
            data: {
                bot_id: 4,
            },
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        }); 
        */
       /*      
        $.ajax({
            url: "http://127.0.0.1:3000/user/bot/update/",
            type: "post",
            data: {
                bot_id: 5,
                bot_name: "new bot name",
                characterization: "new char",
                details: "new details",
            },
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        }); 
        */
        $.ajax({
            url: "http://127.0.0.1:3000/user/bot/getlist/",
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        }); 

    }
}

</script>

<style scoped>

</style>