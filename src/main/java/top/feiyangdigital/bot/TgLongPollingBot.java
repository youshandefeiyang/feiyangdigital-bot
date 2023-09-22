package top.feiyangdigital.bot;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.utils.userNameToUserId.ObtainUserId;

import java.util.List;

@Component
public class TgLongPollingBot extends TelegramLongPollingBot {

    private String botName;
    private String botToken;

    @Autowired
    private CommonFunction commonFunction;

    @Autowired
    private GroupCommands groupCommands;

    @Override
    public void onUpdateReceived(Update update) {
//        System.out.println(update);
        commonFunction.mainFunc(this,update);
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