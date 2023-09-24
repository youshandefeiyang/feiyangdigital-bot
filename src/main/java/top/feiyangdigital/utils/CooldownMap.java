package top.feiyangdigital.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CooldownMap {

    private final Map<UserIdGroupIdKey, Long> cooldownData = new ConcurrentHashMap<>();

    public void setCooldown(String userId, String groupId) {
        UserIdGroupIdKey key = new UserIdGroupIdKey(userId, groupId);
        cooldownData.put(key, System.currentTimeMillis());
    }

    public boolean isCooldownElapsed(String userId, String groupId, long cooldownMillis) {
        UserIdGroupIdKey key = new UserIdGroupIdKey(userId, groupId);
        Long lastAccessTime = cooldownData.get(key);
        if (lastAccessTime == null) {
            return true;
        }
        return System.currentTimeMillis() - lastAccessTime > cooldownMillis;
    }

    private static class UserIdGroupIdKey {
        private final String userId;
        private final String groupId;

        public UserIdGroupIdKey(String userId, String groupId) {
            this.userId = userId;
            this.groupId = groupId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserIdGroupIdKey that = (UserIdGroupIdKey) o;
            return userId.equalsIgnoreCase(that.userId) && groupId.equalsIgnoreCase(that.groupId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, groupId);
        }
    }
}