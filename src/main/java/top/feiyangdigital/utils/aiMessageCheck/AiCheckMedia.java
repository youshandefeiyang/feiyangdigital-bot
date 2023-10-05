package top.feiyangdigital.utils.aiMessageCheck;

import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.feiyangdigital.entity.BaseInfo;
import top.feiyangdigital.entity.BotRecord;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.handleService.GoogleCloudVisionService;
import top.feiyangdigital.sqlService.BotRecordService;
import top.feiyangdigital.sqlService.GroupInfoService;
import top.feiyangdigital.utils.TimerDelete;
import top.feiyangdigital.utils.groupCaptch.RestrictOrUnrestrictUser;

import java.io.File;
import java.util.List;

@Component
public class AiCheckMedia {

    @Autowired
    private TimerDelete timerDelete;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private BotRecordService botRecordService;

    @Autowired
    private RestrictOrUnrestrictUser restrictOrUnrestrictUser;

    @Autowired
    private GoogleCloudVisionService googleCloudVisionService;

    @Autowired
    private AiCheckMessage aiCheckMessage;

    public void checkMedia(AbsSender sender, Update update) throws TelegramApiException {
        String groupId = update.getMessage().getChatId().toString();
        String userId = update.getMessage().getFrom().getId().toString();
        Integer messageId = update.getMessage().getMessageId();
        String firstName = update.getMessage().getFrom().getFirstName();
        GroupInfoWithBLOBs groupInfoWithBLOBs = groupInfoService.selAllByGroupId(groupId);
        if (groupInfoWithBLOBs != null && "open".equals(groupInfoWithBLOBs.getAiflag())) {
            BotRecord botRecord = botRecordService.selBotRecordByGidAndUid(groupId, userId);
            if (botRecord != null) {
                Integer violationCount = botRecord.getViolationcount();
                Integer normalCount = botRecord.getNormalcount();
                if (violationCount >= 5) {
                    String text = String.format("用户 <b><a href=\"tg://user?id=%d\">%s</a></b> 已被AI检测违规超过5次，永久限制发言！", Long.valueOf(userId), firstName);
                    String otherText = String.format("<b>违规用户UserID为：<a href=\"tg://user?id=%d\">%s</a></b>", Long.valueOf(userId), userId);
                    SendMessage notification = new SendMessage();
                    notification.setChatId(groupId);
                    notification.setText(text+"\n"+otherText);
                    notification.setParseMode(ParseMode.HTML);
                    timerDelete.deleteMessageImmediatelyAndNotifyAfterDelay(sender, notification, groupId, messageId, Long.valueOf(userId), 90);
                    restrictOrUnrestrictUser.restrictUser(sender, Long.valueOf(userId), groupId);
                    return;
                } else if (normalCount >= 5) {
                    return;
                }
                String fileId = "";
                if (update.getMessage().hasPhoto()) {
                    fileId = update.getMessage().getPhoto().get(update.getMessage().getPhoto().size() - 1).getFileId();
                } else if (update.getMessage().hasDocument() && update.getMessage().getDocument().getThumbnail() != null) {
                    fileId = update.getMessage().getDocument().getThumbnail().getFileId();
                } else if (update.getMessage().hasSticker() && update.getMessage().getSticker().getThumbnail() != null) {
                    fileId = update.getMessage().getSticker().getThumbnail().getFileId();
                } else if ((update.getMessage().hasVideo() || update.getMessage().hasVideoNote()) && (update.getMessage().getVideo().getThumbnail() != null || update.getMessage().getVideoNote().getThumbnail() != null)) {
                    if (update.getMessage().getVideo().getThumbnail() != null) {
                        fileId = update.getMessage().getVideo().getThumbnail().getFileId();
                    } else if (update.getMessage().getVideoNote().getThumbnail() != null) {
                        fileId = update.getMessage().getVideoNote().getThumbnail().getFileId();
                    }
                }
                if (StringUtils.hasText(fileId)) {
                    GetFile getFile = GetFile.builder()
                            .fileId(fileId)
                            .build();
                    String url = sender.execute(getFile).getFileUrl(BaseInfo.getBotToken());
                    File file = googleCloudVisionService.downloadFileWithOkHttp(url);
                    String miaoshu = "";
                    List<EntityAnnotation> list = googleCloudVisionService.detectTextFromLocalImage(file);
                    if (!list.isEmpty()) {
                        miaoshu = list.get(0).getDescription();
                    }
                    SafeSearchAnnotation safeSearchAnnotation = googleCloudVisionService.detectSafeSearchFromLocalImage(file);
                    BotRecord botRecord1 = new BotRecord();
                    String realUpdateText = miaoshu;
                    if (StringUtils.hasText(update.getMessage().getCaption())) {
                        realUpdateText += "\n" + update.getMessage().getCaption();
                    }
                    update.getMessage().setText(realUpdateText);
                    String content = update.getMessage().getText();
                    if (safeSearchAnnotation.getAdultValue() >= 3 || safeSearchAnnotation.getViolenceValue() >= 3 || safeSearchAnnotation.getRacyValue() >= 3) {
                        String text = String.format("用户 <b><a href=\"tg://user?id=%d\">%s</a></b> 已被AI检测发送违规媒体，直接删除！", Long.valueOf(userId), firstName);
                        String otherText = String.format("<b>违规用户UserID为：<a href=\"tg://user?id=%d\">%s</a></b>", Long.valueOf(userId), userId);
                        SendMessage notification = new SendMessage();
                        notification.setChatId(groupId);
                        notification.setText(text+"\n"+otherText);
                        notification.setParseMode(ParseMode.HTML);
                        timerDelete.deleteMessageImmediatelyAndNotifyAfterDelay(sender, notification, groupId, messageId, Long.valueOf(userId), 90);
                        botRecord1.setViolationcount(violationCount + 1);
                    } else if (StringUtils.hasText(realUpdateText)) {
                        aiCheckMessage.contentAiOption(sender, groupId, userId, firstName, messageId, realUpdateText);
                        if (file != null) {
                            file.delete();
                        }
                        return;
                    } else {
                        botRecord1.setNormalcount(normalCount + 1);
                    }
                    botRecord1.setLastmessage(content);
                    botRecordService.updateRecordByGidAndUid(groupId, userId, botRecord1);
                    if (file != null) {
                        file.delete();
                    }
                }
            }
        }
    }

}
