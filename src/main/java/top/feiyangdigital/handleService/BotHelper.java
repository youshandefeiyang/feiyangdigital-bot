package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.callBack.CommonCallBack;
import top.feiyangdigital.callBack.DeleteRuleCallBack.SetDeleteView;
import top.feiyangdigital.callBack.replyRuleCallBack.SetAutoReplyMenu;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.DeleteGropuRuleMapEntity;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.DeleteGropuRuleMap;
import top.feiyangdigital.utils.KeywordFileSender;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;
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

    public void sendAdminButton(AbsSender sender, Update update) {
        String url = String.format("https://t.me/%s?start=_%s", BaseInfo.getBotName(), update.getMessage().getChatId().toString());
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
        keywordsButtons.add("📝自动回复##autoReply%%⚪️功能占位##zhanwei");
        keywordsButtons.add("👨🏻‍💻仓库地址$$https://github.com/youshandefeiyang/feiyangdigital-bot%%👥官方群组$$https://t.me/feiyangdigital");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前可输入状态：<b>" + addRuleCacheMap.getKeywordsFlagForUser(userId) + "</b>\n⚡️请选择一个操作!⚡️");
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
        if (callbackData != null && !callbackData.isEmpty()) {
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(callbackQuery.getId());
            switch (callbackData) {
                case "autoReply":
                    setAutoReplyMenu.hadleCallBack(sender, update);
                    break;
                case "zhanwei":
                    answer.setText("正在开发中!");
                    try {
                        sender.execute(answer);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "addReplyRule":
                    setAutoReplyMenu.addReplyRule(sender, update);
                    break;
                case "selAllReplyRules":
                    keywordFileSender.sendKeywordsFile(sender,update);
                    break;
                case "selAndDeleteReplyRule":
                    setDeleteView.deleteRuleView(sender, update);
                    break;
                case "backToAutoReply":
                    setAutoReplyMenu.backToAutoReply(sender, update);
                    break;
                case "deleteBackToAutoReply":
                    setDeleteView.deleteBackToAutoReply(sender, update);
                    break;
                case "backMainMenu":
                    commonCallBack.backMainMenu(sender, update);
                    break;
                case "closeMenu":
                    timerDelete.deletePrivateMessageImmediately(sender, update);
                    break;
                case "close":
                    timerDelete.deletePrivateUsualMessageImmediately(sender,update);
                    break;
                default:
            }
            if (deleteGropuRuleMap.getGroupRuleMapSize() > 0) {
                String chatId = deleteRuleCacheMap.getGroupIdForUser(update.getCallbackQuery().getFrom().getId().toString());
                String longUuid = deleteGropuRuleMap.getAllRulesFromGroupId(chatId).getShortUuidToFullUuidMap().get(callbackData);
                if (longUuid != null && callbackData.equals(longUuid.substring(0, 5))) {
                    DeleteGropuRuleMapEntity deleteGropuRuleMapEntity = new DeleteGropuRuleMapEntity(deleteGropuRuleMap);
                    GroupInfoWithBLOBs groupInfoWithBLOBs = new GroupInfoWithBLOBs();
                    groupInfoWithBLOBs.setKeywords(deleteGropuRuleMapEntity.removeRuleAndAssembleString(chatId, longUuid).trim());
                    if (groupInfoService.updateAdminListByGroupId(groupInfoWithBLOBs, chatId)) {
                        setDeleteView.deleteRuleSuccessCallBack(sender,update);
                    }

                }
            }
        }

    }

}
