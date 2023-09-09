package top.feiyangdigital.utils.groupCaptch;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class CaptchaManager {
    private final Map<String, String> userToGroupMap = new ConcurrentHashMap<>();
    private final Map<String, String> userToAnswer = new ConcurrentHashMap<>();

    public void updateUserMapping(String userId, String groupId, String answer) {
        // 每次都覆盖旧的数据，因为在任何时候，一个userId只与一个群组关联
        userToGroupMap.put(userId, groupId);
        userToAnswer.put(userId, answer);
    }

    public String getGroupIdForUser(String userId) {
        return userToGroupMap.get(userId);
    }

    public String getAnswerForUser(String userId) {
        return userToAnswer.get(userId);
    }


    public void clearMappingsForUser(String userId) {
        userToGroupMap.remove(userId);
        userToAnswer.remove(userId);
    }

}
