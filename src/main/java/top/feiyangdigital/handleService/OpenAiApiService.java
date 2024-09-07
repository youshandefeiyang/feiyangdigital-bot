package top.feiyangdigital.handleService;

import com.alibaba.fastjson2.JSONReader;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.BaseChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.interceptor.DynamicKeyOpenAiAuthInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.feiyangdigital.entity.BaseInfo;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OpenAiApiService {

    private static final String ANALYSIS_TEMPLATE = "你是一名聊天应用的群组管理员和垃圾广告检测器。你的任务是根据用户的发言内容，判断其是否为垃圾广告。\n" +
            "请根据以下标准判断垃圾广告：\n" +
            "1. 发言中是否包含以下关键词：区块链、赚钱、黑产、灰产、赌博、色情、金融、诈骗、个人隐私信息交易、虚拟货币交易或兑换？\n" +
            "2. 用户是否提供或引导他人参与非法服务或活动？例如：虚假支付信息、诱导加入群组、点击链接或参与虚假活动，涉及非法支付、赌博、贩卖禁品、推广色情内容等。\n" +
            "3. 是否有诱导用户查看个人简介、加入赚钱项目、招募人员、或提及低门槛的内容？\n" +
            "4. 发言中是否使用了符号、谐音字、特殊字符、emoji等手段来混淆内容？\n" +
            "5. 用户是否分享了知名的正规网站（如github.com、google.com、youtube.com、twitter.com等）？如果是，请谨慎判断是否为垃圾广告。\n" +
            "6. 如果用户发言非常简短，除非有明确的证据，否则不要轻易判断为广告。\n" +
            "以下是用户的发言内容：\n%s\n" +
            "请你根据上述问题，判断该发言是否为垃圾广告，并以以下JSON格式返回结果：\n" +
            "{\n" +
            "\"spamChance\": <垃圾广告的可能性，范围为\"0\"-\"10\">,\n" +
            "\"spamReason\": \"<你判断该发言是否为垃圾广告的原因>\"\n" +
            "}";


    private OpenAiClient createOpenAiClient() {
        List<String> list = BaseInfo.getOpenAIApiKey().toJavaList(String.class, JSONReader.Feature.SupportArrayToBean);

        return OpenAiClient.builder()
                .apiKey(list)
                .authInterceptor(new DynamicKeyOpenAiAuthInterceptor())
                .build();
    }

    public List<ChatChoice> getOpenAiAnalyzeResult(String text) {
        final int MAX_RETRIES = 5;
        int attempt = 0;

        while (attempt < MAX_RETRIES) {
            try {
                String content = String.format(ANALYSIS_TEMPLATE, truncateString(text, 200));
                Message message = Message.builder().role(Message.Role.SYSTEM).content(content).build();
                ChatCompletion chatCompletion = ChatCompletion.builder()
                        .maxTokens(2000)
                        .model(ChatCompletion.Model.GPT_4O_MINI.getName())
                        .temperature(0.4)
                        .topP(1.0)
                        .presencePenalty(1)
                        .messages(Collections.singletonList(message))
                        .build();
                return createOpenAiClient().chatCompletion(chatCompletion).getChoices();
            } catch (Exception e) {
                attempt++;
                log.error("OpenAI分析失败，尝试次数：{}，原因：{}", attempt, e.getMessage(), e);
                if (attempt >= MAX_RETRIES) {
                    log.error("达到最大尝试次数，停止重试。");
                    return Collections.emptyList();  // 返回一个空的列表
                }
            }
        }

        return Collections.emptyList();  // 当达到最大尝试次数时返回空列表
    }

    private String truncateString(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength);
    }
}
