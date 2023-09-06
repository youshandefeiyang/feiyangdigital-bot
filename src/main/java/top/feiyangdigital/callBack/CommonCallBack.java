package top.feiyangdigital.callBack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.SendContent;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonCallBack {

    @Autowired
    private SendContent sendContent;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    public void backMainMenu(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("📝自动回复##autoReply%%⚪️功能占位##zhanwei");
        keywordsButtons.add("👨🏻‍💻仓库地址$$https://github.com/youshandefeiyang/feiyangdigital-bot%%👥官方群组$$https://t.me/feiyangdigital");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>"+ addRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ addRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前可输入状态：<b>"+ addRuleCacheMap.getKeywordsFlagForUser(userId)+"</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
