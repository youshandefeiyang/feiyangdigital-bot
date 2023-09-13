package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public void processUserMessage(AbsSender sender, Update update, List<KeywordsFormat> keywordsList) {
        String messageText = update.getMessage().getText();

        // 如果消息文本为null，直接返回，不做处理
        if (messageText == null) {
            return;
        }
        //这代表的是从群组绑定频道发过来的消息，直接返回，不做处理
        //ToDo 反频道马甲机器人
        if ("Telegram".equals(update.getMessage().getFrom().getFirstName()) && update.getMessage().getSenderChat().isChannelChat()) {
            return;
        }

        for (KeywordsFormat keywordFormat : keywordsList) {
            Map<String, String> currentMap = keywordFormat.getRuleMap();
            String regex = keywordFormat.getRegex();
            Pattern pattern = Pattern.compile(regex);

            if (currentMap.containsKey("DelWelcome")) {// 这是欢迎词，不需要匹配直接返回
                return;
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
                        int deleteReplyAfterYSeconds = Integer.parseInt(currentMap.get("DeleteReplyAfterYSeconds"));
                        if (deleteReplyAfterYSeconds == 0) {
                            // 立即删除reply
                            timerDelete.sendAndDeleteMessageImmediately(sender, sendContent.createResponseMessage(update, keywordFormat, "markdown"));
                        } else {
                            // 使用timer删除reply
                            timerDelete.sendTimedMessage(sender, sendContent.createResponseMessage(update, keywordFormat, "markdown"), deleteReplyAfterYSeconds);
                        }
                    }
                    break;
                }
            } // 可以判断后续逻辑，比如是否ban或者禁言
            else {
                if (pattern.matcher(messageText).find()) {
                    try {
                        sender.execute(sendContent.replyToUser(update, keywordFormat, "markdown"));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}