package com.bbs.botsimulator.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final Queue<Bot> bots = new LinkedList<>();//等待执行任务的bot队列
    public void addBot(Integer userId, String botDetails, String input) {
        lock.lock();
        try {
            bots.add(new Bot(userId, botDetails, input));
            condition.signalAll();//当有bot加进来时，唤醒其它线程
        } finally {
            lock.unlock();
        }
    }


    private void execute(Bot bot) {
        Executor executor = new Executor();
        executor.startTimeout(200, bot);
    }

    @Override
    public void run() {
        super.run();
        while (true)
        {
            lock.lock();
            if (bots.isEmpty())
            {
                try {
                    condition.await();//等待,当有任务时候(上面的condition.signalAll())，await自动解锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }
            else //如果有任务
            {
                Bot bot = bots.remove();//执行完毕，从pool中移除
                lock.unlock();
                execute(bot);
            }
        }
    }
}
