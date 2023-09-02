package top.feiyangdigital.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.ruleCacheMap.DeleteRuleCacheMap;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class TimerDelete {

    @Autowired
    private  CheckUser checkUser;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    @Autowired
    private DeleteRuleCacheMap deleteRuleCacheMap;

    @Autowired
    private SendContent sendContent;

    public void deletePrivateMessageImmediately(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        
        addRuleCacheMap.updateUserMapping(userId, addRuleCacheMap.getGroupIdForUser(userId), addRuleCacheMap.getGroupNameForUser(userId),"notallow");
        deleteRuleCacheMap.updateUserMapping(userId, deleteRuleCacheMap.getGroupIdForUser(userId), deleteRuleCacheMap.getGroupNameForUser(userId), "notdelete");
            try {
                sender.execute(new DeleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(), update.getCallbackQuery().getMessage().getMessageId()));
                sender.execute(sendContent.messageText(update,"当前群组ID："+ addRuleCacheMap.getGroupIdForUser(userId)+" \n当前可输入状态："+ addRuleCacheMap.getKeywordsFlagForUser(userId)+"\n你已退出 "+ addRuleCacheMap.getGroupNameForUser(userId)+" 的设置"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    public void deletePrivateUsualMessageImmediately(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        
        try {
            sender.execute(new DeleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(), update.getCallbackQuery().getMessage().getMessageId()));
            sender.execute(sendContent.messageText(update,"你已退出设置"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessageImmediately(AbsSender sender, Update update) {
        if (!checkUser.isUserAdmin(sender, update)) {
            try {
                sender.execute(new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteMessageAfterDelay(AbsSender sender, Update update, int delayInSeconds) {
        if (!checkUser.isUserAdmin(sender, update)) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        sender.execute(new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }, delayInSeconds * 1000);
        }
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> String sendTimedMessage(AbsSender sender, Method method, int delayInSeconds) {
        try {
            Serializable response = sender.execute(method);

            if (response instanceof Message) {
                Message sentMessage = (Message) response;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            sender.execute(new DeleteMessage(sentMessage.getChatId().toString(), sentMessage.getMessageId()));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }, delayInSeconds * 1000);

                return sentMessage.getMessageId().toString();
            }

            return null;

        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> String sendAndDeleteMessageImmediately(AbsSender sender, Method method) {
        try {
            Serializable response = sender.execute(method);

            if (response instanceof Message) {
                Message sentMessage = (Message) response;
                sender.execute(new DeleteMessage(sentMessage.getChatId().toString(), sentMessage.getMessageId()));
                return sentMessage.getMessageId().toString();
            }

            return null;

        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

}
