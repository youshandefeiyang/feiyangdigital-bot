package top.feiyangdigital.scheduledTasks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class HandleOption {

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private SchedulerService schedulerService;

    public void ruleHandle(AbsSender sender, String groupId, String keyWords) {
        schedulerService.clearAllJobs();
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("sender", sender);
        map.put("groupId", groupId);
        List<KeywordsFormat> keywordsFormatList = Arrays.stream(keyWords.split("\\n{2,}"))
                .map(String::trim)
                .map(KeywordsFormat::new)
                .collect(Collectors.toList());
        for (int i = 0; i < keywordsFormatList.size(); i++) {
            Map<String, String> currentMap = keywordsFormatList.get(i).getRuleMap();
            if (currentMap.containsKey("crontabOption")) {
                map.put("keyButtons",keywordsFormatList.get(i).getKeywordsButtons());
                if (currentMap.get("crontabOption").contains("test1")) {
                    schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), OnlySendMessage.class, "job" + i, "group" + i, map);
                } else if (currentMap.get("crontabOption").contains("test2")) {
                    schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), OnlySendMessage2.class, "job" + i, "group" + i, map);
                }
            }
        }
    }
}
