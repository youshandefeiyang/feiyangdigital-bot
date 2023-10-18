package top.feiyangdigital.callBack.otherGroupSetting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.scheduledTasks.HandleOption;
import top.feiyangdigital.scheduledTasks.SchedulerService;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;

import java.util.ArrayList;
import java.util.List;

@Component
public class NightSetting {

    @Autowired
    private SendContent sendContent;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private HandleOption handleOption;

    public void hadleCallBack(AbsSender sender, Update update) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("✳️临时开启夜间值守模式##openCanSendMediaFlag%%❎临时关闭夜间值守模式##closeCanSendMediaFlag");
        keywordsButtons.add("👑打开/关闭夜间值守任务##changeNightModeFlag");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间值守模式状态：<b>" + groupInfoWithBLOBs.getCansendmediaflag() + "</b>\n当前夜间值守任务状态：<b>" + groupInfoWithBLOBs.getNightmodeflag() + "</b>\n<b>⚠️夜间值守任务开关是针对具体的定时任务，如果你想立即打开/关闭夜间值守，请点击第一排的两个临时开关按钮！" + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
    }

    public void openCanSendMediaFlag(AbsSender sender, Update update) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        String text = "";
        String canSendMediaFlag = groupInfoWithBLOBs.getCansendmediaflag();
        if ("close".equals(canSendMediaFlag)) {
            groupInfoWithBLOBs1.setCansendmediaflag("open");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                text += "✅夜间值守模式已打开\n";
                canSendMediaFlag = "open";
            }
        } else {
            text += "✅夜间值守模式已为开启状态\n";
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("✳️临时开启夜间值守模式##openCanSendMediaFlag%%❎临时关闭夜间值守模式##closeCanSendMediaFlag");
        keywordsButtons.add("👑打开/关闭夜间值守任务##changeNightModeFlag");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text + "当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间值守模式状态：<b>" + canSendMediaFlag + "</b>\n当前夜间值守任务状态：<b>" + groupInfoWithBLOBs.getNightmodeflag() + "</b>\n<b>⚠️夜间值守任务开关是针对具体的定时任务，如果你想立即打开/关闭夜间值守，请点击第一排的两个临时开关按钮！" + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
    }


    public void closeCanSendMediaFlag(AbsSender sender, Update update) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        String text = "";
        String canSendMediaFlag = groupInfoWithBLOBs.getCansendmediaflag();
        if ("open".equals(canSendMediaFlag)) {
            groupInfoWithBLOBs1.setCansendmediaflag("close");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                text += "❗️夜间值守模式已关闭\n";
                canSendMediaFlag = "close";
            }
        } else {
            text += "❗️夜间值守模式已为关闭状态\n";
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("✳️临时开启夜间值守模式##openCanSendMediaFlag%%❎临时关闭夜间值守模式##closeCanSendMediaFlag");
        keywordsButtons.add("👑打开/关闭夜间值守任务##changeNightModeFlag");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text + "当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间值守模式状态：<b>" + canSendMediaFlag + "</b>\n当前夜间值守任务状态：<b>" + groupInfoWithBLOBs.getNightmodeflag() + "</b>\n<b>⚠️夜间值守任务开关是针对具体的定时任务，如果你想立即打开/关闭夜间值守，请点击第一排的两个临时开关按钮！" + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
    }

    public void changeNightModeFlag(AbsSender sender, Update update) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String groupName = groupInfoWithBLOBs.getGroupname();
        String keyWords = groupInfoWithBLOBs.getKeywords();
        String nightFlag = "";
        String text = "";
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        if ("close".equals(groupInfoWithBLOBs.getNightmodeflag())) {
            groupInfoWithBLOBs1.setNightmodeflag("open");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                nightFlag = "open";
                text = "✅夜间值守模式已打开";
                if (StringUtils.hasText(keyWords)) {
                    handleOption.ruleHandle(sender, addRuleCacheMap.getGroupIdForUser(userId),groupName, keyWords);
                }
            }
        } else {
            groupInfoWithBLOBs1.setNightmodeflag("close");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                nightFlag = "close";
                text = "❗夜间值守模式已关闭";
                schedulerService.clearJobsExcludingGroupPrefix("OnlySendMessage");
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("✳️临时开启夜间值守模式##openCanSendMediaFlag%%❎临时关闭夜间值守模式##closeCanSendMediaFlag");
        keywordsButtons.add("👑打开/关闭夜间值守任务##changeNightModeFlag");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text + "\n当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间值守模式状态：<b>" + groupInfoWithBLOBs.getCansendmediaflag() + "</b>\n当前夜间值守任务状态：<b>" + nightFlag + "</b>\n<b>⚠️夜间值守任务开关是针对具体的定时任务，如果你想立即打开/关闭夜间值守，请点击第一排的两个临时开关按钮！" + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
    }


}
