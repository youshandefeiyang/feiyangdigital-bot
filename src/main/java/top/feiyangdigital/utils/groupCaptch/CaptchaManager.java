package top.feiyangdigital.utils.groupCaptch;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class CaptchaManager {
    private final Map<String, String> userToGroupForAnswer = new ConcurrentHashMap<>();

    public void updateUserMapping(String userId, String groupId, String answer) {
        userToGroupForAnswer.put(userId+"|"+groupId,answer);
    }

    public String getGroupIdForUser(String userId) {

        for (Map.Entry<String, String> entry : userToGroupForAnswer.entrySet()) {
            if (userId.equals(entry.getKey().split("\\|")[0])){
                return entry.getKey().split("\\|")[1];
            }
        }
        return null;
    }

    public String getAnswerForUser(String userId) {
        for (Map.Entry<String, String> entry : userToGroupForAnswer.entrySet()) {
            if (userId.equals(entry.getKey().split("\\|")[0])){
                return entry.getValue();
            }
        }
        return null;
    }


    public void clearMappingsForUser(String userId) {
        for (Map.Entry<String, String> entry : userToGroupForAnswer.entrySet()) {
            if (userId.equals(entry.getKey().split("\\|")[0])){
                userToGroupForAnswer.remove(entry.getKey());
            }
        }
    }

}
