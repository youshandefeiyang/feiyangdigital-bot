package top.feiyangdigital.utils.aiMessageCheck;

import com.alibaba.fastjson2.JSONObject;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.BotRecord;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.handleService.MessageHandle;
import top.feiyangdigital.handleService.OpenAiApiService;
import top.feiyangdigital.sqlService.BotRecordService;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.MatchList;
import top.feiyangdigital.utils.TimerDelete;
import top.feiyangdigital.utils.groupCaptch.RestrictOrUnrestrictUser;

import java.util.List;

@Component
public class AiCheckMessage {

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private MatchList matchList;

    @Autowired
    private MessageHandle messageHandle;

    @Autowired
    private BotRecordService botRecordService;

    @Autowired
    private RestrictOrUnrestrictUser restrictOrUnrestrictUser;

    @Autowired
    private OpenAiApiService openAiApiService;

    public void checkMessage(AbsSender sender, Update update) throws TelegramApiException {
        String groupId = update.getMessage().getChatId().toString();
        String userId = update.getMessage().getFrom().getId().toString();
        Integer messageId = update.getMessage().getMessageId();
        String firstName = update.getMessage().getFrom().getFirstName();
        String content = update.getMessage().getText();
        List<KeywordsFormat> keywordsFormatList = matchList.createBanKeyDeleteOptionList(update);
        if (keywordsFormatList != null) {
            if (messageHandle.processUserMessage(sender, update, keywordsFormatList)) {
                return;
            }
        }
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(groupId);
        if (groupInfoWithBLOBs != null && "open".equals(groupInfoWithBLOBs.getAiflag()) && StringUtils.hasText(content)) {
            contentAiOption(sender, groupId, userId, firstName, messageId, content);
        }
    }

    public void contentAiOption(AbsSender sender, String groupId, String userId, String firstName, Integer messageId, String content) {
        BotRecord botRecord = botRecordService.selBotRecordByGidAndUid(groupId, userId);
        if (botRecord != null) {
            Integer violationCount = botRecord.getViolationcount();
            Integer normalCount = botRecord.getNormalcount();
            if (violationCount >= 5) {
                String text = String.format("用户 <b><a href=\"tg://user?id=%d\">%s</a></b> 已被AI检测违规超过5次，永久限制发言！", Long.valueOf(userId), firstName);
                SendMessage notification = new SendMessage();
                notification.setChatId(groupId);
                notification.setText(text);
                notification.setParseMode(ParseMode.HTML);
                timerDelete.deleteMessageImmediatelyAndNotifyAfterDelay(sender, notification, groupId, messageId, Long.valueOf(userId), 90);
                restrictOrUnrestrictUser.restrictUser(sender, Long.valueOf(userId), groupId);
                return;
            } else if (normalCount >= 5) {
                return;
            }
            List<ChatChoice> list = openAiApiService.getOpenAiAnalyzeResult(content);
            if (!list.isEmpty()) {
                JSONObject jsonObject = JSONObject.parseObject(list.get(0).getMessage().getContent());
                Integer spamChance = jsonObject.getInteger("spamChance");
                String spamReason = jsonObject.getString("spamReason");
                BotRecord botRecord1 = new BotRecord();
                if (spamChance >= 6) {
                    String text = String.format("用户 <b><a href=\"tg://user?id=%d\">%s</a></b> 已被AI检测发送违规词，判断原因如下：\n<tg-spoiler>%s</tg-spoiler>", Long.valueOf(userId), firstName, spamReason);
                    SendMessage notification = new SendMessage();
                    notification.setChatId(groupId);
                    notification.setText(text);
                    notification.setParseMode(ParseMode.HTML);
                    timerDelete.deleteMessageImmediatelyAndNotifyAfterDelay(sender, notification, groupId, messageId, Long.valueOf(userId), 90);
                    botRecord1.setViolationcount(violationCount + 1);
                } else {
                    botRecord1.setNormalcount(normalCount + 1);
                }
                botRecord1.setLastmessage(content);
                botRecordService.updateRecordByGidAndUid(groupId, userId, botRecord1);
            }
        }
    }

}
