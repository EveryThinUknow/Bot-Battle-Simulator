import { BotGameObject } from "./BotGameObject";
//地图的墙
export class Wall extends BotGameObject {
    constructor(r, c, gamemap) {
        super();

        this.r = r;
        this.c = c;
        this.gamemap = gamemap;
        this.color = 'rgba(169, 172, 182, 0.9)';//墙壁颜色：深灰色
    }

    update() {
        this.render();
    }

    render() {
        const L = this.gamemap.L;//方格的边长
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;//墙的颜色
        ctx.fillRect(this.c * L, this.r * L, L , L);
    }
}