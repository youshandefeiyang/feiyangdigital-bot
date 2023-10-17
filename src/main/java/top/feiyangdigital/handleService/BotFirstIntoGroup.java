package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.sqlService.GroupInfoService;

@Service
public class BotFirstIntoGroup {
    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private NewMemberIntoGroup newMemberIntoGroup;

    public void handleMessage(AbsSender sender, Update update) throws TelegramApiException {
        Message message = update.getMessage();
        if (message.getNewChatMembers() != null) {
            for (User user : message.getNewChatMembers()) {
                if (user.getIsBot()) {
                    if (BaseInfo.getBotName().equals(user.getUserName())) {
                        String chatId = message.getChat().getId().toString();
                        if ("close".equals(BaseInfo.getBotLimitStatus()) || ("open".equals(BaseInfo.getBotLimitStatus()) && BaseInfo.getGroupWhiteList().contains(chatId))) {
                            String groupName = message.getChat().getTitle();
                            GroupInfoWithBLOBs groupInfo = new GroupInfoWithBLOBs();
                            groupInfo.setGroupid(chatId);
                            groupInfo.setGroupname(groupName);
                            groupInfo.setSettingtimestamp(String.valueOf(System.currentTimeMillis()));
                            if (groupInfoService.selGroup(chatId)) {
                                groupInfoService.addGroup(groupInfo);
                            }
                        } else {
                            LeaveChat leaveChat = new LeaveChat();
                            leaveChat.setChatId(chatId);
                            sender.execute(leaveChat);
                        }
                    } else {
                        newMemberIntoGroup.handleMessage(sender, update, user);
                    }
                }
            }
        }
    }
}
