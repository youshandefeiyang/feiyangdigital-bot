package top.feiyangdigital.callBack.replyRuleCallBack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.scheduledTasks.HandleOption;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.ReplyLegal;
import top.feiyangdigital.utils.SendContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class AddAutoReplyRule {

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private SendContent sendContent;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    @Autowired
    private ReplyLegal replyLegal;
    @Autowired
    private HandleOption handleOption;

    public void addNewRule(AbsSender sender, Update update) throws TelegramApiException {
        if (update.getMessage().getText() != null && !update.getMessage().getText().trim().isEmpty()) {
            String userId = update.getMessage().getFrom().getId().toString();
            String newRule = update.getMessage().getText().trim();
            if (replyLegal.validateRule(newRule)) {
                if ("allow".equals(addRuleCacheMap.getKeywordsFlagForUser(userId))) {
                    GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
                    String settingTimestamp = groupInfoWithBLOBs.getSettingtimestamp();
                    if (settingTimestamp != null && !settingTimestamp.isEmpty()) {
                        if (new Date().getTime() - Long.parseLong(settingTimestamp) > (15 * 60 * 1000)) {
                            sender.execute(sendContent.messageText(update, "本次设置超时，请去群里重新发送/setbot"));
                            addRuleCacheMap.updateUserMapping(userId, addRuleCacheMap.getGroupIdForUser(userId), addRuleCacheMap.getGroupNameForUser(userId), "notallow", addRuleCacheMap.getAiFlagForUser(userId), addRuleCacheMap.getCrontabFlagForUser(userId));
                        } else {
                            String waitRule = UUID.randomUUID().toString() + " | " + newRule;
                            String oldContent = groupInfoWithBLOBs.getKeywords();
                            String newContent;
                            if (oldContent == null || oldContent.isEmpty()) {
                                newContent = waitRule;
                            } else {
                                if (oldContent.contains("&&welcome=") && newRule.contains("&&welcome=")) {
                                    sender.execute(sendContent.messageText(update, "欢迎词只能设置一个！"));
                                    return;
                                }
                                newContent = groupInfoWithBLOBs.getKeywords() + ("\n\n" + waitRule);
                            }
                            GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
                            groupInfoWithBLOBs1.setKeywords(newContent);
                            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1, addRuleCacheMap.getGroupIdForUser(userId))) {
                                GroupInfoWithBLOBs groupInfoWithBLOBs2 = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
                                String keyWords = groupInfoWithBLOBs2.getKeywords();
                                if (StringUtils.hasText(keyWords)) {
                                    handleOption.ruleHandle(sender, addRuleCacheMap.getGroupIdForUser(userId), keyWords);
                                }
                                sender.execute(sendContent.createResponseMessage(update, new KeywordsFormat(waitRule), "html"));
                            }
                        }
                    }

                }
            } else {
                List<String> keywordsButtons = new ArrayList<>();
                KeywordsFormat keywordsFormat = new KeywordsFormat();
                keywordsButtons.add("🔎查看使用文档$$https://github.com/youshandefeiyang/feiyangdigital-bot");
                keywordsButtons.add("◀️返回上一级##backToAutoReply");
                keywordsButtons.add("❌关闭菜单##closeMenu");
                keywordsFormat.setReplyText("⚡️<b>规则不合法，请重新添加！</b>⚡️\n当前群组：<b>" + addRuleCacheMap.getGroupNameForUser(userId) + "</b>\n当前群组ID：<b>" + addRuleCacheMap.getGroupIdForUser(userId) + "</b>\n当前可输入状态：<b>" + addRuleCacheMap.getKeywordsFlagForUser(userId) + "</b>️");
                keywordsFormat.setKeywordsButtons(keywordsButtons);
                sender.execute(sendContent.createResponseMessage(update, keywordsFormat, "html"));
            }
        }
    }
}
