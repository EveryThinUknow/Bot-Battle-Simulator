package com.bbs.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.bbs.backend.consumer.utils.Game;
import com.bbs.backend.consumer.utils.JwtAuthentication;
import com.bbs.backend.mapper.RecordMapper;
import com.bbs.backend.mapper.UserMapper;
import com.bbs.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    //ConcurrentHashMap来安全地存储所有线程(连接)
    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    //存储要链接的客户端，非static，每个线程对应一个
    private User user;

    //维护前端和后端的链接
    private Session session = null;

    //将数据库中的user映射进来，保存成全局变量的形式，因为每个连接都要在user中去寻找对应的ID，需要遍历数据库
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    private Game game = null;

    //同上，此处是record战绩
    public static RecordMapper recordMapper;
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }

    //RestTemplate url for matching system
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";
    private static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }



/////////////////////////////
    // 建立连接
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("connect successfully!");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);//找到该用户

        if (this.user !=null)
        {
            users.put(userId, this);//存到该类的users中
        }
        else
        {
            this.session.close();
        }
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭连接
        System.out.println("disconnect successfully!");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    //后端接收前端的信息
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("receive message successfully!");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event))
        {
            startMathcing();
        }
        else if ("stop-matching".equals(event))
        {
            stopMathcing();
        }
        //获取移动方向，发送给前端(move中已实现)
        else if ("move".equals(event))
        {
            move(data.getInteger("direction"));
        }
    }

    //匹配系统
    public static void startGame(Integer aId, Integer bId) {
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);

        //14行，15列，10blocks 注意，可自行修改，但是格子总数必须是奇数格子，防止双方能同时走进同一格
        Game game = new Game(14, 15, 20, a.getId(), b.getId());
        game.createMap();//创建地图
        game.start();//开启新线程
        if (users.get(a.getId()) != null)
        {
            users.get(a.getId()).game = game;
        }
        if (users.get(b.getId()) != null)
        {
            users.get(b.getId()).game = game;
        }


        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        //发送给前端a和b
        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if (users.get(a.getId()) != null)
        {
            users.get(a.getId()).sendMessage(respA.toJSONString());
        }

        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if (users.get(b.getId()) != null)
        {
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }

    }



    private void startMathcing() {
        System.out.println("start matching!");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void stopMathcing() {
        System.out.println("stop matching!");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    private void move(int direction) {
        //判断玩家id
        if (game.getPlayerA().getId().equals(user.getId())) {//如果是A
            game.setnextStepA(direction);//Input A的选择
        } else if (game.getPlayerB().getId().equals(user.getId())) {//如果是B
            game.setnextStepB(direction);//Input B的选择
        }
    }

    //后端发送消息给前端
    public void sendMessage(String message){
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
