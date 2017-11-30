package com.irh.material.basics.netty.chapter02_2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by iritchie on 2017/8/19.
 */
public class TimeServerHandlerExecutePool{
    private static final String TAG = "TimeServerHandlerExecutePool";
    private ExecutorService executor;

    public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize){
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize,
                120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void executor(Runnable task){
        executor.execute(task);
    }
}
