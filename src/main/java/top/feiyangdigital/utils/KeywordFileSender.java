package top.feiyangdigital.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Component
public class KeywordFileSender {

    @Autowired
    private CooldownMap cooldownMap;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    @Autowired
    private SendContent sendContent;

    public void sendKeywordsFile(AbsSender sender, Update update) {
        String userId = "";
        String chatId = ""; //这个chatId是私人会话的，下面查数据库的是群组的chatId
        if (update.getMessage() == null) {
            userId = update.getCallbackQuery().getFrom().getId().toString();
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        } else {
            userId = update.getMessage().getFrom().getId().toString();
            chatId = update.getMessage().getChatId().toString();
        }
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String settingTimestamp = groupInfoWithBLOBs.getSettingtimestamp();
        if (settingTimestamp != null && !settingTimestamp.isEmpty()) {
            if (new Date().getTime() - Long.parseLong(settingTimestamp) > (15 * 60 * 1000)) {
                try {
                    sender.execute(sendContent.messageText(update, "本次设置超时，请去群里重新发送/setbot"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                if (cooldownMap.isCooldownElapsed(userId, addRuleCacheMap.getGroupIdForUser(userId), 10000)) {
                    String keywords = groupInfoWithBLOBs.getKeywords();
                    if (keywords != null && !keywords.isEmpty()) {
                        File keywordFile = convertStringToFile(keywords.trim());
                        InputStream thumbStream = getClass().getClassLoader().getResourceAsStream("callback.png");
                        InputFile thumb = new InputFile(thumbStream, "callback.png");
                        SendDocument sendDocument = new SendDocument();
                        sendDocument.setChatId(chatId);
                        sendDocument.setDocument(new InputFile(keywordFile, "所有规则.txt"));
                        sendDocument.setThumbnail(thumb);
                        sendDocument.setCaption("查询所有规则冷却时间为10秒，请勿频繁点击！");

                        try {
                            sender.execute(sendDocument);
                            cooldownMap.setCooldown(userId, addRuleCacheMap.getGroupIdForUser(userId));  // 设置冷却时间
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        sender.execute(sendContent.messageText(update, "查询所有规则冷却时间为10秒，请勿频繁点击！"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private File convertStringToFile(String content) {
        try {
            Path tempFile = Files.createTempFile("所有规则", ".txt");
            try (FileWriter writer = new FileWriter(tempFile.toFile())) {
                writer.write(content);
            }
            return tempFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException("写入文件失败", e);
        }
    }
}
