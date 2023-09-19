package top.feiyangdigital.utils.ruleCacheMap;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AddRuleCacheMap {

    private final Map<String, String> userToGroupMap = new ConcurrentHashMap<>();
    private final Map<String, String> userToGroupNameMap = new ConcurrentHashMap<>();
    private final Map<String, String> userToKeywordsFlagMap = new ConcurrentHashMap<>();

    private final Map<String,String> userToAiFlagMap = new ConcurrentHashMap<>();

    public void updateUserMapping(String userId, String groupId, String groupName, String keywordsFlag,String aiFlag) {
        // 每次都覆盖旧的数据，因为在任何时候，一个userId只与一个群组关联
        userToGroupMap.put(userId, groupId);
        userToGroupNameMap.put(userId, groupName);
        userToKeywordsFlagMap.put(userId, keywordsFlag);
        userToAiFlagMap.put(userId,aiFlag);
    }

    public String getGroupIdForUser(String userId) {
        return userToGroupMap.get(userId);
    }

    public String getGroupNameForUser(String userId) {
        return userToGroupNameMap.get(userId);
    }

    public String getKeywordsFlagForUser(String userId) {
        return userToKeywordsFlagMap.get(userId);
    }

    public String getAiFlagForUser(String userId) {
        return userToAiFlagMap.get(userId);
    }

    public void clearMappingsForUser(String userId) {
        userToGroupMap.remove(userId);
        userToGroupNameMap.remove(userId);
        userToKeywordsFlagMap.remove(userId);
        userToAiFlagMap.remove(userId);
    }
}
