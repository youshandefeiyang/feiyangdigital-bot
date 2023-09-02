package top.feiyangdigital.utils.ruleCacheMap;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeleteRuleCacheMap {

    private final Map<String, String> userToGroupMap = new ConcurrentHashMap<>();
    private final Map<String, String> userToGroupNameMap = new ConcurrentHashMap<>();
    private final Map<String, String> userToDeleteKeywordFlagMap = new ConcurrentHashMap<>();

    public void updateUserMapping(String userId, String groupId, String groupName, String deleteKeywordFlag) {
        // 每次都覆盖旧的数据，因为在任何时候，一个userId只与一个群组关联
        userToGroupMap.put(userId, groupId);
        userToGroupNameMap.put(userId, groupName);
        userToDeleteKeywordFlagMap.put(userId, deleteKeywordFlag);
    }

    public String getGroupIdForUser(String userId) {
        return userToGroupMap.get(userId);
    }

    public String getGroupNameForUser(String userId) {
        return userToGroupNameMap.get(userId);
    }

    public String getDeleteKeywordFlagMap(String userId) {
        return userToDeleteKeywordFlagMap.get(userId);
    }

    public void clearMappingsForUser(String userId) {
        userToGroupMap.remove(userId);
        userToGroupNameMap.remove(userId);
        userToDeleteKeywordFlagMap.remove(userId);
    }
}
