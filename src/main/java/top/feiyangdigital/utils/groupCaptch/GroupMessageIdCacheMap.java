package top.feiyangdigital.utils.groupCaptch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;
import top.feiyangdigital.utils.TimerDelete;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GroupMessageIdCacheMap {

    @Autowired
    private TimerDelete timerDelete;

    Map<String, Integer> groupMessageIdManager = new ConcurrentHashMap<>();

    public void setGroupMessageId(String groupId, Integer messageId) {
        this.groupMessageIdManager.put(groupId + "|" + messageId, messageId);
    }

    public int getMapSize(){
        return groupMessageIdManager.size();
    }

    public void deleteAllMessage(AbsSender sender,String chatId){
        for (Map.Entry<String, Integer> entry : groupMessageIdManager.entrySet()) {
            timerDelete.deleteByMessageIdImmediately(sender, chatId, entry.getValue());
            groupMessageIdManager.remove(entry.getKey());
        }
    }

}
