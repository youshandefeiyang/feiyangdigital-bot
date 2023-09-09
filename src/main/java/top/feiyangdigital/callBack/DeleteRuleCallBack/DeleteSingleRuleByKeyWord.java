package top.feiyangdigital.callBack.DeleteRuleCallBack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.DeleteGropuRuleMap;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.ruleCacheMap.DeleteRuleCacheMap;

import java.util.*;

@Component
public class DeleteSingleRuleByKeyWord {

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private SendContent sendContent;

    @Autowired
    private DeleteRuleCacheMap deleteRuleCacheMap;

    @Autowired
    private DeleteGropuRuleMap deleteGropuRuleMap;

    public void DeleteOption(AbsSender sender, Update update) {
        if (update.getMessage().getText() != null && !update.getMessage().getText().trim().isEmpty()) {
            String userId = update.getMessage().getFrom().getId().toString();
            String userMessage = update.getMessage().getText().trim();
            if ("candelete".equals(deleteRuleCacheMap.getDeleteKeywordFlagMap(userId))) {
                GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(deleteRuleCacheMap.getGroupIdForUser(userId));
                String settingTimestamp = groupInfoWithBLOBs.getSettingtimestamp();
                String content = groupInfoWithBLOBs.getKeywords();
                if (settingTimestamp != null && !settingTimestamp.isEmpty()) {
                    if (new Date().getTime() - Long.parseLong(settingTimestamp) > (15 * 60 * 1000)) {
                        try {
                            sender.execute(sendContent.messageText(update, "本次设置超时，请去群里重新发送/setbot"));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        deleteRuleCacheMap.updateUserMapping(userId, deleteRuleCacheMap.getGroupIdForUser(userId), deleteRuleCacheMap.getGroupNameForUser(userId), "notdelete");
                    } else {
                        if (content != null && !content.isEmpty()) {
                            List<String> keywordsButtons = new ArrayList<>();
                            KeywordsFormat keywordsFormat = new KeywordsFormat();
                            Arrays.stream(content.split("\\n{2,}"))
                                    .map(String::trim)
                                    .forEach(i -> {
                                        String[] parts = i.split(" \\| ");
                                        deleteGropuRuleMap.addRuleToGroup(deleteRuleCacheMap.getGroupIdForUser(userId),parts[0],parts[1]);
                                    });
                            keywordsFormat.setReplyText("⚠️当前页面最多显示匹配到的八条规则");
                            for (Map.Entry<String, String> entry : deleteGropuRuleMap.getAllRulesFromGroupId(deleteRuleCacheMap.getGroupIdForUser(userId)).getUuidToRuleMap().entrySet()) {
                                if (entry.getValue().split("===")[0].contains(userMessage)) {
                                    keywordsButtons.add(entry.getValue().split("===")[0] + "##" + entry.getKey().substring(0, 5));
                                }
                            }
                            keywordsButtons.add("◀️返回上一级##deleteBackToAutoReply");
                            keywordsFormat.setKeywordsButtons(keywordsButtons);
                            try {
                                sender.execute(sendContent.createResponseMessage(update, keywordsFormat, "html"));
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }

            }
        }
    }

}
