package top.feiyangdigital.utils.groupCaptch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.handleService.BotHelper;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.AdminList;
import top.feiyangdigital.utils.CheckUser;

import java.util.Date;

@Component
public class SetBot {

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private BotHelper botHelper;

    @Autowired
    private AdminList adminList;

    @Autowired
    private CheckUser checkUser;

    public boolean adminSetBot(AbsSender sender, Update update) {
        if (("/setbot".equals(update.getMessage().getText()) || ("/setbot@" + BaseInfo.getBotName()).equals(update.getMessage().getText())) && ("GroupAnonymousBot".equals(update.getMessage().getFrom().getUserName()) || checkUser.isGroupChannel(sender, update) || checkUser.isChatOwner(sender, update))) {
            GroupInfoWithBLOBs groupInfo = new GroupInfoWithBLOBs();
            groupInfo.setOwnerandanonymousadmins(adminList.fetchHighAdminList(sender, update));
            groupInfo.setGroupname(update.getMessage().getChat().getTitle());
            groupInfo.setSettingtimestamp(String.valueOf(new Date().getTime()));
            groupInfoService.updateSelectiveByChatId(groupInfo, update.getMessage().getChatId().toString());
            botHelper.sendAdminButton(sender, update);
            try {
                sender.execute(new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return true;
        } else if ("/setbot".equals(update.getMessage().getText()) || ("/setbot@" + BaseInfo.getBotName()).equals(update.getMessage().getText())) {
            try {
                sender.execute(new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
