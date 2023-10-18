package top.feiyangdigital.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newFixedThreadPool(10); //设为10个线程，可以根据需要进行调整
    }
}
