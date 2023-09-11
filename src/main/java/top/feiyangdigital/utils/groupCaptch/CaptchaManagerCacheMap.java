package top.feiyangdigital.utils.groupCaptch;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class CaptchaManagerCacheMap {

    private final Map<String, UserGroupInfo> userToGroupInfoMap = new ConcurrentHashMap<>();

    // 内部类来存储群组的attempt和messageId信息
    private static class UserGroupInfo {
        private final Integer attempt;
        private final Integer messageId;

        public UserGroupInfo(Integer attempt, Integer messageId) {
            this.attempt = attempt;
            this.messageId = messageId;
        }

        public Integer getAttempt() {
            return attempt;
        }

        public Integer getMessageId() {
            return messageId;
        }
    }

    private String createKey(String userId, String groupId) {
        return userId + "|" + groupId;
    }

    public void updateUserMapping(String userId, String groupId, Integer attempt, Integer messageId) {
        userToGroupInfoMap.put(createKey(userId, groupId), new UserGroupInfo(attempt, messageId));
    }

    public Integer getAttemptForUser(String userId, String groupId) {
        UserGroupInfo info = userToGroupInfoMap.get(createKey(userId, groupId));
        return info == null ? null : info.getAttempt();
    }

    public Integer getMessageIdForUser(String userId, String groupId) {
        UserGroupInfo info = userToGroupInfoMap.get(createKey(userId, groupId));
        return info == null ? null : info.getMessageId();
    }

    public void clearMappingsForUser(String userId, String groupId) {
        userToGroupInfoMap.remove(createKey(userId, groupId));
    }
}