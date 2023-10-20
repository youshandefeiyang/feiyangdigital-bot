package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class MessageHandle {

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private SendContent sendContent;

    public boolean processUserMessage(AbsSender sender, Update update, List<KeywordsFormat> keywordsList) throws TelegramApiException {
        String messageText = update.getMessage().getText();
        Long userId = update.getMessage().getFrom().getId();

        // 如果消息文本为null，直接返回，不做处理
        if (!StringUtils.hasText(messageText)) {
            return true;
        }

        for (KeywordsFormat keywordFormat : keywordsList) {
            Map<String, String> currentMap = keywordFormat.getRuleMap();
            String regex = keywordFormat.getRegex();
            Pattern pattern = Pattern.compile(regex);

            if (currentMap.containsKey("DelWelcome") || currentMap.containsKey("DelIntoGroupBan") || currentMap.containsKey("crontabOption")) {
                //不执行任何操作
            } else if (currentMap.containsKey("DeleteAfterXSeconds")) {
                if (pattern.matcher(messageText).find()) {
                    // 用户违规了
                    int deleteAfterXSeconds = Integer.parseInt(currentMap.get("DeleteAfterXSeconds"));

                    if (deleteAfterXSeconds == 0) {
                        // 立即删除
                        timerDelete.deleteMessageImmediately(sender, update);
                    } else {
                        // 使用timer删除
                        timerDelete.deleteMessageAfterDelay(sender, update, deleteAfterXSeconds);
                    }

                    if (currentMap.containsKey("DeleteReplyAfterYSeconds")) {
                        String text = String.format("<b>违规用户UserID为：<a href=\"tg://user?id=%d\">%s</a></b>", userId, userId);
                        keywordFormat.setReplyText(keywordFormat.getReplyText() + "\n" + text);
                        int deleteReplyAfterYSeconds = Integer.parseInt(currentMap.get("DeleteReplyAfterYSeconds"));
                        if (deleteReplyAfterYSeconds == 0) {
                            // 立即删除reply
                            timerDelete.sendAndDeleteMessageImmediately(sender, (SendMessage) sendContent.createResponseMessage(update, keywordFormat, "html"));
                        } else {
                            // 使用timer删除reply
                            timerDelete.sendTimedMessage(sender, (SendMessage) sendContent.createResponseMessage(update, keywordFormat, "html"), deleteReplyAfterYSeconds);
                        }
                    }
                    return true;
                }
            } // 可以判断后续逻辑，比如是否ban或者禁言
            else {
                if (pattern.matcher(messageText).find()) {
                    Object response = sendContent.replyToUser(update, keywordFormat, "html");
                    if (response instanceof SendMessage) {
                        sender.execute((SendMessage) response);
                    } else if (response instanceof SendVideo) {
                        sender.execute((SendVideo) response);
                    } else if (response instanceof SendPhoto) {
                        sender.execute((SendPhoto) response);
                    }else {
                        throw new RuntimeException("类型错误");
                    }
                    return true;
                }
            }
        }
        return false;
    }
}