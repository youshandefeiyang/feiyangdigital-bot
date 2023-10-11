package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.BotRecordService;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.CheckUser;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;
import top.feiyangdigital.utils.groupCaptch.CaptchaManager;
import top.feiyangdigital.utils.groupCaptch.CaptchaManagerCacheMap;
import top.feiyangdigital.utils.groupCaptch.GroupMessageIdCacheMap;
import top.feiyangdigital.utils.groupCaptch.RestrictOrUnrestrictUser;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FollowChannelVerification implements CaptchaService {

    @Autowired
    private CheckUser checkUser;

    @Autowired
    private BotRecordService botRecordService;

    @Autowired
    private SendContent sendContent;

    @Autowired
    private CaptchaManager captchaManager;

    @Autowired
    private CaptchaManagerCacheMap captchaManagerCacheMap;

    @Autowired
    private RestrictOrUnrestrictUser restrictOrUnrestrictUser;

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private GroupMessageIdCacheMap groupMessageIdCacheMap;

    @Autowired
    private GroupInfoService groupInfoService;

    @Override
    public void sendCaptcha(AbsSender sender, Update update, String chatId) throws TelegramApiException {
        Long userId = update.getMessage().getFrom().getId();
        captchaManager.updateUserMapping(userId.toString(), chatId, "");
        String text = String.format("请 <b><a href=\"tg://user?id=%d\">%s</a></b> 首先点击 <b>订阅频道按钮</b> ，在成功<b>订阅频道</b>之后，点击<b>完成验证按钮</b>，即可在群组内正常发言！", userId, update.getMessage().getChat().getFirstName());
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("👉订阅频道$$" + checkUser.getLinkedChatInfo(sender, chatId).get("LinkedChatString") + "%%🔄完成验证##answerReplyhandle");
        keywordsFormat.setReplyText(text);
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        sender.execute(sendContent.createResponseMessage(update, keywordsFormat, "html"));
    }

    @Override
    public void answerReplyhandle(AbsSender sender, Update update) throws TelegramApiException {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        Long userId1 = update.getCallbackQuery().getFrom().getId();
        String firstName = update.getCallbackQuery().getFrom().getFirstName();
        if (captchaManager.getGroupIdForUser(userId) != null) {
            String groupId = captchaManager.getGroupIdForUser(userId);
            Integer messageId = captchaManagerCacheMap.getMessageIdForUser(userId, groupId);
            Integer attempt = captchaManagerCacheMap.getAttemptForUser(userId, groupId);
            GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(groupId);

            GetChatMember getChatMember = new GetChatMember();
            getChatMember.setChatId(checkUser.getLinkedChatInfo(sender, groupId).get("LinkedChatId"));
            getChatMember.setUserId(userId1);
            ChatMember chatMember = sender.execute(getChatMember);
            if ("member".equals(chatMember.getStatus())) {
                SendMessage message = sendContent.messageText(update, "验证通过，现在你可以在群里自由发言了");
                sender.execute(message);
                restrictOrUnrestrictUser.unrestrictUser(sender, userId1, groupId);
                botRecordService.addUserRecord(groupId, userId, String.valueOf(new Date().getTime() / 1000));
                if (groupInfoWithBLOBs != null && "open".equals(groupInfoWithBLOBs.getIntogroupwelcomeflag())) {
                    if (groupMessageIdCacheMap.getMapSize() > 0) {
                        groupMessageIdCacheMap.deleteAllMessage(sender, groupId);
                    }
                    if (StringUtils.hasText(groupInfoWithBLOBs.getKeywords()) && groupInfoWithBLOBs.getKeywords().contains("&&welcome=")) {
                        List<KeywordsFormat> keywordsFormatList = Arrays.stream(groupInfoWithBLOBs.getKeywords().split("\\n{2,}"))
                                .map(String::trim)
                                .map(KeywordsFormat::new)
                                .collect(Collectors.toList());
                        for (KeywordsFormat keywordFormat : keywordsFormatList) {
                            Map<String, String> currentMap = keywordFormat.getRuleMap();
                            if (currentMap.containsKey("DelWelcome")) {
                                KeywordsFormat newKeyFormat = new KeywordsFormat();
                                newKeyFormat.setKeywordsButtons(keywordFormat.getKeywordsButtons());
                                String text = keywordFormat.getReplyText()
                                        .replaceAll("@userId", String.format("<b><a href=\"tg://user?id=%d\">%s</a></b>", userId1, firstName))
                                        .replaceAll("@groupName", String.format("<b>%s</b>", groupInfoWithBLOBs.getGroupname()));
                                newKeyFormat.setReplyText(text);
                                SendMessage sendMessage = sendContent.createGroupMessage(groupId, newKeyFormat, "html");
                                sendMessage.setDisableWebPagePreview(true);
                                Integer msgId = timerDelete.deleteMessageImmediatelyAndNotifyAfterDelay(sender, sendMessage, groupId, messageId, userId1, Integer.parseInt(currentMap.get("DelWelcome")));
                                groupMessageIdCacheMap.setGroupMessageId(groupId, msgId);
                            }
                        }
                        return;
                    }
                }
                String text = String.format("用户 <b><a href=\"tg://user?id=%d\">%s</a></b> 验证通过，解除群组限制！", userId1, firstName);
                SendMessage notification = new SendMessage();
                notification.setChatId(groupId);
                notification.setText(text);
                notification.setParseMode(ParseMode.HTML);
                timerDelete.deleteMessageImmediatelyAndNotifyAfterDelay(sender, notification, groupId, messageId, userId1, 10);
            } else {
                if (attempt != null) {
                    captchaManagerCacheMap.updateUserMapping(userId, groupId, attempt + 1, messageId);
                    if (attempt >= 1) {
                        sender.execute(sendContent.messageText(update, "未通过验证，你的机会已经用尽！"));
                        timerDelete.deleteByMessageIdImmediately(sender, groupId, messageId);
                        captchaManager.clearMappingsForUser(userId);
                        captchaManagerCacheMap.clearMappingsForUser(userId, groupId);
                        return;
                    }
                    sender.execute(sendContent.messageText(update, "尚未关注频道，请关注频道后再点击完成验证，请再试一次，你只有两次机会，次数用尽/超时都将会永久禁言"));
                }
            }
        }
    }
}
