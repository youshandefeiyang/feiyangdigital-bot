package top.feiyangdigital.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminList {

    public String fetchHighAdminList(AbsSender sender, Update update) {
        List<ChatMember> admins;
        try {
            GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
            getChatAdministrators.setChatId(update.getMessage().getChatId());
            admins = sender.execute(getChatAdministrators);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return admins.stream()
                .filter(admin ->
                        (admin instanceof ChatMemberAdministrator && ((ChatMemberAdministrator) admin).getIsAnonymous()) || admin instanceof ChatMemberOwner
                ).map(admin -> String.valueOf(admin.getUser().getId()))
                .collect(Collectors.joining("|"));
    }

}