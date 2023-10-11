package top.feiyangdigital.utils.groupCaptch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.CheckUser;
import top.feiyangdigital.utils.TimerDelete;

import java.util.Map;

@Component
public class ReportToOwner {

    @Autowired
    private CheckUser checkUser;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private TimerDelete timerDelete;

    public boolean haddle(AbsSender sender, Update update) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        Integer messageId = update.getMessage().getMessageId();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(chatId);
        if ("@admin".equals(update.getMessage().getText()) && "open".equals(groupInfoWithBLOBs.getReportflag())) {
            Map<String, String> map = checkUser.getChatOwner(sender, update);
            String text = String.format("用户 <b>%s</b> 在 <b>%s</b> 召唤群主 <b><a href=\"tg://user?id=%d\">%s</a></b> 请立即查看消息！", update.getMessage().getFrom().getFirstName(), update.getMessage().getChat().getTitle(), Long.valueOf(map.get("ownerId")), map.get("ownerFirstName"));
            SendMessage notification = new SendMessage();
            notification.setChatId(chatId);
            notification.setText(text);
            notification.setParseMode(ParseMode.HTML);
            sender.execute(notification);
            timerDelete.deleteMessageByMessageIdDelay(sender,chatId,messageId,10);
            return true;
        }
        return false;
    }
}
