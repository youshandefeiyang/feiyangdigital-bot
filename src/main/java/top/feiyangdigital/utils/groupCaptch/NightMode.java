package top.feiyangdigital.utils.groupCaptch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.utils.AdminList;
import top.feiyangdigital.utils.CheckUser;

@Component
public class NightMode {

    @Autowired
    private CheckUser checkUser;

    public boolean deleteMedia(AbsSender sender, Update update) {
        Message message = update.getMessage();
        boolean hasLinkOrMedia = (message.hasEntities() && "url".equals(message.getEntities().get(message.getEntities().size() - 1).getType()))
                || message.hasPhoto() || message.hasVideo() || message.hasDocument() || message.hasSticker() || message.hasVideoNote() || message.hasVoice();

        if (hasLinkOrMedia && !checkUser.isGroupAdmin(sender,update)) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(message.getChatId()));
            deleteMessage.setMessageId(message.getMessageId());

            try {
                sender.execute(deleteMessage);
                return true;
            } catch (TelegramApiException e) {
                return false;
            }
        }
        return false;
    }
}
