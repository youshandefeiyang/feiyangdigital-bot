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
public class AntiFlood {
    @Autowired
    private SendContent sendContent;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    public void hadleCallBack(AbsSender sender, Update update) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🗣️打开防刷屏模式设置##openAntiFloodFlag%%🤐关闭防刷屏模式##closeAntiFloodFlag");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前群组开启防刷屏模式状态：<b>" + groupInfoWithBLOBs.getAntifloodflag() + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
    }

    public void openAntiFloodFlag(AbsSender sender, Update update) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String second = "";
        String infoCount = "";
        if (StringUtils.hasText(groupInfoWithBLOBs.getAntifloodsetting())) {
            second = groupInfoWithBLOBs.getAntifloodsetting().split(",")[0];
            infoCount = groupInfoWithBLOBs.getAntifloodsetting().split(",")[1];
        }
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        String text = "";
        String antiFloodFlag = groupInfoWithBLOBs.getAntifloodflag();
        if ("close".equals(groupInfoWithBLOBs.getAntifloodflag())) {
            groupInfoWithBLOBs1.setAntifloodflag("open");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                text += "✅️防刷屏模式已打开\n";
                antiFloodFlag = "open";
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🗒️设置消息条数##setFloodInfoCount%%🕒设置反刷屏时间##setFloodTime");
        keywordsButtons.add("🤐关闭防刷屏模式##closeAntiFloodFlag%%◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text + "当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前群组开启防刷屏模式状态：<b>" + antiFloodFlag + "</b>\n\n👉目前：" + "<b>" + second + "</b>秒内发送" + "<b>" + infoCount + "</b>条消息会触发反刷屏。");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
    }

    public void closeAntiFloodFlag(AbsSender sender, Update update) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        String text = "";
        String antiFloodFlag = groupInfoWithBLOBs.getAntifloodflag();
        if ("open".equals(groupInfoWithBLOBs.getAntifloodflag())) {
            groupInfoWithBLOBs1.setAntifloodflag("close");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                text += "❗️防刷屏模式已关闭\n";
                antiFloodFlag = "close";
            }
        } else {
            text += "❗️防刷屏模式已为关闭状态\n";
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🗣️打开防刷屏模式##openAntiFloodFlag%%🤐关闭防刷屏模式##closeAntiFloodFlag");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text + "当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前群组开启防刷屏模式状态：<b>" + antiFloodFlag + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
    }


}
