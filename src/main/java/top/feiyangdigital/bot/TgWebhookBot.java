package top.feiyangdigital.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Component
public class TgWebhookBot extends TelegramWebhookBot {

    private String botName;

    private String botToken;

    private String botPath;

    @Autowired
    private CommonFunction commonFunction;

    @Autowired
    private GroupCommands groupCommands;

    @Autowired
    public TgWebhookBot(List<String> list){
        this.getOptions().setAllowedUpdates(list);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        commonFunction.mainFunc(this,update);
        return null;
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