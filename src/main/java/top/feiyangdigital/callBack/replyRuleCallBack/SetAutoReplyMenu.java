package top.feiyangdigital.callBack.replyRuleCallBack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.SendContent;

import java.util.ArrayList;
import java.util.List;

@Component
public class SetAutoReplyMenu {

    @Autowired
    private SendContent sendContent;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    public void hadleCallBack(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("➕添加群组规则##addReplyRule%%📝查看所有规则##selAllReplyRules");
        keywordsButtons.add("🔍查找并删除规则##selAndDeleteReplyRule%%◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>"+ addRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ addRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前可输入状态：<b>"+ addRuleCacheMap.getKeywordsFlagForUser(userId)+"</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void addReplyRule(AbsSender sender, Update update){
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        
        addRuleCacheMap.updateUserMapping(userId, addRuleCacheMap.getGroupIdForUser(userId), addRuleCacheMap.getGroupNameForUser(userId),"allow", addRuleCacheMap.getAiFlagForUser(userId));
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🔎查看使用文档$$https://github.com/youshandefeiyang/feiyangdigital-bot");
        keywordsButtons.add("◀️返回上一级##backToAutoReply");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>"+ addRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ addRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前可输入状态：<b>"+ addRuleCacheMap.getKeywordsFlagForUser(userId)+"</b>\n⚡️请直接在输入框里输入需要添加的回复规则，\n规则必须满足要求，否则会添加失败⚡️️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void backToAutoReply(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        
        addRuleCacheMap.updateUserMapping(userId, addRuleCacheMap.getGroupIdForUser(userId), addRuleCacheMap.getGroupNameForUser(userId),"notallow", addRuleCacheMap.getAiFlagForUser(userId));
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("➕添加群组规则##addReplyRule%%📝查看所有规则##selAllReplyRules");
        keywordsButtons.add("🔍查找并删除规则##selAndDeleteReplyRule%%◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>"+ addRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ addRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前可输入状态：<b>"+ addRuleCacheMap.getKeywordsFlagForUser(userId)+"</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}