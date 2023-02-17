package com.bbs.botsimulator.service.impl;

import com.bbs.botsimulator.service.BotRunningService;
import com.bbs.botsimulator.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botPool = new BotPool();

    @Override
    public String addBot(Integer userId, String botDetails, String input) {
        botPool.addBot(userId, botDetails, input);
        return "add bot success";
    }
}
