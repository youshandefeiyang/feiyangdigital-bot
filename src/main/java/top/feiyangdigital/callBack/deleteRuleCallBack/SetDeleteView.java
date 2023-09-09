package top.feiyangdigital.callBack.deleteRuleCallBack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.ruleCacheMap.DeleteRuleCacheMap;

import java.util.ArrayList;
import java.util.List;

@Component
public class SetDeleteView {

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    @Autowired
    private DeleteRuleCacheMap deleteRuleCacheMap;

    @Autowired
    private SendContent sendContent;

    public void deleteRuleView(AbsSender sender, Update update){
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        
        deleteRuleCacheMap.updateUserMapping(userId, deleteRuleCacheMap.getGroupIdForUser(userId), deleteRuleCacheMap.getGroupNameForUser(userId),"candelete");
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🔎查看使用文档$$https://github.com/youshandefeiyang/feiyangdigital-bot");
        keywordsButtons.add("◀️返回上一级##backToAutoReply");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>"+ deleteRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ deleteRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前可删除状态：<b>"+ deleteRuleCacheMap.getDeleteKeywordFlagMap(userId)+"</b>\n⚡️请直接在输入框里输入删除的规则，\n将会根据输入的关键词找到对应的规则⚡️️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteBackToAutoReply(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        
        deleteRuleCacheMap.updateUserMapping(userId, deleteRuleCacheMap.getGroupIdForUser(userId), deleteRuleCacheMap.getGroupNameForUser(userId),"notdelete");
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("➕添加回复规则##addReplyRule%%📝查看所有规则##selAllReplyRules");
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

    public void deleteRuleSuccessCallBack(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        
        deleteRuleCacheMap.updateUserMapping(userId, deleteRuleCacheMap.getGroupIdForUser(userId), deleteRuleCacheMap.getGroupNameForUser(userId),"notdelete");
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("➕添加回复规则##addReplyRule%%📝查看所有规则##selAllReplyRules");
        keywordsButtons.add("🔍查找并删除规则##selAndDeleteReplyRule%%◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("✅规则删除成功\n当前群组：<b>"+ addRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ addRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前可删除状态：<b>"+ deleteRuleCacheMap.getDeleteKeywordFlagMap(userId)+"</b>");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
