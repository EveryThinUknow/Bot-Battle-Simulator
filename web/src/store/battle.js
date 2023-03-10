//import $ from 'jquery'

export default {
    state: {
        status: "matching", //matching, playing
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        gamemap: null,//地图数据
        //a和b控制移动的方向坐标，id
        a_id: 0,
        a_sx: 0,
        a_sy: 0,
        b_id: 0,
        b_sx: 0,
        b_sy: 0,
        gameObject: null, //bot的身体
        loser: "no result yet", //all平局/A A输/B B输。 注意区分大小写,是之前定义好的
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;//对手昵称
            state.opponent_photo = opponent.photo;//对手头像
        },
        updateStatus(state, status) {
            state.status = status;//匹配状态 playing or matching
        },
        updateGame(state, game) {
            state.gamemap = game.map;//将其中的map数据存到前端的gamemap中
            //玩家a和b的id和出生点坐标
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
        },
        updateGameObject(state, gameObject) { //存储两个bot游戏对象
            state.gameObject = gameObject;
        },
        updateLoser(state, loser) {
            state.loser = loser;
        }
    },
    actions: {

    },
    modules: {
    }
}
