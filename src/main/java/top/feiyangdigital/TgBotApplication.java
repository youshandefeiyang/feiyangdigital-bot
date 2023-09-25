package top.feiyangdigital;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import top.feiyangdigital.bot.TgLongPollingBot;
import top.feiyangdigital.bot.TgWebhookBot;
import top.feiyangdigital.entity.BaseInfo;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableCaching
@MapperScan("top.feiyangdigital.mapper")
public class TgBotApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TgBotApplication.class);

    @Autowired
    private TgLongPollingBot tgLongPollingBot;

    @Autowired
    private TgWebhookBot tgWebhookBot;

    private final List<String> allowed_Update = Arrays.asList("update_id", "message", "edited_message", "channel_post", "edited_channel_post", "inline_query", "chosen_inline_result", "callback_query", "shipping_query", "pre_checkout_query", "poll", "poll_answer", "my_chat_member", "chat_member", "chat_join_request");

    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
    }

    @PostConstruct
    public void init() {
        tgLongPollingBot.getOptions().setAllowedUpdates(allowed_Update);
        tgWebhookBot.getOptions().setAllowedUpdates(allowed_Update);
        log.info("AllowedUpdates属性设置完毕");
    }

    @Override
    public void run(String... args) throws Exception {
        if ("longPolling".equals(BaseInfo.getBotMode())) {
            tgLongPollingBot.setBotName(BaseInfo.getBotName());
            tgLongPollingBot.setBotToken(BaseInfo.getBotToken());
            tgLongPollingBot.setGroupCommands();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(tgLongPollingBot);
            log.info("longPolling模式已启动");
        } else if ("webhook".equals(BaseInfo.getBotMode())) {
            tgWebhookBot.setBotToken(BaseInfo.getBotToken());
            tgWebhookBot.setGroupCommands();
            SetWebhook setWebhook = new SetWebhook(BaseInfo.getBotPath());
            setWebhook.setAllowedUpdates(allowed_Update);  // 添加这行来设置 allowed_updates
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(tgWebhookBot, setWebhook);
            log.info("webhook模式已启动");
        } else {
            throw new Exception("请将配置填写完整");
        }
    }
}