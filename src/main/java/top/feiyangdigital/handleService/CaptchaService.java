package top.feiyangdigital.handleService;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface CaptchaService {

    void sendCaptcha(AbsSender sender,Update update, String chatId) throws TelegramApiException;

    void answerReplyhandle(AbsSender sender, Update update) throws TelegramApiException;
}
