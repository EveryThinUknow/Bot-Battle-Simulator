package com.bbs.backend.service.impl.battle;

import com.bbs.backend.consumer.WebSocketServer;
import com.bbs.backend.consumer.utils.Game;
import com.bbs.backend.service.battle.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

//接收bot编码执行的direction返回值，传送给next step
@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        //System.out.println("receive bot move: " + userId + " " + direction);用户userid的bot向direction移动
        if (WebSocketServer.users.get(userId) != null)
        {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null)
            {
                if (game.getPlayerA().getId().equals(userId)) {//如果是A的bot
                    game.setnextStepA(direction);//Input A的bot的选择
                } else if (game.getPlayerB().getId().equals(userId)) {//如果是B的bot
                    game.setnextStepB(direction);//Input B的bot的选择
                }
            }
        }
        return "receive bot move success";
    }
}
