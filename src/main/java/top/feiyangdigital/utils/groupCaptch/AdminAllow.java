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
    private BanOrUnBan banOrUnBan;

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private CaptchaManager captchaManager;

    @Autowired
    private CaptchaManagerCacheMap captchaManagerCacheMap;

    public void allow(AbsSender sender, Long userId, String chatId, Integer messageId, AnswerCallbackQuery answer, boolean haveAnswer) throws TelegramApiException {
        restrictOrUnrestrictUser.unrestrictUser(sender, userId, chatId);
        answer.setText("用户已被手动解禁");
        timerDelete.deleteByMessageIdImmediately(sender, chatId, messageId);
        if (haveAnswer) {
            captchaManager.clearMappingsForUser(userId.toString());
        }
        captchaManagerCacheMap.clearMappingsForUser(userId.toString(), chatId);
        sender.execute(answer);
    }

    public void allowUnBan(AbsSender sender, Long userId, String chatId, Integer messageId, AnswerCallbackQuery answer) throws TelegramApiException {
        banOrUnBan.unBanUserById(sender, userId, chatId);
        answer.setText("用户已被手动解封");
        timerDelete.deleteByMessageIdImmediately(sender, chatId, messageId);
        captchaManagerCacheMap.clearMappingsForUser(userId.toString(), chatId);
        sender.execute(answer);
    }
}