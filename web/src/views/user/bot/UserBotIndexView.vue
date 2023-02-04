<template>
<div class="container">
    <div class="row">
        <div class="col-3">
            <div class="card" style="margin-top: 23px;">
                <div class="card-body">
                    <img :src="$store.state.user.photo" class="card-img-top" alt="" style="width:100%;">
                </div>
            </div>
        </div>

        <div class="col-9">
            <div class="card" style="margin-top: 23px;">
                <div class="card-header">
                    <span style="font-size: 150%;">我的Bot</span>
                    <!-- Button trigger modal -->
                    <button type="button" class="btn btn-success float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                        创建Bot
                    </button>
                    <!-- Modal -->
                    <div class="modal fade" id="add-bot-btn" tabindex="-1">
                    <div class="modal-dialog modal-xl">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <!--代码编辑框-->
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="add-bot-botName" class="form-label">Bot名称</label>
                                <input v-model="botadd.bot_name" type="text" class="form-control" id="add-bot-botName" placeholder="请输入Bot名称">
                            </div>
                            <div class="mb-3">
                                <label for="add-bot-characterization" class="form-label">Bot简介</label>
                                <textarea v-model="botadd.characterization" class="form-control" id="add-bot-characterization" rows="3" placeholder="请输入Bot简介(小于150字)"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="add-bot-details" class="form-label">Bot代码</label>
                                <VAceEditor
                                v-model:value="botadd.details"
                                @init="editorInit"
                                lang="c_cpp"
                                theme="textmate"
                                style="height: 300px" />
                            </div>
                        </div>
                        <!--代码编辑框end-->>
                        <div class="modal-footer" style="width:100%;">
                            <div class="error-message">{{ botadd.receive_message }}</div>
                            <button type="button" class="btn btn-primary" @click="add_bot">保存</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                        </div>
                    </div>
                    </div>
                    </div>
                    <!-- Modal-end -->
                </div>
                <div class="card-body">
                    <table class="table table-striped table-hover">
                        <thead>
                        <tr>
                            <th>Bot名称</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <tr v-for="bot in bots" :key="bot.id">
                            <td> {{ bot.botName }}</td>
                            <td> {{ bot.createTime }}</td>
                            <td>
                                <!-- Button trigger modal -->
                                <button type="button" class="btn btn-primary" data-bs-toggle="modal" 
                                :data-bs-target="'#update-bot-btn-' + bot.id" style="margin-right: 8px;">
                                修改
                                </button>
                                <button type="button" class="btn btn-danger" style="margin-right:8px;" @click="remove_bot(bot)">
                                删除
                                </button>
                                <!-- Modal -->
                                <div class="modal fade" :id="'update-bot-btn-' + bot.id" tabindex="-1">
                                <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5" id="exampleModalLabel">修改Bot</h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <!--代码编辑框-->
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="update-bot-botName" class="form-label">Bot名称</label>
                                            <input v-model="bot.bot_name" type="text" class="form-control" id="update-bot-botName" placeholder="请输入Bot名称">
                                        </div>
                                        <div class="mb-3">
                                            <label for="update-bot-characterization" class="form-label">Bot简介</label>
                                            <textarea v-model="bot.characterization" class="form-control" id="update-bot-characterization" rows="3" placeholder="请输入Bot简介(小于150字)"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="update-bot-details" class="form-label">Bot代码</label>
                                            <VAceEditor
                                            v-model:value="bot.details"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            theme="textmate"
                                            style="height: 300px" />
                                        </div>
                                    </div>
                                    <!--代码编辑框end-->>
                                    <div class="modal-footer" style="width:100%;">
                                        <div class="error-message">{{ bot.receive_message }}</div>
                                        <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                                    </div>
                                </div>
                                </div>
                                </div>
                                <!-- Modal-end -->
                            </td>
                            </tr>
                        </tbody>
                    </table>    
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import $ from 'jquery'
import {ref, reactive} from 'vue' //ref绑定变量，reactive绑定对象
import { useStore } from 'vuex'
import router from '../../../router/index'
import { Modal} from 'bootstrap/dist/js/bootstrap'

//Vue3-ace-editor:在线网页编译器
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default{
    components: {
        VAceEditor
    },
    setup() {
        const store = useStore();
        let bots = ref([]);
//////////////////////////////////////////////
        //查getlist
        //打开该界面自动触发，显示所有该用户的Bot
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
//////////////////////////////////////////////
        //增add
        //点击创建按钮触发该函数，添加新的Bot数据
        const botadd = reactive({
            bot_name: "",
            characterization: "",
            details: "",
            receive_message: "",
        });

        const add_bot = () => {
            botadd.receive_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/add/",
                type: "post",
                data: {
                    bot_name: botadd.bot_name,
                    characterization: botadd.characterization,
                    details: botadd.details,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.receive_message === "success")
                    {
                        //编辑框数据清空
                        botadd.bot_name = "";
                        botadd.characterization = "";
                        botadd.details = "";
                        //
                        Modal.getInstance("#add-bot-btn").hide(); //关闭编辑框
                        refresh_bots(); //刷新列表 
                        router.push({ name: 'user_bot_index' }); 
                    }
                    else
                    {
                        botadd.receive_message = resp.receive_message;
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            });
        }
//////////////////////////////////////////////
        //删remove
        //点击删除按钮触发该函数，删除某个Bot
        const remove_bot = (bot) => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success() {
                    refresh_bots();
                },
                error(resp) {
                    console.log(resp);
                }
            }); 
        }
//////////////////////////////////////////////
        //改update
        //点击修改按钮出发该函数，修改某个Bot
        const update_bot = (bot) => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    bot_name: bot.bot_name,
                    characterization: bot.characterization,
                    details: bot.details,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    console.log(resp);
                    if (resp.receive_message === "success")
                    {
                        Modal.getInstance('#update-bot-btn-' + bot.id).hide(); //关闭编辑框
                        refresh_bots(); //刷新列表 
                    }
                    else
                    {
                        bot.receive_message = resp.receive_message;
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            }); 
        }
        //配置vue3-ace-editor
        ace.config.set(
        "basePath", 
        "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        return {
            bots,
            botadd,
            add_bot,
            remove_bot,
            update_bot,
        }

       /*      

        */
    }
}

</script>

<style scoped>
div.card{
    background: rgba(211,211,211,0.66);
}
div.modal{
    background: rgba(211,211,211,0.66);
}
div.error-message{
    color:tomato;
    margin-left: 30px;
}
</style>