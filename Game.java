package com.bbs.backend.consumer.utils;

//匹配状态下，地图保存在云端（保证双方地图一致）
import com.alibaba.fastjson.JSONObject;
import com.bbs.backend.consumer.WebSocketServer;
import com.bbs.backend.pojo.Bot;
import com.bbs.backend.pojo.Record;
import com.bbs.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{ //Game需要多线程(Thread)
    final private Integer rows;//行
    final private Integer cols;//列
    final private Integer inner_walls_count;//划定block的数量
    final private int[][] g;//地图坐标
    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1}; //判定方向

    //保存play的两个玩家 a和b
    final private Player playerA, playerB;
    public Player getPlayerA()
    {
        return playerA;
    }
    public Player getPlayerB()
    {
        return playerB;
    }

    //声明a和b的下一步操作
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();//线程锁
    private String status = "playing"; //playing or finished
    private String loser = ""; //all:平局， “A”， "B"
    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";
    public void setnextStepA(Integer nextStepA)
    {
        lock.lock();
        try{
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }
    public void setnextStepB(Integer nextStepB)
    {
        lock.lock();
        try{
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }


    //初始化
    public Game(
            Integer rows,
            Integer cols,
            Integer inner_walls_count,
            Integer idA,
            Bot botA,
            Integer idB,
            Bot botB
            ) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        Integer botIdA = -1, botIdB = -1;
        String botDetailsA = "", botDetailsB = "";
        if (botA != null)
        {
            botIdA = botA.getId();
            botDetailsA = botA.getDetails();
        }
        if (botB != null)
        {
            botIdB = botB.getId();
            botDetailsB = botB.getDetails();
        }
        playerA = new Player(idA, botIdA, botDetailsA,rows-2, 1, new ArrayList<>()); //A出生在左下角
        playerB = new Player(idB, botIdB, botDetailsB, 1, cols-2, new ArrayList<>()); //B出生在右上角
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

    /////////////////////以上是同步地图，确保地图一致性，地图保存在云端
    /////////////////////以下是同步操作

    //Bot获取对战局面
    //地图#bot头部sx#头部sy#操作#对手sx#对手sy#对手操作
    private String getInput(Player player) {
        Player me, opponent;
        if (playerA.getId().equals(player.getId()))
        {
            me = playerA;
            opponent = playerB;
        }
        else
        {
            me = playerB;
            opponent = playerA;
        }

        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +
                opponent.getSx() + "#" +
                opponent.getSy() + "#(" +
                opponent.getStepsString() + ")";
    }


    private void sendBotDetails(Player player) {
        if (player.getBotId().equals(-1)) return; //-1是手动操作，无bot

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_details", player.getBotDetails());
        data.add("input", getInput(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);

    }
    //KEEP WAITING 两玩家的下一步操作，true表示接收到，false表示至少未收到一名玩家的nextstep
    private boolean nextStep()
    {
        //Bug!！！ 因为前端的Bot的speed是200ms一个cell(格子)
        //如果不sleep线程，前端会在这200ms里多次接收数据，前端就只保留200ms里的最后一步
        //表现在实际演示中的结果就是画面和操作不完全匹配(当玩家操作过快，只会成功了一部分操作)
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        sendBotDetails(playerA);
        sendBotDetails(playerB);

        for (int i = 0; i < 50; i++)
        {
            try {
                Thread.sleep(200);
                lock.lock();
                try{
                    //A和B的下一步操作都接收到（不为空），结束该判断
                    if (nextStepA != null && nextStepB != null)
                    {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //根据玩家的实际操作step判断输赢


    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 2023; i ++ ) {
            if (nextStep()) {  // 是否获取了两条蛇的下一步操作
                judge();
                if (status.equals("playing")) { //需要把双方的nextstep都发送给双方
                    sendMove();
                } else {//如果双方都收到了nextstep的消息，进行判断
                    sendResult();//nextstep的结果
                    break;
                }
            } else {
                status = "finished"; //
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";//平局
                    } else if (nextStepA == null) {
                        loser = "A";//A没在规定时间给出答案，A输
                    } else {
                        loser = "B";//同理B输
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }

    private void sendMove() {//传递nextstep信息的具体实现
        lock.lock();
        try{
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);

            sendBothMessage(resp.toJSONString());//发给前端
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }

    }

    private void sendBothMessage(String message) { //双方把nextsetp信息发送给双方的具体实现
        if (WebSocketServer.users.get(playerA.getId()) != null)
        {
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        }
        if (WebSocketServer.users.get(playerB.getId()) != null)
        {
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }
    }

    //判断nextstep是否合规
    //目的地不能是边界walls和障碍物blocks,另一个bot的cell
    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.x][cell.y] == 1) return false;

        for (int i = 0; i < n - 1; i ++ ) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }

        for (int i = 0; i < n - 1; i ++ ) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }

        return true;
    }
    private void judge() {
        //获取A,B的bot的身体
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        //查看走完nextstep后，是否合规
        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";//当至少一个bot不合规时，游戏结束，能判定结果

            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();//把对战数据存入该对局的record中
        sendBothMessage(resp.toJSONString());//发给前端
    }

    ///////////////////Record存入数据库////////////////
    private void saveToDatabase() {
        //对战出结果后，获胜方加分，失败方扣分
        Integer playerARating = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer playerBRating = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        if ("A".equals(loser))
        {
            playerARating -= 30;
            playerBRating += 50;
        }
        else if ("B".equals(loser))
        {
            playerARating += 50;
            playerBRating -= 30;
        }
        RatingChange(playerA, playerARating);
        RatingChange(playerB, playerBRating);

        Record record = new Record (
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),//getStepsString在Player.java中
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
                );

        WebSocketServer.recordMapper.insert(record);
    }

    private void RatingChange(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating); //修改user类中rating的值
        WebSocketServer.userMapper.updateById(user); //将修改后的user上传更新到数据库
    }

    //与getStepsString同操作
    private String getMapString() {
        StringBuilder br = new StringBuilder();
        for (int i=0; i<rows; i++)
        {
            for (int j=0; j<cols; j++)
            {
                br.append(g[i][j]);
            }
        }
        return br.toString();
    }

}

