const BOT_GAME_OBJECTS = []; //存储所有应用中的，操作对象的数据

export class BotGameObject {
    constructor() {
        BOT_GAME_OBJECTS.push(this);
        this.has_called_start = false;

        this.timedelta = 0; //每一帧的时间间隔
    }

    //运行时执行且仅执行一次
    start() {

    }

    //start()后每一帧执行一次
    update() {

    }

    //删除该对象前执行一次
    on_destroy() {

    }

    destroy() {
        this.on_destroy();

        for (let i in BOT_GAME_OBJECTS) {
            const obj = BOT_GAME_OBJECTS[i];
            if (obj === this) {
                BOT_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}


//让每一帧执行同一函数
let last_timestamp; //上一帧的绝对时间
const step = timestamp => { //step等于当前绝对时间
    for (let obj of BOT_GAME_OBJECTS) {
        if (!obj.has_called_start) {
            obj.has_called_start = true;//如果未执行start()，让该对象执行
            obj.start();
        }
        else //如果执行过
        {
            obj.timedelta = timestamp - last_timestamp; //timedelta等于两帧的时间差
            obj.update();
        }
    }

    last_timestamp = timestamp; //刷新上一帧结束的绝对时间
    requestAnimationFrame(step)
}

requestAnimationFrame(step)