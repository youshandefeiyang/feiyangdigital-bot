package top.feiyangdigital.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.handleService.SpamChannelBotService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CheckUser {

    @Autowired
    private AdminList adminList;

    @CacheEvict(value = "adminsCache", allEntries = true)
    public String fetchHighAdminList(AbsSender sender, Update update) throws TelegramApiException {
        return adminList.getAdmins(sender,update.getMessage().getChatId().toString()).stream()
                .filter(admin ->
                        (admin instanceof ChatMemberAdministrator && ((ChatMemberAdministrator) admin).getIsAnonymous()) || admin instanceof ChatMemberOwner
                ).map(admin -> String.valueOf(admin.getUser().getId()))
                .collect(Collectors.joining("|"));
    }

    public boolean isGroupChannel(AbsSender sender,Update update){
        Chat senderChat = update.getMessage().getSenderChat();
        String chatId = update.getMessage().getChatId().toString();
        if (senderChat !=null && "channel".equals(senderChat.getType())){
            Chat checkChat = new Chat();
            try {
                GetChat getChat = GetChat.builder().chatId(chatId).build();
                checkChat = sender.execute(getChat);
            }catch (Exception e){
                e.printStackTrace();
            }
            return checkChat.getLinkedChatId() != null && senderChat.getId().equals(checkChat.getLinkedChatId());
        }
        return false;
    }


    public boolean isGroupAdmin(AbsSender sender, Update update) throws TelegramApiException {
        for (ChatMember admin : adminList.getAdmins(sender, update.getMessage().getChatId().toString())) {
            if ("GroupAnonymousBot".equals(update.getMessage().getFrom().getUserName()) || admin.getUser().getId().equals(update.getMessage().getFrom().getId()) || isGroupChannel(sender,update)) {
                return true;
            }
        }
        return false; // 如果用户不是管理员
    }

    public boolean isChatOwner(AbsSender sender, Update update) throws TelegramApiException {
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

    public Map<String,String> getChatOwner(AbsSender sender, Update update) throws TelegramApiException {
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
