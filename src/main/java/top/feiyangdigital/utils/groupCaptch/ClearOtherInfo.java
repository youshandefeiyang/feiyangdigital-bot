package top.feiyangdigital.utils.groupCaptch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.TimerDelete;

@Component
public class ClearOtherInfo {

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private TimerDelete timerDelete;

    public void clearAdviceInfo(AbsSender sender, Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        Integer messageId = update.getMessage().getMessageId();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(chatId);
        if (groupInfoWithBLOBs != null && "open".equals(groupInfoWithBLOBs.getClearinfoflag()) && (update.getMessage().getLeftChatMember() != null || !update.getMessage().getNewChatMembers().isEmpty() || update.getMessage().getPinnedMessage() != null)) {
            sender.execute(new DeleteMessage(chatId, messageId));
        }
    }

    public void clearBotCommand(AbsSender sender, Update update) {
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(update.getMessage().getChatId().toString());
        if (groupInfoWithBLOBs != null && "open".equals(groupInfoWithBLOBs.getClearinfoflag()) && update.getMessage().hasEntities()) {
            update.getMessage().getEntities().forEach(i -> {
                if ("bot_command".equals(i.getType())) {
                    try {
                        timerDelete.deleteMessageAfterDelay(sender, update, 10);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
