package com.bbs.botsimulator.service;

public interface BotRunningService {
    String addBot(Integer userId, String botDetails, String input); //details: bot的代码; input: bot的输入
}
