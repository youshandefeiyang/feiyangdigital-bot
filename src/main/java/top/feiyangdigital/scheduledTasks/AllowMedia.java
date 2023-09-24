package top.feiyangdigital.scheduledTasks;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;

import java.util.List;

@Component
@Slf4j
public class AllowMedia implements Job {

    @Autowired
    private SendContent sendContent;

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private GroupInfoService groupInfoService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("执行关闭夜间模式");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        AbsSender sender = (AbsSender) dataMap.get("sender");
        String groupId = dataMap.getString("groupId");
        int delMessageTime = dataMap.getInt("delMessageTime");
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsFormat.setKeywordsButtons((List<String>) dataMap.get("keyButtons"));
        keywordsFormat.setReplyText(dataMap.getString("text"));
        timerDelete.sendTimedMessage(sender,sendContent.createGroupMessage(groupId,keywordsFormat,"html"),delMessageTime);
        GroupInfoWithBLOBs groupInfoWithBLOBs = new GroupInfoWithBLOBs();
        groupInfoWithBLOBs.setCansendmediaflag("close");
        if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs,groupId)){
            log.info("夜间模式关闭成功");
        }
    }
}
