package com.bbs.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id; //玩家的id
    private Integer sx; //出生点坐标，行
    private Integer sy; //出生点坐标，列
    private List<Integer> steps; //存储移动路径（Integer存方向）



    private boolean check_tail_increasing(int step) {  // 检验当前回合，bot的长度是否增加
        if (step <= 3) return true;
        if (step % 3 == 1 && step <= 14) return true;     //超过十回合，不再变得更长
        return false;
    }

    //根据移动情况,生成bot的身体
    public List<Cell> getCells() {
        List<Cell> body = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;//当前走到第几步
        body.add(new Cell(x, y));
        for (int d: steps) {
            x += dx[d];
            y += dy[d];
            body.add(new Cell(x, y));//在头部的下一个移动方向生成个新头部
            if (!check_tail_increasing( ++ step)) {//如果没变长
                body.remove(0);//尾部直接删掉即可
            }
        }
        return body;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int d: steps) {
            res.append(d);
        }
        return res.toString();
    }
}

