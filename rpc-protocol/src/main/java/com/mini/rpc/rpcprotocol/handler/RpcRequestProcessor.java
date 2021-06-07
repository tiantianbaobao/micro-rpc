package com.mini.rpc.rpcprotocol.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/***
 * @description rpc request processor with thread
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-22 0:07
 * @since V1.0.0
 */
public class RpcRequestProcessor {
    private static ThreadPoolExecutor threadPoolExecutor;

    public static void submitTask(Runnable task){
        if (threadPoolExecutor == null) {
            synchronized (RpcRequestProcessor.class) {
                if (threadPoolExecutor == null) {
                    TimeUnit unit;
                    BlockingQueue workQueue;
                    threadPoolExecutor = new ThreadPoolExecutor(60, 60, 100, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10000));
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}
