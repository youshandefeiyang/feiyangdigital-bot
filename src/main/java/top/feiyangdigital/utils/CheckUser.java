package top.feiyangdigital.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CheckUser {

    @Autowired
    private AdminList adminList;


    public boolean isGroupAdmin(AbsSender sender, Update update) {
        for (ChatMember admin : adminList.getAdmins(sender, update.getMessage().getChatId().toString())) {
            if ("GroupAnonymousBot".equals(update.getMessage().getFrom().getUserName()) || admin.getUser().getId().equals(update.getMessage().getFrom().getId())) {
                return true;
            }
        }
        return false; // 如果用户不是管理员
    }

    public boolean isChatOwner(AbsSender sender, Update update) {
        String chatId;
        long uid;
        if (update.getMessage() == null) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            uid = update.getCallbackQuery().getFrom().getId();
        } else {
            chatId = update.getMessage().getChatId().toString();
            uid = update.getMessage().getFrom().getId();
        }
        for (ChatMember admin : adminList.getAdmins(sender, chatId)) {
            if (admin.getUser().getId().equals(uid)) {
                if (admin instanceof ChatMemberOwner) {
                    return true;
                }

            }
        }
        return false;
    }

    public Map<String,String> getChatOwner(AbsSender sender, Update update) {
        Map<String,String> map = new ConcurrentHashMap<>();
        String chatId = "";
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        }
        for (ChatMember admin : adminList.getAdmins(sender, chatId)) {
                if (admin instanceof ChatMemberOwner) {
                    map.put("ownerId",admin.getUser().getId().toString());
                    map.put("ownerFirstName",admin.getUser().getFirstName());
                }
        }
        return map;
    }

}
