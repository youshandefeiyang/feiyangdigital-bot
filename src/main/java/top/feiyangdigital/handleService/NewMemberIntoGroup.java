package top.feiyangdigital.handleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;
import top.feiyangdigital.utils.groupCaptch.CaptchaManagerCacheMap;
import top.feiyangdigital.utils.groupCaptch.RestrictOrUnrestrictUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NewMemberIntoGroup {

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private RestrictOrUnrestrictUser restrictOrUnrestrictUser;

    @Autowired
    private SendContent sendContent;

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private CaptchaManagerCacheMap captchaManagerCacheMap;

    public void handleMessage(AbsSender sender, Update update) {
        Message message = update.getMessage();
        if (message.getNewChatMembers() != null) {
            for (User user : message.getNewChatMembers()) {
                if (user.getIsBot() && BaseInfo.getBotName().equals(user.getUserName())) {
                    String chatId = message.getChat().getId().toString();
                    if ("open".equals(BaseInfo.getBotLimitStatus()) && BaseInfo.getGroupWhiteList().contains(chatId)) {
                        String groupName = message.getChat().getTitle();
                        GroupInfoWithBLOBs groupInfo = new GroupInfoWithBLOBs();
                        groupInfo.setGroupid(chatId);
                        groupInfo.setGroupname(groupName);
                        groupInfo.setSettingtimestamp(String.valueOf(new Date().getTime()));
                        if (groupInfoService.selGroup(chatId)) {
                            groupInfoService.addGroup(groupInfo);
                        }
                    } else {
                        LeaveChat leaveChat = new LeaveChat();
                        leaveChat.setChatId(chatId);
                        try {
                            sender.execute(leaveChat);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } else if ("open".equals(groupInfoService.selAllByGroupId(update.getMessage().getChatId().toString()).getIntogroupcheckflag())) {
                    Long userId = user.getId();
                    Long chatId = update.getMessage().getChatId();
                    String url = String.format("https://t.me/%s?start=_intoGroupInfo%sand%s", BaseInfo.getBotName(), chatId.toString(), userId.toString());
                    restrictOrUnrestrictUser.restrictUser(sender, userId, chatId.toString());
                    KeywordsFormat keywordsFormat = new KeywordsFormat();
                    List<String> keywordsButtons = new ArrayList<>();
                    keywordsButtons.add("👥管理员解封##adminUnrestrict" + userId);
                    keywordsButtons.add("❗️点击验证$$" + url);
                    keywordsFormat.setKeywordsButtons(keywordsButtons);
                    String text = String.format("欢迎 <b><a href=\"tg://user?id=%d\">%s</a></b> 加入<b> %s </b>, 现在你需要在<b>90秒内</b>点击下面的验证按钮完成验证，超时将永久限制发言！", userId, user.getFirstName(), update.getMessage().getChat().getTitle());
                    keywordsFormat.setReplyText(text);
                    try {
                        Message message1 = sender.execute(sendContent.createResponseMessage(update, keywordsFormat, "html"));
                        Integer messageId = message1.getMessageId();
                        captchaManagerCacheMap.updateUserMapping(userId.toString(), 0, messageId);
                        timerDelete.deleteMessageAndNotifyAfterDelay(sender, chatId.toString(), messageId, 90, userId, user.getFirstName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
