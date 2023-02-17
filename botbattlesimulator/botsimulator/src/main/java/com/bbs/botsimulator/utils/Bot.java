package com.bbs.botsimulator.utils;

import java.util.ArrayList;
import java.util.List;

public class Bot implements com.bbs.botsimulator.utils.BotInterface{
    //bot的位置坐标
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean check_tail_increasing(int step) { //检验当前回合，bot的长度是否增加
        if (step <= 3) return true;
        if (step % 3 == 1 && step <= 28) return true; //超过19回合，不再变得更长
        return false;
    }
    //根据移动情况,获取bot的整个身体坐标
    public List<Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1); //去掉steps的括号
        List<Cell> body = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;//当前走到第几步
        body.add(new Cell(x, y));
        for (int i=0; i<steps.length(); i++) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            body.add(new Cell(x, y));//在头部的下一个移动方向生成个新头部
            if (!check_tail_increasing( ++ step)) {//如果没变长
                body.remove(0);//尾部直接删掉即可
            }
        }
        return body;
    }

    //处理地图
    @Override
    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[14][15];
        for (int i = 0, k = 0; i < 14; i++) {
            for (int j = 0; j < 15; j++, k++) {
                if (strs[0].charAt(k) == '1') {
                    g[i][j] = 1;
                }
            }
        }
        //字符串已经被#号隔开
        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);

        List<Cell> aCells = getCells(aSx, aSy, strs[3]);//自己
        List<Cell> bCells = getCells(bSx, bSy, strs[6]);//对手

        //在地图中标记body，碰到body和block和wall的结果一样，都会导致输掉对局，所以g[][] 设置为1
        for (Cell c: aCells) g[c.x][c.y] = 1;
        for (Cell c: bCells) g[c.x][c.y] = 1;

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        //朝周围g[][]不是1的坐标前进(决定方向)
        for (int i = 0; i < 4; i ++ ) {
            int x = aCells.get(aCells.size() - 1).x + dx[i];
            int y = aCells.get(aCells.size() - 1).y + dy[i];
            if (x >= 0 && x < 14 && y >= 0 && y < 15 && g[x][y] == 0) {
                return i;
            }
        }

        return 0;
    }

}
