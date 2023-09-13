package top.feiyangdigital.callBack.groupSetting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.ruleCacheMap.AddRuleCacheMap;

import java.util.ArrayList;
import java.util.List;

@Component
public class SetGroupSettingView {

    @Autowired
    private SendContent sendContent;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private AddRuleCacheMap addRuleCacheMap;

    public void hadleCallBack(AbsSender sender, Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🔑是/否进群验证##changeGroupCheckStatus%%🎉是/否设置进群欢迎词##changeGroupWelcomeStatus");
        keywordsButtons.add("🔍是/否进群昵称违规检测##xxxxxx%%◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText("当前群组：<b>"+ addRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ addRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前群组验证状态：<b>"+ groupInfoWithBLOBs.getIntogroupcheckflag() +"</b>\n当前进群欢迎状态：<b>"+ groupInfoWithBLOBs.getIntogroupwelcomeflag() +"</b>\n当前进群昵称验证状态：<b>"+ groupInfoWithBLOBs.getIntogroupusernamecheckflag() +"</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void changeGroupWelcomeStatus(AbsSender sender ,Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String intoGroupWelcome = "";
        String text = "";
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        if ("close".equals(groupInfoWithBLOBs.getIntogroupwelcomeflag())){
            groupInfoWithBLOBs1.setIntogroupwelcomeflag("open");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1,addRuleCacheMap.getGroupIdForUser(userId))){
                intoGroupWelcome = "open";
                text = "✅进群欢迎功能已打开";
            }
        }else {
            groupInfoWithBLOBs1.setIntogroupwelcomeflag("close");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1,addRuleCacheMap.getGroupIdForUser(userId))){
                intoGroupWelcome = "close";
                text = "❗️进群欢迎功能已关闭";
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🔑打开/关闭进群验证##changeGroupCheckStatus%%🎉设置进群欢迎词##changeGroupWelcomeStatus");
        keywordsButtons.add("🔍进群昵称违规检测##xxxxxx%%◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text+"\n当前群组：<b>"+ addRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ addRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前群组验证状态：<b>"+ groupInfoWithBLOBs.getIntogroupcheckflag() +"</b>\n当前进群欢迎状态：<b>"+ intoGroupWelcome +"</b>\n当前进群昵称验证状态：<b>"+ groupInfoWithBLOBs.getIntogroupusernamecheckflag() +"</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void changeGroupCheckStatus(AbsSender sender ,Update update) {
        String userId = update.getCallbackQuery().getFrom().getId().toString();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(addRuleCacheMap.getGroupIdForUser(userId));
        String intoGroupCheckFlag = "";
        String text = "";
        GroupInfoWithBLOBs groupInfoWithBLOBs1 = new GroupInfoWithBLOBs();
        if ("close".equals(groupInfoWithBLOBs.getIntogroupcheckflag())){
            groupInfoWithBLOBs1.setIntogroupcheckflag("open");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1,addRuleCacheMap.getGroupIdForUser(userId))){
                intoGroupCheckFlag = "open";
                text = "✅进群验证功能已打开";
            }
        }else {
            groupInfoWithBLOBs1.setIntogroupcheckflag("close");
            if (groupInfoService.updateSelectiveByChatId(groupInfoWithBLOBs1,addRuleCacheMap.getGroupIdForUser(userId))){
                intoGroupCheckFlag = "close";
                text = "❗️进群验证功能已关闭";
            }
        }
        List<String> keywordsButtons = new ArrayList<>();
        KeywordsFormat keywordsFormat = new KeywordsFormat();
        keywordsButtons.add("🔑打开/关闭进群验证##changeGroupCheckStatus%%🎉设置进群欢迎词##changeGroupWelcomeStatus");
        keywordsButtons.add("🔍进群昵称违规检测##xxxxxx%%◀️返回主菜单##backMainMenu");
        keywordsButtons.add("❌关闭菜单##closeMenu");
        keywordsFormat.setReplyText(text+"\n当前群组：<b>"+ addRuleCacheMap.getGroupNameForUser(userId)+"</b>\n当前群组ID：<b>"+ addRuleCacheMap.getGroupIdForUser(userId)+"</b>\n当前群组验证状态：<b>"+ intoGroupCheckFlag +"</b>\n当前进群欢迎状态：<b>"+ groupInfoWithBLOBs.getIntogroupwelcomeflag() +"</b>\n当前进群昵称验证状态：<b>"+ groupInfoWithBLOBs.getIntogroupusernamecheckflag() +"</b>\n⚡️请选择一个操作!⚡️");
        keywordsFormat.setKeywordsButtons(keywordsButtons);
        try {
            sender.execute(sendContent.editResponseMessage(update, keywordsFormat, "html"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
