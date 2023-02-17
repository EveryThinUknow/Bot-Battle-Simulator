package com.bbs.botsimulator.service.impl.utils;

import com.bbs.botsimulator.utils.BotInterface;
import org.joor.Reflect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.UUID;

@Component
public class Executor extends Thread {
    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/battle/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Executor.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();

        try {
            this.join(timeout);//等待 timeout秒后无其它响应，会执行后续操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //中断当前线程
            this.interrupt();
        }
    }


    //在用 joor 编码的Bot类名后，添加uuid
    private String addUid(String code, String uid) {
        int k = code.indexOf(" implements com.bbs.botsimulator.utils.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);//只截取前8位
    }

    @Override
    public void run() {
        //每次请求，返回一个不同的id作为标记(uuid)
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);//只截取前八位

        //reflect来自于 joor 依赖，是动态编译java的扩展
        BotInterface botInterface = Reflect.compile(
                "com.bbs.botsimulator.utils.Bot" + uid,
                addUid(bot.getBotDetails(), uid)
        ).create().get();

        Integer direction = botInterface.nextMove(bot.getInput());
        System.out.println("move execution: " + bot.getUserId() + " " + direction);

        //传输bot的nextstep 指令给前端
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }

}
