package top.feiyangdigital.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.utils.groupCaptch.CaptchaManager;
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

    @Autowired
    private CaptchaManager captchaManager;

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
        try {
            sender.execute(new DeleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(), update.getCallbackQuery().getMessage().getMessageId()));
            sender.execute(sendContent.messageText(update,"你已退出设置"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessageImmediately(AbsSender sender, Update update) {
        if (!checkUser.isGroupAdmin(sender, update)) {
            try {
                sender.execute(new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteMessageAfterDelay(AbsSender sender, Update update, int delayInSeconds) {
        if (!checkUser.isGroupAdmin(sender, update)) {
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

    public void deleteMessageAndNotifyAfterDelay(AbsSender sender, String chatId, Integer messageId, int delayInSeconds, Long userId, String text,int notifyDelay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sender.execute(new DeleteMessage(chatId, messageId));
                    captchaManager.clearMappingsForUser(userId.toString());
                    // 在此发送提示消息
                    SendMessage notification = new SendMessage();
                    notification.setChatId(chatId);
                    notification.setText(text);
                    notification.setParseMode(ParseMode.HTML);
                    Message message = sender.execute(notification);
                    deleteMessageByMessageIdDelay(sender,chatId,message.getMessageId(),notifyDelay);

                } catch (TelegramApiException e) {
                    // 如果您仍希望不论是否出现异常都发送提示，那么将发送提示的逻辑移至catch块外部
                }
            }
        }, delayInSeconds * 1000);
    }

    public void deleteMessageImmediatelyAndNotifyAfterDelay(AbsSender sender, String chatId, Integer messageId, Long userId, String text,int notifyDelay) {
            try {
                sender.execute(new DeleteMessage(chatId,messageId));
                captchaManager.clearMappingsForUser(userId.toString());
                // 在此发送提示消息
                SendMessage notification = new SendMessage();
                notification.setChatId(chatId);
                notification.setText(text);
                notification.setParseMode(ParseMode.HTML);
                Message message = sender.execute(notification);
                deleteMessageByMessageIdDelay(sender,chatId,message.getMessageId(),notifyDelay);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    public void deleteMessageByMessageIdDelay(AbsSender sender, String chatId, Integer messageId, int delayInSeconds) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sender.execute(new DeleteMessage(chatId, messageId));
                } catch (TelegramApiException e) {
                    // 这里可以捕获异常，但是我们可以选择不执行任何操作，因为我们不关心消息是否确实已经被删除
                }
            }
        }, delayInSeconds * 1000);
    }

    public void deleteByMessageIdImmediately(AbsSender sender, String chatId, Integer messageId) {
        try {
            DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
            sender.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
