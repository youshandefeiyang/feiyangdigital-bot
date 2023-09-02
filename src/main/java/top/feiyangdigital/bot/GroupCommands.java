package top.feiyangdigital.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.*;
import org.telegram.telegrambots.meta.api.objects.commands.scope.serialization.BotCommandScopeDeserializer;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Component
public class GroupCommands {
    public void setGroupCommands(AbsSender sender) throws TelegramApiException {
        List<BotCommand> commands = Arrays.asList(
                new BotCommand("/setbot", "设置机器人"),
                new BotCommand("/ban", "封禁群员"),
                new BotCommand("/unban", "解封群员"),
                new BotCommand("/kick", "踢出群员")
        );

        BotCommandScopeAllChatAdministrators scope = new BotCommandScopeAllChatAdministrators();


        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setScope(scope);
        setMyCommands.setCommands(commands);

        sender.execute(setMyCommands);
    }
}
