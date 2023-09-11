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

        chatPermissions.setCanSendOtherMessages(false);


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

        ChatPermissions permissions = new ChatPermissions();
        permissions.setCanSendMessages(true);
        permissions.setCanSendPolls(true);
        permissions.setCanSendOtherMessages(true);
        permissions.setCanAddWebPagePreviews(true);
        permissions.setCanChangeInfo(true);
        permissions.setCanInviteUsers(true);
        permissions.setCanPinMessages(true);

        restrictChatMember.setPermissions(permissions);

        try {
            sender.execute(restrictChatMember); // Execute the Telegram API method
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}