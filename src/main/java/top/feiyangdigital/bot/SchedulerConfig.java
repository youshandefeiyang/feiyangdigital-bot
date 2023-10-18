package top.feiyangdigital.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class SchedulerConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);  // 配置线程池大小
        taskScheduler.setThreadNamePrefix("my-scheduler-");
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int core = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(core); // 核心线程数
        executor.setMaxPoolSize(core * 2 + 1);  // 最大线程数
        executor.setQueueCapacity(100); // 队列容量
        executor.setKeepAliveSeconds(60); // 线程空闲时间
        executor.setThreadNamePrefix("TaskExecutor-"); // 线程前缀名称
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 设置拒绝策略
        executor.initialize();
        return executor;
    }
}
