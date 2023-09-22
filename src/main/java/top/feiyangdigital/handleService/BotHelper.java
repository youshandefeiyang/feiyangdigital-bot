package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.callBack.CommonCallBack;
import top.feiyangdigital.callBack.deleteRuleCallBack.SetDeleteView;
import top.feiyangdigital.callBack.groupSetting.SetGroupSettingView;
import top.feiyangdigital.callBack.replyRuleCallBack.SetAutoReplyMenu;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.DeleteGropuRuleMapEntity;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.*;
import top.feiyangdigital.utils.groupCaptch.AdminAllow;
import top.feiyangdigital.utils.groupCaptch.CaptchaManagerCacheMap;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.ruleCacheMap.DeleteRuleCacheMap;

import java.util.ArrayList;
import java.util.List;

@Service
public class BotHelper {

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private SetAutoReplyMenu setAutoReplyMenu;

    @Autowired
    private SetDeleteView setDeleteView;

    @Autowired
    private SendContent sendContent;

    @Autowired
    private CommonCallBack commonCallBack;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    @Autowired
    private DeleteRuleCacheMap deleteRuleCacheMap;

    @Autowired
    private DeleteGropuRuleMap deleteGropuRuleMap;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private KeywordFileSender keywordFileSender;

    @Autowired
    private AdminAllow adminAllow;

    @Autowired
    private SetGroupSettingView setGroupSettingView;

    @Autowired
    private AdminList adminList;

    @Autowired
    private CaptchaManagerCacheMap captchaManagerCacheMap;

    public void sendAdminButton(AbsSender sender, Update update) {
        String url = String.format("https://t.me/%s?start=_groupId%s", BaseInfo.getBotName(), update.getMessage().getChatId().toString());
        List<String> keywordsButtons = new ArrayList<>();
        keywordsButtons.add("🤖Bot设置$$" + url);
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsFormat.setReplyText("点击跳转至群组设置：");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        timerDelete.sendTimedMessage(sender, sendContent.createResponseMessage(update, keywordsFormat, "def"), 10);
    }


    public void sendInlineKeyboard(AbsSender sender, Update update) {
        String userId = update.getMessage().getFrom().getId().toString();

        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("📝自动回复##autoReply%%⚙️群组设置##groupSetting");
        keywordsButtons.add("👨🏻‍💻仓库地址$$https://github.com/youshandefeiyang/feiyangdigital-bot%%👥官方群组$$https://t.me/feiyangdigital");
        keywordsButtons.add("🔮打开/关闭AI##aiOption");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前可输入状态：<b>" + addRuleCacheMap.getKeywordsFlagForUser(userId) + "</b>\n当前AI状态：<b>" + addRuleCacheMap.getAiFlagForUser(userId) + "</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.createResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void handleCallbackQuery(AbsSender sender, Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        if (callbackData == null || callbackData.isEmpty()) return;

        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackQuery.getId());
        switch (callbackData) {
            case "changeIntoGroupUserNameCheckStatus":
                setGroupSettingView.changeIntoGroupUserNameCheckStatus(sender, update);
                return;
            case "changeGroupWelcomeStatus":
                setGroupSettingView.changeGroupWelcomeStatus(sender, update);
                return;
            case "autoReply":
                setAutoReplyMenu.hadleCallBack(sender, update);
                return;
            case "groupSetting":
                setGroupSettingView.hadleCallBack(sender, update);
                return;
            case "changeGroupCheckStatus":
                setGroupSettingView.changeGroupCheckStatus(sender, update);
                return;
            case "addReplyRule":
                setAutoReplyMenu.addReplyRule(sender, update);
                return;
            case "selAllReplyRules":
                keywordFileSender.sendKeywordsFile(sender, update);
                return;
            case "selAndDeleteReplyRule":
                setDeleteView.deleteRuleView(sender, update);
                return;
            case "backToAutoReply":
                setAutoReplyMenu.backToAutoReply(sender, update);
                return;
            case "deleteBackToAutoReply":
                setDeleteView.deleteBackToAutoReply(sender, update);
                return;
            case "backMainMenu":
                commonCallBack.backMainMenu(sender, update);
                return;
            case "closeMenu":
                timerDelete.deletePrivateMessageImmediately(sender, update);
                return;
            case "aiOption":
                commonCallBack.aiOption(sender,update);
                return;
            case "close":
                timerDelete.deletePrivateUsualMessageImmediately(sender, update);
                return;
            default:
        }

        if (callbackData.startsWith("adminUnrestrict")) {

            for (ChatMember admin : adminList.getAdmins(sender, callbackQuery.getMessage().getChatId().toString())) {
                if ("GroupAnonymousBot".equals(callbackQuery.getFrom().getUserName()) || admin.getUser().getId().equals(callbackQuery.getFrom().getId())) {
                    adminAllow.allow(sender, Long.valueOf(callbackData.substring(15)), callbackQuery.getMessage().getChatId().toString(), captchaManagerCacheMap.getMessageIdForUser(callbackData.substring(15), callbackQuery.getMessage().getChatId().toString()), answer,true);
                    return;
                }
            }
            answer.setText("❌你无权执行该操作！");
            try {
                sender.execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        if (callbackData.startsWith("adminUnBan")){
            for (ChatMember admin : adminList.getAdmins(sender, callbackQuery.getMessage().getChatId().toString())) {
                if ("GroupAnonymousBot".equals(callbackQuery.getFrom().getUserName()) || admin.getUser().getId().equals(callbackQuery.getFrom().getId())) {
                    adminAllow.allowUnBan(sender, Long.valueOf(callbackData.substring(10)), callbackQuery.getMessage().getChatId().toString(),captchaManagerCacheMap.getMessageIdForUser(callbackData.substring(10), callbackQuery.getMessage().getChatId().toString()), answer);
                    return;
                }
            }
            answer.setText("❌你无权执行该操作！");
            try {
                sender.execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        if (callbackData.startsWith("adminUnmute")){
            for (ChatMember admin : adminList.getAdmins(sender, callbackQuery.getMessage().getChatId().toString())) {
                if ("GroupAnonymousBot".equals(callbackQuery.getFrom().getUserName()) || admin.getUser().getId().equals(callbackQuery.getFrom().getId())) {
                    adminAllow.allow(sender, Long.valueOf(callbackData.substring(11)), callbackQuery.getMessage().getChatId().toString(),captchaManagerCacheMap.getMessageIdForUser(callbackData.substring(11), callbackQuery.getMessage().getChatId().toString()), answer,false);
                    return;
                }
            }
            answer.setText("❌你无权执行该操作！");
            try {
                sender.execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        if (deleteGropuRuleMap.getGroupRuleMapSize() > 0) {
            String chatId = deleteRuleCacheMap.getGroupIdForUser(update.getCallbackQuery().getFrom().getId().toString());
            String longUuid = deleteGropuRuleMap.getAllRulesFromGroupId(chatId).getShortUuidToFullUuidMap().get(callbackData);
            if (longUuid != null && callbackData.equals(longUuid.substring(0, 5))) {
                DeleteGropuRuleMapEntity deleteGropuRuleMapEntity = new DeleteGropuRuleMapEntity(deleteGropuRuleMap);
                GroupInfoWithBLOBs groupInfoWithBLOBs = new GroupInfoWithBLOBs();
                groupInfoWithBLOBs.setKeywords(deleteGropuRuleMapEntity.removeRuleAndAssembleString(chatId, longUuid).trim());
                if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs, chatId)) {
                    setDeleteView.deleteRuleSuccessCallBack(sender, update);
                }

            }
        }
    }


}
