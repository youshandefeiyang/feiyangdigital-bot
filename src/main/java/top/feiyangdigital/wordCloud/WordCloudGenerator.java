package top.feiyangdigital.wordCloud;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WordCloudGenerator {

    @Autowired
    private MessageService messageService;

    private final Random random = new Random();

    public ColorPalette generateRandomColorPalette(int numberOfColors) {
        Color[] colors = new Color[numberOfColors];
        for (int i = 0; i < numberOfColors; i++) {
            colors[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }
        return new ColorPalette(colors);
    }

    public BufferedImage generateWordCloud(java.util.List<String> texts) {
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(2);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(texts);
        final Dimension dimension = new Dimension(1200, 1200);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setBackground(new CircleBackground(600));
        wordCloud.setPadding(5);
        wordCloud.setBackgroundColor(Color.WHITE);
        wordCloud.setColorPalette(generateRandomColorPalette(10));
        wordCloud.setKumoFont(new KumoFont(new Font("WenQuanYi Micro Hei", Font.PLAIN, 20))); 
        wordCloud.setFontScalar(new LinearFontScalar(20, 100));
        wordCloud.build(wordFrequencies);
        return wordCloud.getBufferedImage();
    }

    public void generateAndSendWordCloud(AbsSender sender,String chatId, String chatName, String date) throws TelegramApiException, IOException {
        Set<String> allMessages = messageService.getAllMessagesForDate(chatId, chatName, date);

        String combinedMessages = String.join("\n", allMessages);

//        System.out.println(combinedMessages);

        List<String> words = ToAnalysis.parse(combinedMessages).getTerms().stream()
                .map(term -> term.getName().trim())
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
        BufferedImage wordCloudImage = generateWordCloud(words);
        Path tempFile = Files.createTempFile("wordcloud", ".png");
        ImageIO.write(wordCloudImage, "PNG", tempFile.toFile());

        InputFile inputFile = new InputFile(tempFile.toFile());
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption(buildStatisticsMessage(chatId,chatName,date));
        sendPhoto.setParseMode(ParseMode.HTML);
        sender.execute(sendPhoto);
        Files.deleteIfExists(tempFile);
    }

    public String buildStatisticsMessage(String chatId, String chatName, String date) {
        int totalMessages = messageService.getAllMessagesForDate(chatId, chatName, date).size();
        Map<String, Integer> topSpeakers = messageService.getTopSpeakersForDate(chatId, chatName, date, 10);

        StringBuilder message = new StringBuilder();

        message.append("🎤 今日话题词云 ").append("<b>").append(date).append("</b>").append(" 🎤\n");
        message.append("⏰ 截至今天 ").append("<b>").append(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).format(DateTimeFormatter.ofPattern("HH:mm"))).append("</b>").append("\n");
        message.append("🗣️ 本群 ").append("<b>").append(topSpeakers.size()).append("</b>").append(" 位朋友共产生 ").append("<b>").append(totalMessages).append("</b>").append(" 条无重复发言\n");
        message.append("🔍 看下有没有你感兴趣的关键词？\n\n");
        message.append("🏵 活跃用户排行榜 🏵\n\n");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : topSpeakers.entrySet()) {
            if (rank == 1) message.append("🥇");
            else if (rank == 2) message.append("🥈");
            else if (rank == 3) message.append("🥉");
            else message.append("🎖");

            message.append("<b>").append(entry.getKey()).append("</b>").append("  贡献: ").append(entry.getValue()).append("\n");
            rank++;
        }

        message.append("\n🎉感谢这些朋友今天的分享!🎉");

        return message.toString();
    }

}



