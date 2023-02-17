package com.bbs.matchingsystem.service.impl;

import com.bbs.matchingsystem.service.MatchingService;
import com.bbs.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingpool = new MatchingPool();//匹配线程

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        matchingpool.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        matchingpool.removePlayer(userId);
        return "remove player success";
    }

}
