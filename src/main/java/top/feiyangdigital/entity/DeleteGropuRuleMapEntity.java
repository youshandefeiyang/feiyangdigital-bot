package top.feiyangdigital.entity;


import top.feiyangdigital.utils.DeleteGropuRuleMap;

import java.util.Map;

public class DeleteGropuRuleMapEntity {

    private final DeleteGropuRuleMap deleteGropuRuleMap;

    public DeleteGropuRuleMapEntity(DeleteGropuRuleMap deleteGropuRuleMap) {
        this.deleteGropuRuleMap = deleteGropuRuleMap;
    }

    public String removeRuleAndAssembleString(String groupId, String uuid) {
        // 1. 删除规则
        deleteGropuRuleMap.removeRuleFromGroup(groupId, uuid);

        // 2. 重新组装规则为字符串
        return assembleRules(groupId);
    }

    public String assembleRules(String groupId) {
        StringBuilder stringBuilder = new StringBuilder();
        DeleteGropuRuleMap.GroupRuleMappings mappings = deleteGropuRuleMap.getAllRulesFromGroupId(groupId);

        if (mappings != null) {
            for (Map.Entry<String, String> entry : mappings.getUuidToRuleMap().entrySet()) {
                stringBuilder.append(entry.getKey());
                stringBuilder.append(" | ");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("\n\n");
            }
        }

        return stringBuilder.toString();
    }
}

