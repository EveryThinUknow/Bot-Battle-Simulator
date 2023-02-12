import { BotGameObject } from "./BotGameObject";
import { Cell } from "./Cell";

export class SimpleBot extends BotGameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        //Bot的身体，cells[0]是头部
        this.cells = [new Cell(info.r, info.c)];
        this.next_cell = null; //下一步的坐标位置，next_step
        this.speed = 5; //移动速度
        this.direction = -1; //0 1 2 3 上 右 下 左
        this.status = "idle"; // idle静止， move正在移动， end结束
        
        //移动方向
        this.eps = 1e-2;
        this.dr = [-1, 0, 1, 0]; //dy,行的偏移
        this.dc = [0, 1, 0, -1]; //dx,列的偏移
        this.step = 0; //已进行几步

        //初始化bot0和bot1头部的flag（eye）的朝向
        this.flag_direction = 0;
        if (this.id === 1) this.flag_direction = 2;
        //上 右 下 左
        this.flag_dx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.flag_dy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ];
    }   

    start () {

    }

    //统一方向决策
    set_direction(d) {
        this.direction = d;
    }

    //时间越久，bot长度越长
    check_tail_increasing() {
        if (this.step <= 3) return true;
        if (this.step % 3 === 1 && this.step <= 14) return true;
        //超过十回合，不再变得更长
        return false;
    }

    //筹划下一回合的操作
    next_step() {
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1;
        this.flag_direction = d;
        this.status = "move";
        this.step ++;

        //bot的长度
        const len = this.cells.length;
        for (let i = len; i > 0; i--) 
        {
            //相当于， abc 变成了 aabc
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }

        //判断是否无法向前，如果valid没通过，status变为end，直接结束
        if (!this.gamemap.check_valid(this.next_cell))
        {
            this.status = "end";//该bot输了
        }

    }

    update_move() {
        //每一帧的移动距离
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);//勾股定理，▲x 与 ▲y 求得 两点的直线距离

        //当距离小于误差值，认为到达目的地坐标点
        if (distance < this.eps)
        {
            this.cells[0] = this.next_cell; //表现得bot往前移动，其实是在前方新添加bot的头部，原头部被覆盖
            this.next_cell = null;
            this.status = "idle"; //到达目标点，状态重新变为静止
            
            //如果bot不变长
            if (!this.check_tail_increasing())
            {
                this.cells.pop();
            }

        }
        else
        {
            const move_distance = this.speed * this.timedelta / 1000;
            this.cells[0].x += move_distance * dx / distance; //* cos
            this.cells[0].y += move_distance * dy / distance; //* sin
            
            //如果bot不变长，尾部变成倒数第二个cells[]的位置，并且原位置不删除。更新bot尾部的位置
            if (!this.check_tail_increasing())
            {   
                const len = this.cells.length;
                const tail = this.cells[len - 1];
                const tail_next = this.cells[len - 2];
                const tail_dx = tail_next.x - tail.x;
                const tail_dy = tail_next.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    update() {
        if (this.status === 'move')
        {
            this.update_move();
        }

        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        if (this.status === "end")
        {
            ctx.fillStyle = "white";
        }
        //依次取cells中的cell对象
        for(const cell of this.cells)
        {
            //圆弧
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0,  Math.PI * 2);
            ctx.fill();
        }

        //润色bot的外貌
        for (let i = 1; i < this.cells.length; i++)
        {
            const a = this.cells[i-1];
            const b = this.cells[i];

            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            if (Math.abs(a.x - b.x) < this.eps) //如果是垂直方向间距大，有缝隙，想补齐
            {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            }
            else //如果是水平方向间距大，有缝隙，想补齐
            {
                ctx.fillRect((Math.min(a.x, b.x)) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i++)
        {
            const flag_x = (this.cells[0].x + this.flag_dx[this.flag_direction][i] * 0.16) * L;
            const flag_y = (this.cells[0].y + this.flag_dy[this.flag_direction][i] * 0.16) * L;
            ctx.beginPath();
            ctx.arc(flag_x, flag_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }

    }
}