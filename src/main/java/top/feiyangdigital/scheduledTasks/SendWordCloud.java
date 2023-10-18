package top.feiyangdigital.scheduledTasks;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;
import top.feiyangdigital.wordCloud.WordCloudGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class SendWordCloud implements Job {
    
    @Autowired
    private SendContent sendContent;

    @Autowired
    private WordCloudGenerator wordCloudGenerator;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.warn("触发定时发送词云统计");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        AbsSender sender = (AbsSender) dataMap.get("sender");
        String groupId = dataMap.getString("groupId");
        String groupName = dataMap.getString("groupName");
        String timeContent = LocalDate.now(ZoneId.of("Asia/Shanghai")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        try {
            wordCloudGenerator.generateAndSendWordCloud(sender, groupId, groupName, timeContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
