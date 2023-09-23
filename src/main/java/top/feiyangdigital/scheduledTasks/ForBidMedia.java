package top.feiyangdigital.scheduledTasks;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ForBidMedia implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("执行禁止媒体任务1111111");

    }
}
