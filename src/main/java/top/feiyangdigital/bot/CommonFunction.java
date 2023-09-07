package top.feiyangdigital.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.callBack.DeleteRuleCallBack.DeleteSingleRuleByKeyWord;
import top.feiyangdigital.callBack.replyRuleCallBack.AddAutoReplyRule;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.handleService.BotFirstIntoGroup;
import top.feiyangdigital.handleService.BotHelper;
import top.feiyangdigital.handleService.MessageHandle;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.*;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.ruleCacheMap.DeleteRuleCacheMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CommonFunction {

    @Autowired
    private CheckUser checkUser;

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private BotFirstIntoGroup botFirstIntoGroup;

    @Autowired
    private BotHelper botHelper;

    @Autowired
    private AdminList adminList;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private MatchList matchList;

    @Autowired
    private SendContent sendContent;

    @Autowired
    private MessageHandle messageHandle;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    @Autowired
    private DeleteRuleCacheMap deleteRuleCacheMap;

    @Autowired
    private AddAutoReplyRule addAutoReplyRule;

    @Autowired
    private DeleteSingleRuleByKeyWord deleteSingleRuleByKeyWord;

    public void mainFunc(AbsSender sender, Update update){
        if (update.hasMessage() && update.getMessage().getChat().isUserChat()) {
            if (update.getMessage().getText().contains("start _")) {
                GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(update.getMessage().getText().split("_")[1]);
                if (groupInfoWithBLOBs.getOwnerandanonymousadmins().contains(update.getMessage().getFrom().getId().toString())) {
                    addRuleCacheMap.updateUserMapping(update.getMessage().getFrom().getId().toString(), groupInfoWithBLOBs.getGroupid(), groupInfoWithBLOBs.getGroupname(), groupInfoWithBLOBs.getKeywordsflag());
                    deleteRuleCacheMap.updateUserMapping(update.getMessage().getFrom().getId().toString(), groupInfoWithBLOBs.getGroupid(), groupInfoWithBLOBs.getGroupname(), groupInfoWithBLOBs.getDeletekeywordflag());
                    botHelper.sendInlineKeyboard(sender, update);
                }
            } else if ("/start".equals(update.getMessage().getText())) {
                String url = String.format("https://t.me/%s?startgroup=start", BaseInfo.getBotName());
                List<String> keywordsButtons = new ArrayList<>();
                KeywordsFormat keywordsFormat = new KeywordsFormat();
                keywordsButtons.add("➕将Bot加入群组$$" + url);
                keywordsButtons.add("❌关闭菜单##close");
                keywordsFormat.setReplyText("<b>GitHub地址：</b><b><a href='https://github.com/youshandefeiyang/feiyangdigital-bot'>点击查看</a></b>\n<b>官方群组：</b><b><a href='https://t.me/feiyangdigital'>点击加入</a></b>\n");
                keywordsFormat.setKeywordsButtons(keywordsButtons);
                try {
                    sender.execute(sendContent.createResponseMessage(update, keywordsFormat, "html"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                String userId = update.getMessage().getFrom().getId().toString();
                if ("allow".equals(addRuleCacheMap.getKeywordsFlagForUser(userId))) {
                    addAutoReplyRule.addNewRule(sender, update);
                } else if ("candelete".equals(deleteRuleCacheMap.getDeleteKeywordFlagMap(userId))) {
                    deleteSingleRuleByKeyWord.DeleteOption(sender, update);
                }
            }

        }

        if (update.hasMessage() && (update.getMessage().getChat().isGroupChat() || update.getMessage().getChat().isSuperGroupChat())) {
            List<KeywordsFormat> keywordsFormatList = matchList.createBanKeyDeleteOptionList(update);
            if (keywordsFormatList != null) {
                messageHandle.processUserMessage(sender, update, keywordsFormatList);
            }
            if (("GroupAnonymousBot".equals(update.getMessage().getFrom().getUserName()) || checkUser.isChatOwner(sender, update)) && ("/setbot".equals(update.getMessage().getText()) || ("/setbot@" + BaseInfo.getBotName()).equals(update.getMessage().getText()))) {
                GroupInfoWithBLOBs groupInfo = new GroupInfoWithBLOBs();
                groupInfo.setOwnerandanonymousadmins(adminList.fetchHighAdminList(sender, update));
                groupInfo.setGroupname(update.getMessage().getChat().getTitle());
                groupInfo.setSettingtimestamp(String.valueOf(new Date().getTime()));
                groupInfoService.updateAdminListByGroupId(groupInfo, update.getMessage().getChatId().toString());
                botHelper.sendAdminButton(sender, update);
            } else if ("/setbot".equals(update.getMessage().getText()) || ("/setbot@" + BaseInfo.getBotName()).equals(update.getMessage().getText())) {
                timerDelete.sendTimedMessage(sender, sendContent.messageText(update, "你没有权限执行此命令"), 10);
            }
        }

        if (update.hasMessage() && (update.getMessage().getNewChatMembers() != null && !update.getMessage().getNewChatMembers().isEmpty())) {
            botFirstIntoGroup.handleMessage(sender,update.getMessage());
        }


        if (update.hasCallbackQuery()) {
            botHelper.handleCallbackQuery(sender, update);
        }
    }
}