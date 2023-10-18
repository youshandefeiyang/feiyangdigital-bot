package top.feiyangdigital.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllChatAdministrators;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Component
public class GroupCommands {
    public void setGroupCommands(AbsSender sender) throws TelegramApiException {
        List<BotCommand> commands = Arrays.asList(
                new BotCommand("/setbot", "设置机器人，仅群主可用"),
                new BotCommand("/rank", "生成词云排行榜"),
                new BotCommand("/ban", "!或/ban user 时间(可选) 原因(可选)"),
                new BotCommand("/dban","可Ban掉用户的同时，删除他的发言，格式参考Ban"),
                new BotCommand("/unban", "解封用户，!或/unban user"),
                new BotCommand("/mute", "禁言用户，格式参考Ban"),
                new BotCommand("/unmute","解除用户发言限制，!或/unmute user")
        );

        BotCommandScopeAllChatAdministrators scope = new BotCommandScopeAllChatAdministrators();


        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setScope(scope);
        setMyCommands.setCommands(commands);

        sender.execute(setMyCommands);
    }
}
