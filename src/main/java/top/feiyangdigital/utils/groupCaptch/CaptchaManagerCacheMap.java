package top.feiyangdigital.utils.groupCaptch;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class CaptchaManagerCacheMap {
    private final Map<String, Integer> userToAttemptMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> userToMessageIdMap = new ConcurrentHashMap<>();

    public void updateUserMapping(String userId, Integer attempt, Integer MessageId) {
        // 每次都覆盖旧的数据，因为在任何时候，一个userId只与一个群组关联
        userToAttemptMap.put(userId,attempt);
        userToMessageIdMap.put(userId,MessageId);
    }

    public Integer getAttemptForUser(String userId) {
        return userToAttemptMap.get(userId);
    }

    public Integer getMessageIdForUser(String userId) {
        return userToMessageIdMap.get(userId);
    }


    public void clearMappingsForUser(String userId) {
        userToAttemptMap.remove(userId);
        userToMessageIdMap.remove(userId);
    }

}
