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
public class NightModeAndReport {

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

    public void hadleCallBack(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🌙打开/关闭夜间值守##changeNightModeFlag%%🔔打开/关闭通知Admin##reportToAdmin");
        keywordsButtons.add("🧹清理无用指令/通知##clearCommand%%🚫反频道马甲模式##spamChannelBot");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间模式状态：<b>" + groupInfoWithBLOBs.getNightmodeflag() + "</b>\n当前通知Admin状态：<b>" + groupInfoWithBLOBs.getReportflag() + "</b>\n当前清理指令/通知状态：<b>" + groupInfoWithBLOBs.getClearinfoflag() + "</b>\n当前反频道马甲模式状态：<b>" + groupInfoWithBLOBs.getChannelspamflag() + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void reportToAdmin(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String reportFlag = "";
        String text = "";
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        if ("close".equals(groupInfoWithBLOBs.getReportflag())) {
            groupInfoWithBLOBs1.setReportflag("open");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                reportFlag = "open";
                text = "✅通知Admin模式已打开";
            }
        } else {
            groupInfoWithBLOBs1.setReportflag("close");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                reportFlag = "close";
                text = "❗通知Admin模式已关闭";
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🌙打开/关闭夜间值守##changeNightModeFlag%%🔔打开/关闭通知Admin##reportToAdmin");
        keywordsButtons.add("🧹清理无用指令/通知##clearCommand%%🚫反频道马甲模式##spamChannelBot");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text + "\n当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间模式状态：<b>" + groupInfoWithBLOBs.getNightmodeflag() + "</b>\n当前通知Admin状态：<b>" + reportFlag + "</b>\n当前清理指令/通知状态：<b>" + groupInfoWithBLOBs.getClearinfoflag() + "</b>\n当前反频道马甲模式状态：<b>" + groupInfoWithBLOBs.getChannelspamflag() + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void clearCommand(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String clearFlag = "";
        String text = "";
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        if ("close".equals(groupInfoWithBLOBs.getClearinfoflag())) {
            groupInfoWithBLOBs1.setClearinfoflag("open");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                clearFlag = "open";
                text = "✅清理指令模式已打开";
            }
        } else {
            groupInfoWithBLOBs1.setClearinfoflag("close");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                clearFlag = "close";
                text = "❗清理指令模式已关闭";
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🌙打开/关闭夜间值守##changeNightModeFlag%%🔔打开/关闭通知Admin##reportToAdmin");
        keywordsButtons.add("🧹清理无用指令/通知##clearCommand%%🚫反频道马甲模式##spamChannelBot");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text + "\n当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间模式状态：<b>" + groupInfoWithBLOBs.getNightmodeflag() + "</b>\n当前通知Admin状态：<b>" + groupInfoWithBLOBs.getReportflag() + "</b>\n当前清理指令/通知状态：<b>" + clearFlag + "</b>\n当前反频道马甲模式状态：<b>" + groupInfoWithBLOBs.getChannelspamflag() + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void changeNightModeFlag(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
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
                    handleOption.ruleHandle(sender, addRuleCacheMap.getGroupIdForUser(userId), keyWords);
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
        keywordsButtons.add("🌙打开/关闭夜间值守##changeNightModeFlag%%🔔打开/关闭通知Admin##reportToAdmin");
        keywordsButtons.add("🧹清理无用指令/通知##clearCommand%%🚫反频道马甲模式##spamChannelBot");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text+"\n当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间模式状态：<b>" + nightFlag + "</b>\n当前通知Admin状态：<b>" + groupInfoWithBLOBs.getReportflag()+ "</b>\n当前清理指令/通知状态：<b>" + groupInfoWithBLOBs.getClearinfoflag()+ "</b>\n当前反频道马甲模式状态：<b>" + groupInfoWithBLOBs.getChannelspamflag() + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void spamChannelBot(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String spamFlag = "";
        String text = "";
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        if ("close".equals(groupInfoWithBLOBs.getChannelspamflag())) {
            groupInfoWithBLOBs1.setChannelspamflag("open");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                spamFlag = "open";
                text = "✅反频道马甲模式已打开";
            }
        } else {
            groupInfoWithBLOBs1.setChannelspamflag("close");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                spamFlag = "close";
                text = "❗反频道马甲模式已关闭";
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🌙打开/关闭夜间值守##changeNightModeFlag%%🔔打开/关闭通知Admin##reportToAdmin");
        keywordsButtons.add("🧹清理无用指令/通知##clearCommand%%🚫反频道马甲模式##spamChannelBot");
        keywordsButtons.add("◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text + "\n当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前夜间模式状态：<b>" + groupInfoWithBLOBs.getNightmodeflag() + "</b>\n当前通知Admin状态：<b>" + groupInfoWithBLOBs.getReportflag() + "</b>\n当前清理指令/通知状态：<b>" + groupInfoWithBLOBs.getClearinfoflag() + "</b>\n当前反频道马甲模式状态：<b>" + spamFlag + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


}
