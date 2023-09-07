package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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

    public void handleMessage(AbsSender sender, Message message) {
        if (isFeiyangBotAddedToGroup(message)) {
            String chatId = message.getChat().getId().toString();
            if ("open".equals(BaseInfo.getBotLimitStatus()) && BaseInfo.getGroupWhiteList().contains(chatId)) {
                String groupName = message.getChat().getTitle();
                GroupInfoWithBLOBs groupInfo = new GroupInfoWithBLOBs();
                groupInfo.setGroupid(chatId);
                groupInfo.setGroupname(groupName);
                groupInfo.setSettingtimestamp(String.valueOf(new Date().getTime()));
                if (groupInfoService.selGroup(chatId)) {
                    groupInfoService.addGroup(groupInfo);
                }
            } else {
                LeaveChat leaveChat = new LeaveChat();
                leaveChat.setChatId(chatId);
                try {
                    sender.execute(leaveChat);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
