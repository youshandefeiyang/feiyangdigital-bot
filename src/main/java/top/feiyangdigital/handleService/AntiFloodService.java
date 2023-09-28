package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.CheckUser;
import top.feiyangdigital.utils.TimerDelete;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AntiFloodService {

    private final ConcurrentHashMap<String, List<MessageInfo>> userGroupMessages = new ConcurrentHashMap<>();

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private CheckUser checkUser;

    @Autowired
    private TimerDelete timerDelete;

    public static class MessageInfo {
        public final int messageId;
        public final Instant timestamp;

        public MessageInfo(int messageId, Instant timestamp) {
            this.messageId = messageId;
            this.timestamp = timestamp;
        }
    }

    public void checkFlood(AbsSender sender, Update update) throws TelegramApiException {
        String groupId = update.getMessage().getChatId().toString();
        Long userId = update.getMessage().getFrom().getId();
        String firstName = update.getMessage().getFrom().getFirstName();
        Integer messageId = update.getMessage().getMessageId();
        Instant messageTimestamp = Instant.ofEpochMilli(System.currentTimeMillis());
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(groupId);
        if (!checkUser.isGroupAdmin(sender,update) &&  groupInfoWithBLOBs!=null && "open".equals(groupInfoWithBLOBs.getAntifloodflag()) && StringUtils.hasText(groupInfoWithBLOBs.getAntifloodsetting())){
            int floodSecond = Integer.parseInt(groupInfoWithBLOBs.getAntifloodsetting().split(",")[0]);
            int floodInfoCount = Integer.parseInt(groupInfoWithBLOBs.getAntifloodsetting().split(",")[1]);
            String key = groupId + "_" + userId;
            userGroupMessages.compute(key, (k, messages) -> {
                if (messages == null) {
                    messages = new ArrayList<>();
                }
                messages.add(new MessageInfo(messageId, messageTimestamp));
                // 保留最近x秒内的消息
                messages.removeIf(info -> Duration.between(info.timestamp, messageTimestamp).getSeconds() > floodSecond);
                // 检查消息数量是否超过x条
                if (messages.size() > floodInfoCount) {
                    triggerAntiSpamMode(sender,groupId, messages);
                    String text = String.format("用户 <b><a href=\"tg://user?id=%d\">%s</a></b> 在 <b>%d秒内</b> 连续发送<b>%d条消息</b>，触发反刷屏模式！", userId, firstName,floodSecond,floodInfoCount);
                    SendMessage notification = new SendMessage();
                    notification.setChatId(groupId);
                    notification.setText(text);
                    notification.setParseMode(ParseMode.HTML);
                    timerDelete.sendTimedMessage(sender,notification,20);
                    messages.clear();  // 清空消息列表，避免连续触发
                }
                return messages;  // 返回更新后的消息列表
            });
        }
    }

    private void triggerAntiSpamMode(AbsSender sender,String groupId, List<MessageInfo> messages) {
        messages.forEach(i->{
           DeleteMessage deleteMessage = new DeleteMessage(groupId, i.messageId);
            try {
                sender.execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

