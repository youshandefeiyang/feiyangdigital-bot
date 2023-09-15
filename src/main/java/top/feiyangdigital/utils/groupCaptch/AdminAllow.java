package top.feiyangdigital.utils.groupCaptch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.utils.TimerDelete;

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

    public void allow(AbsSender sender, Long userId, String chatId,Integer messageId, AnswerCallbackQuery answer){
        restrictOrUnrestrictUser.unrestrictUser(sender,userId,chatId);
        answer.setText("用户已被手动解禁");
        timerDelete.deleteByMessageIdImmediately(sender,chatId,messageId);
        captchaManager.clearMappingsForUser(userId.toString());
        captchaManagerCacheMap.clearMappingsForUser(userId.toString(), chatId);
        try {
            sender.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}