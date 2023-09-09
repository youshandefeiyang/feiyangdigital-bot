package top.feiyangdigital.utils.groupCaptch;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class RestrictOrUnrestrictUser {
    public void restrictUser(AbsSender sender,Long userId,String chatId) {
        RestrictChatMember restrictChatMember = new RestrictChatMember();
        restrictChatMember.setChatId(chatId);
        restrictChatMember.setUserId(userId);

        ChatPermissions chatPermissions = new ChatPermissions();
        chatPermissions.setCanSendMessages(false);
        chatPermissions.setCanSendMediaMessages(false);
        chatPermissions.setCanSendOtherMessages(false);
        chatPermissions.setCanAddWebPagePreviews(false);

        restrictChatMember.setPermissions(chatPermissions);

        try {
            sender.execute(restrictChatMember); // Execute the Telegram API method
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void unrestrictUser(AbsSender sender,Long userId,String chatId) {
        RestrictChatMember restrictChatMember = new RestrictChatMember();
        restrictChatMember.setChatId(chatId);
        restrictChatMember.setUserId(userId);

        ChatPermissions chatPermissions = new ChatPermissions();
        chatPermissions.setCanSendMessages(true);
        chatPermissions.setCanSendMediaMessages(true);
        chatPermissions.setCanSendOtherMessages(true);
        chatPermissions.setCanAddWebPagePreviews(true);

        restrictChatMember.setPermissions(chatPermissions);

        try {
            sender.execute(restrictChatMember); // Execute the Telegram API method
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}