package top.feiyangdigital.scheduledTasks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    private SchedulerService schedulerService;

    @Autowired
    private GroupInfoService groupInfoService;

    public void ruleHandle(AbsSender sender, String groupId, String groupName, String keyWords) {
        schedulerService.clearAllJobs();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(groupId);
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("sender", sender);
        map.put("groupId", groupId);
        map.put("groupName", groupName);
        List<KeywordsFormat> keywordsFormatList = Arrays.stream(keyWords.split("\\n{2,}"))
                .map(String::trim)
                .map(KeywordsFormat::new)
                .collect(Collectors.toList());
        for (int i = 0; i < keywordsFormatList.size(); i++) {
            Map<String, String> currentMap = keywordsFormatList.get(i).getRuleMap();
            if (currentMap.containsKey("crontabOption")) {
                if (keywordsFormatList.get(i).getVideoUrl() != null) {
                    map.put("videoUrl", keywordsFormatList.get(i).getVideoUrl());
                } else if (keywordsFormatList.get(i).getPhotoUrl() != null) {
                    map.put("photoUrl", keywordsFormatList.get(i).getPhotoUrl());
                }
                map.put("keyButtons", keywordsFormatList.get(i).getKeywordsButtons());
                String[] content = currentMap.get("crontabOption").split("、");
                map.put("text", content[1]);
                map.put("delMessageTime", content[2]);
                if (content.length == 3) {
                    if ("AllowMedia".equalsIgnoreCase(content[0]) && "open".equals(groupInfoWithBLOBs.getNightmodeflag())) {
                        schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), AllowMedia.class, content[0] + "job" + "_" + groupId + "_" + i, content[0] + "group" + "_" + groupId + "_" + i, map);
                    } else if ("ForBidMedia".equalsIgnoreCase(content[0]) && "open".equals(groupInfoWithBLOBs.getNightmodeflag())) {
                        schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), ForBidMedia.class, content[0] + "job" + "_" + groupId + "_" + i, content[0] + "group" + "_" + groupId + "_" + i, map);
                    } else if ("OnlySendMessage".equalsIgnoreCase(content[0]) && "open".equals(groupInfoWithBLOBs.getCrontabflag())) {
                        schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), OnlySendMessage.class, content[0] + "job" + "_" + groupId + "_" + i, content[0] + "group" + "_" + groupId + "_" + i, map);
                    } else if ("SendWordCloud".equalsIgnoreCase(content[0]) && "open".equals(groupInfoWithBLOBs.getCrontabflag())) {
                        schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), SendWordCloud.class, content[0] + "job" + "_" + groupId + "_" + i, content[0] + "group" + "_" + groupId + "_" + i, map);
                    }
                } else if (content.length == 4) {
                    if ("AllowMedia".equalsIgnoreCase(content[0]) && "open".equals(groupInfoWithBLOBs.getNightmodeflag())) {
                        schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), AllowMedia.class, content[0] + "job" + "_" + groupId + "_" + i, content[0] + "group" + "_" + groupId + "_" + i, map, content[3]);
                    } else if ("ForBidMedia".equalsIgnoreCase(content[0]) && "open".equals(groupInfoWithBLOBs.getNightmodeflag())) {
                        schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), ForBidMedia.class, content[0] + "job" + "_" + groupId + "_" + i, content[0] + "group" + "_" + groupId + "_" + i, map, content[3]);
                    } else if ("OnlySendMessage".equalsIgnoreCase(content[0]) && "open".equals(groupInfoWithBLOBs.getCrontabflag())) {
                        schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), OnlySendMessage.class, content[0] + "job" + "_" + groupId + "_" + i, content[0] + "group" + "_" + groupId + "_" + i, map, content[3]);
                    } else if ("SendWordCloud".equalsIgnoreCase(content[0]) && "open".equals(groupInfoWithBLOBs.getCrontabflag())) {
                        schedulerService.updateTrigger(keywordsFormatList.get(i).getReplyText(), SendWordCloud.class, content[0] + "job" + "_" + groupId + "_" + i, content[0] + "group" + "_" + groupId + "_" + i, map, content[3]);
                    }
                }
            }
        }
    }
}
