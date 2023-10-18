package top.feiyangdigital.wordCloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 记录发言内容和次数
    public void recordMessage(Long chatId, String chatName, Long userId, String userName, String message, String date) {
        String keySpeak = String.format("%s_%s_%s-%s_%s_发言数", chatId, chatName, userId, userName, date);
        redisTemplate.opsForZSet().incrementScore(keySpeak, "score", 1);
        redisTemplate.expire(keySpeak, 30, TimeUnit.DAYS);

        String keyContent = String.format("%s_%s_%s-%s_%s_发言内容", chatId, chatName, userId, userName, date);
        redisTemplate.opsForZSet().add(keyContent, message, System.currentTimeMillis());
        redisTemplate.expire(keyContent, 30, TimeUnit.DAYS);
    }

    // 获取指定日期范围的所有消息
    public Set<String> getAllMessagesForDate(String chatId, String chatName, String date) {
        Set<String> allMessages = new HashSet<>();

        // 使用日期直接构建键模式，这样可以避免不必要的键匹配
        String keyPattern = String.format("%s_%s_*_%s_发言内容", chatId, chatName, date);
        Set<String> keys = redisTemplate.keys(keyPattern);
        if (keys != null && keys.isEmpty()) return allMessages;  // 返回空集合，如果没有匹配的键
        if (keys != null) {
            for (String key : keys) {
                Set<String> messages = redisTemplate.opsForZSet().range(key, 0, -1);
                if (messages != null) {
                    allMessages.addAll(messages);
                }
            }
        }

        return allMessages;
    }

    public Map<String, Integer> getTopSpeakersForDate(String chatId, String chatName, String date, int topN) {
        Map<String, Integer> userSpeakCounts = new HashMap<>();

        String keyPattern = String.format("%s_%s_*_%s_发言数", chatId, chatName, date);
        Set<String> keys = redisTemplate.keys(keyPattern);

        if (keys != null) {
            for (String key : keys) {
                Double score = redisTemplate.opsForZSet().score(key, "score");
                if (score != null) {
                    String userName = key.split("_")[2].split("-")[1];
                    userSpeakCounts.put(userName, score.intValue());
                }
            }
        }

        // 对map按值排序
        return userSpeakCounts.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry<String, Integer>::getValue).reversed())
                .limit(topN)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}