package top.feiyangdigital.scheduledTasks;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AllowMedia implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("执行允许媒体任务22222222");

    }
}
