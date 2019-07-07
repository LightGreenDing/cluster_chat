package com.wt.wtchat.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author Administrator
 */
@Component
public class ThreadPoolTaskExecutorEx {
    private ThreadPoolTaskExecutor taskExecutor;

    public ThreadPoolTaskExecutorEx() {
        this(20, 200, 200, 60, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public ThreadPoolTaskExecutorEx(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds, RejectedExecutionHandler handler) {

        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setRejectedExecutionHandler(handler);
        taskExecutor.afterPropertiesSet();
    }


    public void execute(Runnable task) {
        taskExecutor.execute(task);
    }

    public Map<String, Object> getPoolInfo() {
        ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
        Map<String, Object> info = new HashMap<>();
        info.put("PoolSize", threadPoolExecutor.getPoolSize());
        info.put("CorePoolSize", threadPoolExecutor.getCorePoolSize());
        info.put("Active", taskExecutor.getActiveCount());
        info.put("Completed", threadPoolExecutor.getCompletedTaskCount());
        info.put("Task", threadPoolExecutor.getTaskCount());
        info.put("Queue", threadPoolExecutor.getQueue().size());
        info.put("LargestPoolSize", threadPoolExecutor.getLargestPoolSize());
        info.put("MaximumPoolSize", threadPoolExecutor.getMaximumPoolSize());
        info.put("KeepAliveTime", threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS));
        info.put("Shutdown", threadPoolExecutor.isShutdown());
        info.put("Terminating", threadPoolExecutor.isTerminating());
        info.put("Terminated", threadPoolExecutor.isTerminated());
        return info;
    }

    public ThreadPoolTaskExecutor getTaskExecutor() {
        return taskExecutor;
    }
}
