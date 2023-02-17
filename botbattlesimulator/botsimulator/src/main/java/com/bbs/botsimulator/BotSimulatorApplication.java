package com.bbs.botsimulator;

import com.bbs.botsimulator.service.impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotSimulatorApplication {
    public static void main(String[] args) {
        BotRunningServiceImpl.botPool.start();//启动bot pool线程
        SpringApplication.run(BotSimulatorApplication.class, args);
    }
}