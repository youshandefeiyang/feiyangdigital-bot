package top.feiyangdigital.utils;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminList {


    @CacheEvict(value = "adminsCache", allEntries = true)
    public String fetchHighAdminList(AbsSender sender, Update update) {
        return getAdmins(sender,update.getMessage().getChatId().toString()).stream()
                .filter(admin ->
                        (admin instanceof ChatMemberAdministrator && ((ChatMemberAdministrator) admin).getIsAnonymous()) || admin instanceof ChatMemberOwner
                ).map(admin -> String.valueOf(admin.getUser().getId()))
                .collect(Collectors.joining("|"));
    }

    @Cacheable(value = "adminsCache", key = "#chatId + 'admins'")
    public List<ChatMember> getAdmins(AbsSender sender,String chatId){
        List<ChatMember> list = new ArrayList<>();
        GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
        getChatAdministrators.setChatId(chatId);
        try {
           list = sender.execute(getChatAdministrators);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return list;
    }

}