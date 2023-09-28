package top.feiyangdigital.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.callBack.deleteRuleCallBack.DeleteSingleRuleByKeyWord;
import top.feiyangdigital.callBack.replyRuleCallBack.AddAutoReplyRule;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.handleService.*;
import top.feiyangdigital.scheduledTasks.HandleOption;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.aiMessageCheck.AiCheckMedia;
import top.feiyangdigital.utils.aiMessageCheck.AiCheckMessage;
import top.feiyangdigital.utils.groupCaptch.*;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.ruleCacheMap.DeleteRuleCacheMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommonFunction {
    private final Map<Long, Boolean> groupFlags = new ConcurrentHashMap<>();

    @Autowired
    private ClearOtherInfo clearOtherInfo;

    @Autowired
    private AiCheckMessage aiCheckMessage;

    @Autowired
    private AiCheckMedia aiCheckMedia;

    @Autowired
    private NewMemberIntoGroup newMemberIntoGroup;

    @Autowired
    private BotHelper botHelper;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private SendContent sendContent;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    @Autowired
    private DeleteRuleCacheMap deleteRuleCacheMap;

    @Autowired
    private AddAutoReplyRule addAutoReplyRule;

    @Autowired
    private DeleteSingleRuleByKeyWord deleteSingleRuleByKeyWord;

    @Autowired
    private CaptchaGenerator captchaGenerator;

    @Autowired
    private CaptchaManager captchaManager;

    @Autowired
    private BotFirstIntoGroup botFirstIntoGroup;

    @Autowired
    private BanOrUnBan banOrUnBan;

    @Autowired
    private RestrictOrUnrestrictUser restrictOrUnrestrictUser;

    @Autowired
    private SetBot setBot;

    @Autowired
    private HandleOption handleOption;

    @Autowired
    private NightMode nightMode;

    @Autowired
    private ReportToOwner reportToOwner;

    @Autowired
    private AntiFloodService antiFloodService;

    @Autowired
    private SpamChannelBotService spamChannelBotService;

    private boolean checkTextMessage(AbsSender sender, Update update) throws TelegramApiException {
        if (setBot.adminSetBot(sender, update)) {
            return true;
        } else if (banOrUnBan.banOption(sender, update)) {
            return true;
        } else if (banOrUnBan.dbanOption(sender, update)) {
            return true;
        } else if (banOrUnBan.unBanOption(sender, update)) {
            return true;
        } else if (restrictOrUnrestrictUser.muteOption(sender, update)) {
            return true;
        } else return restrictOrUnrestrictUser.unMuteOption(sender, update);
    }

    public void mainFunc(AbsSender sender, Update update) throws TelegramApiException {

        if ((update.hasMessage() && (update.getMessage().getChat().isGroupChat() || update.getMessage().getChat().isSuperGroupChat()))
                || (update.hasEditedMessage() && (update.getEditedMessage().getChat().isGroupChat() || update.getEditedMessage().getChat().isSuperGroupChat()))
        ) {
            if (!update.hasMessage() && update.hasEditedMessage()) {
                update.setMessage(update.getEditedMessage());
            }
            GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(update.getMessage().getChatId().toString());
            if (groupInfoWithBLOBs != null) {
                Long chatId = update.getMessage().getChatId();
                groupFlags.putIfAbsent(chatId, true);
                Boolean flag = groupFlags.get(chatId);
                if (flag != null && flag) {
                    String keyWords = groupInfoWithBLOBs.getKeywords();
                    if (StringUtils.hasText(keyWords)) {
                        handleOption.ruleHandle(sender, chatId.toString(), keyWords);
                    }
                    groupFlags.put(chatId, false);
                }
                if ("open".equals(groupInfoWithBLOBs.getCansendmediaflag())) {
                    if (nightMode.deleteMedia(sender, update)) {
                        return;
                    }
                }
            }
            if (spamChannelBotService.checkChannelOption(sender, update)) {
                checkTextMessage(sender, update);
                return;
            }
            antiFloodService.checkFlood(sender, update);
            if (update.getMessage().hasText()) {
                if (checkTextMessage(sender, update)) {
                    return;
                } else if (reportToOwner.haddle(sender, update)) {
                    return;
                }
                aiCheckMessage.checkMessage(sender, update);
                clearOtherInfo.clearBotCommand(sender, update);
                return;
            }
            aiCheckMedia.checkMedia(sender, update);
            clearOtherInfo.clearAdviceInfo(sender, update);
        }

        if (update.hasMessage() && update.getMessage().getText() != null && update.getMessage().getChat().isUserChat()) {

            if (update.getMessage().getText().contains("start _groupId")) {
                GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(update.getMessage().getText().split("_")[1].substring(7));
                if (groupInfoWithBLOBs.getOwnerandanonymousadmins().contains(update.getMessage().getFrom().getId().toString())) {
                    addRuleCacheMap.updateUserMapping(update.getMessage().getFrom().getId().toString(), groupInfoWithBLOBs.getGroupid(), groupInfoWithBLOBs.getGroupname(), groupInfoWithBLOBs.getKeywordsflag(), groupInfoWithBLOBs.getAiflag(), groupInfoWithBLOBs.getCrontabflag());
                    deleteRuleCacheMap.updateUserMapping(update.getMessage().getFrom().getId().toString(), groupInfoWithBLOBs.getGroupid(), groupInfoWithBLOBs.getGroupname(), groupInfoWithBLOBs.getDeletekeywordflag());
                    botHelper.sendInlineKeyboard(sender, update);
                }
            } else if (update.getMessage().getText().contains("start _intoGroupInfo")) {
                String[] idGroup = update.getMessage().getText().split("_")[1].substring(13).split("and");
                String chatId = idGroup[0];
                String userId = idGroup[1];
                String currentChatId = update.getMessage().getChatId().toString();
                String firstName = update.getMessage().getChat().getFirstName();
                if (update.getMessage().getFrom().getId().toString().equals(userId) && "open".equals(groupInfoService.selAllByGroupId(chatId).getIntogroupcheckflag())) {

                    captchaGenerator.sendCaptcha(sender, update.getMessage().getFrom().getId(), chatId, currentChatId, firstName);
                } else {
                    sender.execute(sendContent.messageText(update, "❌这不是你的验证"));
                }
            } else if ("/start".equals(update.getMessage().getText())) {
                String url = String.format("https://t.me/%s?startgroup=start", BaseInfo.getBotName());
                List<String> keywordsButtons = new ArrayList<>();
                KeywordsFormat keywordsFormat = new KeywordsFormat();
                keywordsButtons.add("➕将Bot加入群组$$" + url);
                keywordsButtons.add("❌关闭菜单##close");
                keywordsFormat.setReplyText("<b>GitHub地址：</b><b><a href='https://github.com/youshandefeiyang/feiyangdigital-bot'>点击查看</a></b>\n<b>官方群组：</b><b><a href='https://t.me/feiyangdigital'>点击加入</a></b>\n");
                keywordsFormat.setKeywordsButtons(keywordsButtons);
                sender.execute(sendContent.createResponseMessage(update, keywordsFormat, "html"));
            } else {
                String userId = update.getMessage().getFrom().getId().toString();
                if ("allow".equals(addRuleCacheMap.getKeywordsFlagForUser(userId))) {
                    addAutoReplyRule.addNewRule(sender, update);
                } else if ("candelete".equals(deleteRuleCacheMap.getDeleteKeywordFlagMap(userId))) {
                    deleteSingleRuleByKeyWord.DeleteOption(sender, update);
                } else if (StringUtils.hasText(captchaManager.getAnswerForUser(userId))) {
                    captchaGenerator.answerReplyhandle(sender, update);

                }
            }

        }


        //检测新入群用户且状态正常的用户
        if (update.getChatMember() != null && "left".equals(update.getChatMember().getOldChatMember().getStatus()) && "member".equals(update.getChatMember().getNewChatMember().getStatus())) {

            newMemberIntoGroup.handleMessage(sender, update, null);
        }

        //检测新入群Bot
        if (update.hasMessage() && (update.getMessage().getNewChatMembers() != null && !update.getMessage().getNewChatMembers().isEmpty())) {

            botFirstIntoGroup.handleMessage(sender, update);
        }

        if (update.hasCallbackQuery()) {
            botHelper.handleCallbackQuery(sender, update);
        }
    }

}
