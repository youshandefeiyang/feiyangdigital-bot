package top.feiyangdigital.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Component
public class TgWebhookBot extends TelegramWebhookBot {

    private String botName;

    private String botToken;

    private String botPath;

    @Autowired
    private CommonFunction commonFunction;

    @Autowired
    private GroupCommands groupCommands;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        commonFunction.mainFunc(this,update);
        return null;
    }

    public void setAllowUpdated(){
        String[] allowUpdated = new String[]{"update_id","message","edited_message","channel_post","edited_channel_post","inline_query","chosen_inline_result","callback_query","shipping_query","pre_checkout_query","poll","poll_answer","my_chat_member","chat_member","chat_join_request"};
        this.getOptions().setAllowedUpdates(Arrays.asList(allowUpdated));
    }

    public void setGroupCommands() throws TelegramApiException {
        groupCommands.setGroupCommands(this);
    }


    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public String getBotPath() {
        return null;
    }

    public void setBotPath(String botPath) {
        this.botPath = botPath;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}