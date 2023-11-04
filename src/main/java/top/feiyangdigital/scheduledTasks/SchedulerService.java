package top.feiyangdigital.scheduledTasks;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TimeZone;

@Service
@Slf4j
public class SchedulerService {

    @Autowired
    private Scheduler scheduler;

    public void updateTrigger(
            String newCronExpression,
            Class<? extends Job> jobClass,
            String jobName,
            String groupName,
            Map<String, Object> jobParams
    ) {
        updateTrigger(newCronExpression, jobClass, jobName, groupName, jobParams, "Asia/Shanghai");  // 默认时区为 "Asia/Shanghai"
    }

    public void updateTrigger(String newCronExpression, Class<? extends Job> jobClass, String jobName, String groupName, Map<String, Object> jobParams, String timeZoneId) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName + "Trigger", groupName);
        JobKey jobKey = JobKey.jobKey(jobName, groupName);

        try {
            // 检查现有的触发器
            CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            JobDetail oldJobDetail = scheduler.getJobDetail(jobKey);

            // 判断触发器和任务是否存在
            if (oldTrigger != null && oldJobDetail != null) {
                String oldCronExpression = oldTrigger.getCronExpression();
                // 如果Cron表达式和Job类都没有变化，则无需执行任何操作
                if (oldCronExpression.equals(newCronExpression) && oldJobDetail.getJobClass().equals(jobClass)) {
                    return;
                }
                // 删除旧任务
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);
            }

            // 创建和安排新任务
            JobDataMap jobDataMap = new JobDataMap(jobParams);  // 创建JobDataMap来存储参数
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .setJobData(jobDataMap)  // 设置JobDataMap
                    .build();
            CronTrigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(newCronExpression)
                            .inTimeZone(TimeZone.getTimeZone(timeZoneId)))  // 使用时区参数
                    .build();

            scheduler.scheduleJob(jobDetail, newTrigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务触发器失败", e);
        }
    }

    public void clearJobsWithGroupPrefix(String groupPrefix) {
        try {
            // 获取所有的 job group
            for (String groupName : scheduler.getJobGroupNames()) {
                // 遍历 group 内所有的 job
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    if (jobKey.getName().startsWith(groupPrefix)) {
                        // 删除 job
                        scheduler.deleteJob(jobKey);
                    }
                }
            }
            log.warn("所有以{}开头的组的Job，都被成功清除", groupPrefix);
        } catch (SchedulerException e) {
            log.error("清除所有以{}开头的组的Job，发生异常", groupPrefix, e);
        }
    }

    public void clearAllJobs(String chatId, String chatName) {
        try {
            // 获取所有的 job group
            for (String groupName : scheduler.getJobGroupNames()) {
                // 遍历 group 内所有的 job
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    if (jobKey.getName().contains(chatId)) {
                        // 删除 job
                        scheduler.deleteJob(jobKey);
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error("清除除群组{}所有job失败", chatName, e);
        }
    }

}