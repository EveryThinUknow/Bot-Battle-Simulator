package com.bbs.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

//匹配线程工具类
@Component
public class MatchingPool extends Thread {
    private static List<Player> players = new ArrayList<>();//正要匹配的player
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:3000/battle/start/game/";
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }
    public void addPlayer(Integer userId, Integer rating, Integer botId) {
        lock.lock();
        try{
            players.add(new Player(userId, rating, botId, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try{
            List<Player> newPlayers = new ArrayList<>();//创建新的list存⬇
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {//除了remove的player外，剩下的players
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }


//////////////////////////////////
    //匹配监听函数 及其辅助函数
    @Override
    public void run() {
        while(true)
        {
            try{
                Thread.sleep(200);
                lock.lock();
                try {
                    increaseMatchingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void increaseMatchingTime() {//匹配时间+1s
        for (Player player : players)
        {
            player.setMatchingTime(player.getMatchingTime() + 1);
        }
    }
    private void matchPlayers() { //监听所有玩家，进行匹配
        boolean[] used = new boolean[players.size()];//如果该player已经matched，就不管他了，等待删去即可
        for (int i=0; i<players.size(); i++)
        {

            if (used[i]) continue;
            for (int j=i+1; j<players.size(); j++)
            {
                if (used[j]) continue;
                //找到未匹配的两名player
                Player a = players.get(i);
                Player b = players.get(j);
                if (can_Matched(a, b))//找到能够匹配的两个player
                {
                    used[i] = used[j] = true;//匹配并且标记已匹配
                    matchResult(a, b);
                    break;
                }
            }
        }
        //在matchingPool删去匹配成功的players,注意，used[]是按顺序存储的，不能用上面的remove函数在这里实现删除操作
        List<Player> newPlayers = new ArrayList<>();
        for (int i=0; i<players.size(); i++)
        {
            if (!used[i])
            {
                newPlayers.add(players.get(i));
            }
        }

        players = newPlayers;
    }
    //我发现csgo或者lol等竞技游戏都有匹配时间越长，越有可能匹配到分差更大的对手，这样的机制是为了防止用户无限等待
    private boolean can_Matched(Player a, Player b) { //判断找到的两个player能否匹配(分差不能过大)
        int ratingDifference = Math.abs(a.getRating() - b.getRating()); //分差
        int matchingTime = Math.min(a.getMatchingTime(), b.getMatchingTime());
        if (ratingDifference >= 500) return false; //500分以上属于分差局，不可匹配
        return ratingDifference <= matchingTime * 10;

    }
    private void matchResult(Player a, Player b) { //返回匹配结果
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_id", b.getUserId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }
}
