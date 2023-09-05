package top.feiyangdigital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import top.feiyangdigital.bot.TgBot;
import top.feiyangdigital.entity.BaseInfo;

@SpringBootApplication
@MapperScan("top.feiyangdigital.mapper")
public class TgBotApplication implements CommandLineRunner {

    @Autowired
    private TgBot tgBot;

    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("TgBotApplication.run");
        tgBot.setBotName(BaseInfo.getBotName());
        tgBot.setBotToken(BaseInfo.getBotToken());
        tgBot.setGroupCommands();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(tgBot);
    }
}
