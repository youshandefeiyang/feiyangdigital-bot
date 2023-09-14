package top.feiyangdigital.utils.groupCaptch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AdminAllow {
    @Autowired
    private RestrictOrUnrestrictUser restrictOrUnrestrictUser;

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private CaptchaManager captchaManager;

    @Autowired
    private CaptchaManagerCacheMap captchaManagerCacheMap;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private SendContent sendContent;

    @Autowired
    private GroupMessageIdCacheMap groupMessageIdCacheMap;

    public void allow(AbsSender sender, String firstName, Long userId, String chatId, Integer messageId, AnswerCallbackQuery answer) {
        restrictOrUnrestrictUser.unrestrictUser(sender, userId, chatId);
        answer.setText("用户已被手动解禁");
        timerDelete.deleteByMessageIdImmediately(sender, chatId, messageId);
        captchaManager.clearMappingsForUser(userId.toString());
        captchaManagerCacheMap.clearMappingsForUser(userId.toString(), chatId);
        try {
            sender.execute(answer);
            GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(chatId);
            if (groupInfoWithBLOBs != null && "open".equals(groupInfoWithBLOBs.getIntogroupwelcomeflag())) {
                if (groupMessageIdCacheMap.getGroupMessageId(chatId.toString()) != null) {
                    timerDelete.deleteByMessageIdImmediately(sender, chatId.toString(), groupMessageIdCacheMap.getGroupMessageId(chatId.toString()));
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
                                    .replaceAll("@userId", String.format("<b><a href=\"tg://user?id=%d\">%s</a></b>", userId, firstName))
                                    .replaceAll("@groupName", String.format("<b>%s</b>", groupInfoWithBLOBs.getGroupname()));
                            newKeyFormat.setReplyText(text);
                            SendMessage sendMessage = sendContent.createGroupMessage(chatId.toString(), newKeyFormat, "html");
                            sendMessage.setDisableWebPagePreview(true);
                            String msgId = timerDelete.sendTimedMessage(sender, sendMessage, Integer.parseInt(currentMap.get("DelWelcome")));
                            groupMessageIdCacheMap.setGroupMessageId(chatId.toString(), Integer.valueOf(msgId));
                        }
                    }
                }

            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
