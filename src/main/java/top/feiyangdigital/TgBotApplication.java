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

@SpringBootApplication
@EnableCaching
@MapperScan("top.feiyangdigital.mapper")
public class TgBotApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TgBotApplication.class);

    @Autowired
    private TgLongPollingBot tgLongPollingBot;

    @Autowired
    private TgWebhookBot tgWebhookBot;

    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if ("longPolling".equals(BaseInfo.getBotMode())) {
            log.info("longPolling模式已启动");
            tgLongPollingBot.setBotName(BaseInfo.getBotName());
            tgLongPollingBot.setBotToken(BaseInfo.getBotToken());
            tgLongPollingBot.setGroupCommands();
            tgLongPollingBot.setAllowUpdated();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(tgLongPollingBot);
        } else if ("webhook".equals(BaseInfo.getBotMode())) {
            log.info("webhook模式已启动");
            tgWebhookBot.setBotToken(BaseInfo.getBotToken());
            tgWebhookBot.setGroupCommands();
            tgWebhookBot.setAllowUpdated();
            SetWebhook setWebhook = SetWebhook.builder().url(BaseInfo.getBotPath()).build();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(tgWebhookBot, setWebhook);
        } else {
            throw new Exception("请将配置填写完整");
        }
    }
}
