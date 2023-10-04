package top.feiyangdigital.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TgLongPollingBot extends TelegramLongPollingBot {

    private String botName;
    private String botToken;

    @Autowired
    private CommonFunction commonFunction;

    @Autowired
    private GroupCommands groupCommands;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        commonFunction.mainFunc(this, update);
    }

    public void setGroupCommands() throws TelegramApiException {
        groupCommands.setGroupCommands(this);
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }
}