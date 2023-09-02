package top.feiyangdigital.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeleteGropuRuleMap {

    // 该映射用于将groupId关联到它的两个映射
    private final Map<String, GroupRuleMappings> deleteGroupRuleMap = new ConcurrentHashMap<>();

    public int getGroupRuleMapSize(){
        return deleteGroupRuleMap.size();
    }

    // 添加规则到指定的群组
    public void addRuleToGroup(String groupId, String uuid, String rule) {
        GroupRuleMappings mappings = deleteGroupRuleMap.computeIfAbsent(groupId, k -> new GroupRuleMappings());
        mappings.addRule(uuid, rule);
    }

    // 根据groupId获取所有规则
    public GroupRuleMappings getAllRulesFromGroupId(String groupId){
        return deleteGroupRuleMap.get(groupId);
    }

    // 移除指定群组的指定规则
    public void removeRuleFromGroup(String groupId, String uuid) {
        GroupRuleMappings mappings = deleteGroupRuleMap.get(groupId);
        if (mappings != null) {
            mappings.removeRuleByUuid(uuid);
        }
    }

    public static class GroupRuleMappings {
        // UUID对应规则
        private final Map<String, String> uuidToRuleMap = new ConcurrentHashMap<>();

        // UUID前几位对应完整的UUID
        private final Map<String, String> shortUuidToFullUuidMap = new ConcurrentHashMap<>();

        public void addRule(String uuid, String rule) {
            uuidToRuleMap.put(uuid, rule);
            shortUuidToFullUuidMap.put(uuid.substring(0, 5), uuid);  // 保存前5位作为键
        }

        public String getRuleByUuid(String uuid) {
            return uuidToRuleMap.get(uuid);
        }

        public String getFullUuidByShortUuid(String shortUuid) {
            return shortUuidToFullUuidMap.get(shortUuid);
        }

        public Map<String, String> getUuidToRuleMap() {
            return uuidToRuleMap;
        }

        public Map<String, String> getShortUuidToFullUuidMap() {
            return shortUuidToFullUuidMap;
        }

        public void removeRuleByUuid(String uuid) {
            String rule = uuidToRuleMap.remove(uuid);
            if (rule != null) {
                shortUuidToFullUuidMap.remove(uuid.substring(0, 5));
            }
        }
    }
}