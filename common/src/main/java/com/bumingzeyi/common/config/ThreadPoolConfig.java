package com.bumingzeyi.common.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author :BuMing
 * @date : 2022-04-01 21:10
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(value = "threadPoolInstance")
    public ExecutorService createThreadPoolInstance(){
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNamePrefix("thread-pool-%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        return threadPoolExecutor;
    }

}
