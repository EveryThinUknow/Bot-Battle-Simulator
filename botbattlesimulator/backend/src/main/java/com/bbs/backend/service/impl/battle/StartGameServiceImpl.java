package com.bbs.backend.service.impl.battle;

import com.bbs.backend.consumer.WebSocketServer;
import com.bbs.backend.service.battle.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        WebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "start game success";
    }
}
