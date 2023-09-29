package top.feiyangdigital.callBack.spamOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;

import java.util.ArrayList;
import java.util.List;

@Component
public class SetFloodInfoCount {

    @Autowired
    private SendContent sendContent;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    public void haddle(AbsSender sender, Update update, boolean mode) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String second = "";
        String infoCount = "";
        if (!mode && StringUtils.hasText(groupInfoWithBLOBs.getAntifloodsetting())) {
            second = groupInfoWithBLOBs.getAntifloodsetting().split(",")[0];
            infoCount = groupInfoWithBLOBs.getAntifloodsetting().split(",")[1];
        }
        if (mode) {
            String count = update.getCallbackQuery().getData().substring(14);
            GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
            groupInfoWithBLOBs1.setAntifloodsetting(groupInfoWithBLOBs.getAntifloodsetting().split(",")[0] + "," + count);
            second = groupInfoWithBLOBs.getAntifloodsetting().split(",")[0];
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                infoCount = count;
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("2##floodInfoCount2%%3##floodInfoCount3%%4##floodInfoCount4%%5##floodInfoCount5");
        keywordsButtons.add("6##floodInfoCount6%%7##floodInfoCount7%%8##floodInfoCount8%%9##floodInfoCount9");
        keywordsButtons.add("10##floodInfoCount10%%12##floodInfoCount12%%15##floodInfoCount15%%20##floodInfoCount20");
        keywordsButtons.add("🔙返回##openAntiFloodFlag");
        keywordsFormat.setReplyText("当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n\n👉目前：" + "<b>" + second + "</b>秒内发送" + "<b>" + infoCount + "</b>条消息会触发反刷屏。");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
    }
}
