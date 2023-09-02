package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.sqlService.GroupInfoService;

import java.util.Date;

@Service
public class BotFirstIntoGroup {

    @Autowired
    private GroupInfoService groupInfoService;

    public boolean isFeiyangBotAddedToGroup(Message message) {
        if (message.getNewChatMembers() != null) {
            for (User user : message.getNewChatMembers()) {
                if (user.getIsBot() && BaseInfo.getBotName().equals(user.getUserName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void handleMessage(Message message) {
        if (isFeiyangBotAddedToGroup(message)) {
            String chatId = message.getChat().getId().toString();
            String groupName = message.getChat().getTitle();
            GroupInfoWithBLOBs groupInfo = new GroupInfoWithBLOBs();
            groupInfo.setGroupid(chatId);
            groupInfo.setGroupname(groupName);
            groupInfo.setSettingtimestamp(String.valueOf(new Date().getTime()));
            if (groupInfoService.selGroup(chatId)){
                groupInfoService.addGroup(groupInfo);
            }
        }
    }
}
