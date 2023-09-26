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
public class SetFloodTime {
    @Autowired
    private SendContent sendContent;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    public void haddle(AbsSender sender, Update update, boolean mode) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String second = "";
        String infoCount = "";
        if (!mode && StringUtils.hasText(groupInfoWithBLOBs.getAntifloodsetting())) {
            second = groupInfoWithBLOBs.getAntifloodsetting().split(",")[0];
            infoCount = groupInfoWithBLOBs.getAntifloodsetting().split(",")[1];
        }
        if (mode) {
            String time = update.getCallbackQuery().getData().substring(9);
            GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
            groupInfoWithBLOBs1.setAntifloodsetting(time+","+groupInfoWithBLOBs.getAntifloodsetting().split(",")[1]);
            infoCount = groupInfoWithBLOBs.getAntifloodsetting().split(",")[1];
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1,addRuleCacheMap.getGroupIdForUser(userId))){
               second = time;
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("2##floodTime2%%3##floodTime3%%4##floodTime4%%5##floodTime5");
        keywordsButtons.add("6##floodTime6%%7##floodTime7%%8##floodTime8%%9##floodTime9");
        keywordsButtons.add("10##floodTime10%%12##floodTime12%%15##floodTime15%%20##floodTime20");
        keywordsButtons.add("🔙返回##openAntiFloodFlag");
        keywordsFormat.setReplyText("当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n\n👉目前：" + "<b>" + second + "</b>秒内发送" + "<b>" + infoCount + "</b>条消息会触发反刷屏。");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
