import { BotGameObject } from "./BotGameObject";

export class Cell extends BotGameObject {
    constructor(r, c){
        super();
        
        this.r = r;
        this.c = c;
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}