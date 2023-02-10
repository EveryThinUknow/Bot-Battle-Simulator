package com.bbs.backend.consumer.utils;

//匹配状态下，地图保存在云端（保证双方地图一致）
import java.util.Random;

public class Game {
    final private Integer rows;//行
    final private Integer cols;//列
    final private Integer inner_walls_count;//划定block的数量
    final private int[][] g;//地图坐标
    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1}; //判定方向

    //初始化
    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
    }


    //获取整个坐标信息
    public int[][] getG() {
        return g;
    }


    //绘制地图
    private boolean draw() {
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;//先初始化
            }
        }
        //地图最外层的一圈是“墙”，因此g[][] = 1，0代表可以通过，1代表block
        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }
        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 2023; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                //对称绘制，确保公平性
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                //左下角和右上角的出生点不能是block
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;
                //该点和对称点成为block，绘制成功
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++ ) {
            if (draw())
                break;
        }
    }


    //判断地图的连通性，不连通两bot无法相遇
    private boolean check_connectivity(int cx, int cy, int tx, int ty) {
        if (cx == tx && cy == ty) return true;
        g[cx][cy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = cx + dx[i], y = cy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[cx][cy] = 0;
                    return true;
                }
            }
        }

        g[cx][cy] = 0;
        return false;
    }

}

