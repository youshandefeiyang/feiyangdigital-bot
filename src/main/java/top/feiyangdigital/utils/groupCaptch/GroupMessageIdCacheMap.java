package top.feiyangdigital.utils.groupCaptch;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GroupMessageIdCacheMap {
    Map<String,Integer> groupMessageIdManager= new ConcurrentHashMap<>();

    public Integer getGroupMessageId(String groupId) {
        return groupMessageIdManager.get(groupId);
    }

    public void setGroupMessageId(String groupId,Integer messageId) {
        this.groupMessageIdManager.put(groupId,messageId);
    }
}
