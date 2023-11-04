package top.feiyangdigital.scheduledTasks;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.bots.AbsSender;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;

import java.util.List;

@Component
@Slf4j
public class OnlySendMessage implements Job {

    @Autowired
    private SendContent sendContent;

    @Autowired
    private TimerDelete timerDelete;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws RuntimeException {
        log.warn("仅仅只发送消息，不执行操作");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        AbsSender sender = (AbsSender) dataMap.get("sender");
        String photoUrl = dataMap.getString("photoUrl");
        String videoUrl = dataMap.getString("videoUrl");
        String groupId = dataMap.getString("groupId");
        int delMessageTime = dataMap.getInt("delMessageTime");
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsFormat.setKeywordsButtons((List<String>) dataMap.get("keyButtons"));
        keywordsFormat.setReplyText(dataMap.getString("text"));
        if (StringUtils.hasText(photoUrl)) {
            keywordsFormat.setPhotoUrl(photoUrl);
        } else if (StringUtils.hasText(videoUrl)) {
            keywordsFormat.setVideoUrl(videoUrl);
        }
        timerDelete.sendTimedMessage(sender, sendContent.createGroupMessage(groupId, keywordsFormat, "html"), delMessageTime);
    }
}
