package top.feiyangdigital.handleService;

import com.alibaba.fastjson2.JSONReader;
import com.unfbx.chatgpt.OpenAiClient;
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

    private static final String ANALYSIS_TEMPLATE = "You are a chat app group administrator and spam detector,your\n" +
            "job is to determine whether a user's speech is spam advertising based on the content of their statements,\n" +
            "以下是我们专门关注的垃圾广告特征：\n" +
            "- 使用以下关键词：区块链、黑产、灰产、赌博、色情、金融、诈骗、个人隐私信息交易，虚拟货币交易或兑换；\n" +
            "- 提供或引导用户使用非法服务或进行非法活动，如包含虚假支付机构或银行卡信息、诱导用户加入群组、点击链接或参与虚假活动、涉及非法支付、赌博、贩卖、引流推广、禁止物品、色情内容等。\n" +
            "- 使用视觉混淆手段：在内容中插入符号、谐音字、特殊字符、emoji等；\n"+
            "- 如果用户分享的是知名和正规网站，如：github.com、google.com、youtube.com、twitter.com之类，应该谨慎判定为广告。\n"+
            "- 如果用户发言很短，除非有十分确切的原因，否则应该尽量避免认定其为广告。\n"+
            "这是我提供的用户发言详情：\n%s\n" +
            "请你对这个发言进行判断，是否可以被视为垃圾广告，并以如下JSOM格式返回你的答案" +
            "{\n" +
            "\"spamChance\"：<垃圾广告的概率，范围是\"0\"-\"10\">，\n" +
            "\"spamReason\"：\"<你对这个发言为何是或不是垃圾广告的原因>\n" +
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
                        .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
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
