import { BotGameObject } from "./BotGameObject";
import { Wall } from "./Walls";
//地图
export class GameMap extends BotGameObject {
    constructor(ctx, parent) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //每一块砖的绝对距离
        //方块格的长宽边个数
        this.rows = 13;//列
        this.cols = 13;//行

        this.blocks = [];//墙壁，边界
        this.blocks_count = 10;//内部障碍物的数量
    }

    //图的连通性判断
    check_connect(block, start_x, start_y, end_x, end_y) {
        //start既是起始坐标，也表示当前的位置
        if (start_x === end_x && start_y === end_y)
        {
            return true;
        }
        block[start_x][start_y] = true;
        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for (let i = 0; i < 4; i++)
        {
            let x = start_x + dx[i], y = start_y + dy[i];
            //判断有没有撞墙,递归，继续判断
            if(!block[x][y] && this.check_connect(block, x, y, end_x, end_y))
                return true;
        }

        return false;
    }

    //生成walls和blocks
    create_walls() {
        const  block = [];
        for (let r = 0; r < this.rows; r++)
        {
            block[r] = [];
            for (let c = 0; c < this.cols; c++)
            {
                block[r][c] = false;
            }
        }
        
        //边界均为墙
            //左右边界
        for (let r = 0; r < this.rows; r++){
            block[r][0] = block[r][this.cols - 1] = true;//每一行的第一列和最后一列为true，即该方格为边界墙
        }
            //上下边界
        for (let c = 0; c < this.cols; c++){
            block[0][c] = block[this.rows - 1][c] = true;//每一列的第一行和最后一行为true，即该方格为边界墙
        }        

        //生成地图（随机生成障碍物方块blocks）
        for (let i = 0; i < this.blocks_count; i++)
        {
            for (let j = 0; j < 2023; j++)
            {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                //以右倾对角线为轴，生成对称的地图布局，如果block[][] == true,证明已经生成过block或者wall
                if (block[r][c] || block[c][r]) continue;
                if (r === this.rows -2 || c === this.cols -2) continue; //不在左下角和右上角出生点生成block
                
                block[r][c] = block[c][r] = true;
                break;//成功找到，break子循环
            }
        }

        //复制一份，用于判断连通性（因为判断算法会改变方格的bool值）
        const copy_block = JSON.parse(JSON.stringify(block));
        if (!this.check_connect(copy_block, this.rows - 2, 1, 1, this.cols - 2)) return false;
        //生成blocks和walls
        for (let r = 0; r < this.rows; r++)
        {
            for (let c = 0; c < this.cols; c++)
            {
                if(block[r][c] === true)
                    this.blocks.push(new Wall(r, c, this));
            }
        }

        return true;
    }

    start() {
        for (let i = 0; i < 2023; i++)
        {
            this.create_walls();
            if (this.create_walls())
                break;
        }
    }

    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
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