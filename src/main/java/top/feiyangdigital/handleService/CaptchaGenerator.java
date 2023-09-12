package top.feiyangdigital.handleService;

import com.pig4cloud.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import top.feiyangdigital.utils.SendContent;
import top.feiyangdigital.utils.TimerDelete;
import top.feiyangdigital.utils.groupCaptch.CaptchaManager;
import top.feiyangdigital.utils.groupCaptch.CaptchaManagerCacheMap;
import top.feiyangdigital.utils.groupCaptch.RestrictOrUnrestrictUser;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
public class CaptchaGenerator {

    @Autowired
    private SendContent sendContent;

    @Autowired
    private CaptchaManager captchaManager;

    @Autowired
    private CaptchaManagerCacheMap captchaManagerCacheMap;

    @Autowired
    private RestrictOrUnrestrictUser restrictOrUnrestrictUser;

    @Autowired
    private TimerDelete timerDelete;


    public void sendCaptcha(AbsSender sender, Long userId, String chatId,String currentChatId,String firstName) {

        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(3);
        
        

        // 保存验证码的文本值以供后续比较
        captchaManager.updateUserMapping(userId.toString(), chatId, captcha.text());


        // 使用Base64获取验证码图像的InputStream
        String base64Image = captcha.toBase64();
        base64Image = base64Image.replace("data:image/png;base64,", ""); // 移除前缀
        ByteArrayInputStream captchaStream = base64ToInputStream(base64Image);

        // 发送验证码图像到用户
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(currentChatId);
        sendPhoto.setPhoto(new InputFile(captchaStream, "captcha.png"));
        String text = String.format("请 <b><a href=\"tg://user?id=%d\">%s</a></b> 在 <b>90秒内</b> 输入计算结果，超时将永久限制发言！", userId, firstName);
        sendPhoto.setCaption(text);
        sendPhoto.setParseMode(ParseMode.HTML);

        try {
            sender.execute(sendPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void answerReplyhandle(AbsSender sender, Update update) {
        String userAnswer = update.getMessage().getText();
        String userId = update.getMessage().getFrom().getId().toString();
        String groupId = captchaManager.getGroupIdForUser(userId);
        Integer messageId = captchaManagerCacheMap.getMessageIdForUser(userId,groupId);
        Integer attempt = captchaManagerCacheMap.getAttemptForUser(userId,groupId);
        String correctAnswer = captchaManager.getAnswerForUser(userId);
        if (StringUtils.hasText(userAnswer) && !correctAnswer.isEmpty()) {
            SendMessage message;
            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                message = sendContent.messageText(update, "验证通过，现在你可以在群里自由发言了");
                restrictOrUnrestrictUser.unrestrictUser(sender,update.getMessage().getFrom().getId(),groupId);
                String text = String.format("用户 <b><a href=\"tg://user?id=%d\">%s</a></b> 验证通过，解除群组限制！", update.getMessage().getFrom().getId(), update.getMessage().getFrom().getFirstName());
                timerDelete.deleteMessageImmediatelyAndNotifyAfterDelay(sender, groupId, messageId,update.getMessage().getFrom().getId(),text);
            } else {
                captchaManagerCacheMap.updateUserMapping(userId,groupId,attempt+1,messageId);
                if (attempt>=1) {
                    try {
                        sender.execute(sendContent.messageText(update, "未通过验证，你的机会已经用尽！"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timerDelete.deleteByMessageIdImmediately(sender,groupId,messageId);
                    captchaManager.clearMappingsForUser(userId);
                    return;
                }
                message = sendContent.messageText(update, "未通过验证，请再试一次，你只有两次机会，次数用尽/超时都将会永久禁言");
            }
            try {
                sender.execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public ByteArrayInputStream base64ToInputStream(String base64) {
        
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return new ByteArrayInputStream(decodedBytes);
    }
}
