import { BotGameObject } from "./BotGameObject";
import { SimpleBot } from "./SimpleBot";
import { Wall } from "./Walls";
//地图
export class GameMap extends BotGameObject {
    constructor(ctx, parent, store) {
        super();

        //联机地图数据
        this.store = store;

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //每一块砖的绝对距离

        //方块格的长宽边个数，边界，block等，前端测试用
        this.rows = 14;//列
        this.cols = 15;//行
        this.blocks = [];//墙壁，边界
        this.blocks_count = 10;//内部障碍物的数量

        //两个对战的bot
        this.bots = [
            new SimpleBot({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new SimpleBot({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ]

    }



    //生成walls和blocks
    create_walls() {
        const block = this.store.state.battle.gamemap;

        for (let r = 0; r < this.rows; r++)
        {
            for (let c = 0; c < this.cols; c++)
            {
                if(block[r][c]) //后端设定block值为0是通的，值为1的格子是block
                    this.blocks.push(new Wall(r, c, this));
            }
        }
    }

    //绑定键盘操作方向
    add_listening_events() {
        this.ctx.canvas.focus();
        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;
            if (e.key === 'w') d = 0;
            else if (e.key === 'd') d = 1;
            else if (e.key === 's') d = 2;
            else if (e.key === 'a') d = 3;

            //从后端game的move中，获取用户的移动方向
            if (d >= 0) {
                this.store.state.battle.socket.send(JSON.stringify({
                    event: "move",//读取后端给的move数据
                    direction: d,
                }));
            }

        });
    }

    start() {
        this.create_walls();    
        this.add_listening_events();
    }

    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    //判断移动是否可以执行（目的地不能是边界walls和障碍物blocks,另一个bot的cell）
    check_valid(cell) {
        for (const block of this.blocks )
        {
            if (block.r === cell.r && block.c === cell.c)
                return false;
        }

        for (const bot of this.bots)
        {
            let len = bot.cells.length;
            //如果bot的尾部不变长，则无需判断，直接valid
            if (!bot.check_tail_increasing())
            {
                len --;
            }
            for (let i = 0; i < len; i++)
            {
                if (bot.cells[i].r === cell.r && bot.cells[i].c === cell.c)
                {
                    return false;
                }
            }
        }

        return true;
    }
    
    //判断当前回合是否执行完毕，并且下回合的操作已经就绪，如ready，才可以移动
    check_ready() {
        for (const bot of this.bots) 
        {
            if (bot.status !== "idle") return false; 
            if (bot.direction === -1) return false;
        }
        return true;
    }

    //两个bot均执行next_step操作
    next_step() {
        //分别执行下一回合的操作
        for (const bot of this.bots)
        {
            bot.next_step();
        }
    }

    update() {
        this.update_size();
        if (this.check_ready() === true)
        {
            this.next_step();
        }
        this.render();
    }

    render() {
        //this.ctx.globalAlpha=0.4;//全局透明度
        const color_even = 'rgba(233, 214, 107, 0.4)'; //偶数格是浅黄色
        const color_odd  = 'rgba(228, 155, 15, 0.4)'; //奇数格是深黄色

        for (let r = 0; r < this.rows; r++)
        {
            for (let c = 0; c < this.cols; c++)
            {
                //如果是偶数格
                if((r + c) % 2 == 0)
                {
                    this.ctx.fillStyle = color_even;
                } 
                else 
                {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(this.L * c, this.L * r, this.L, this.L);//加载画布
            }
        }

    }


}