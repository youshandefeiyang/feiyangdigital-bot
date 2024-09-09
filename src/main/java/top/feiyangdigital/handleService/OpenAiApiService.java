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

    private static final String ANALYSIS_TEMPLATE = "你是一名聊天应用的群组管理员和垃圾信息检测器。你的任务是根据用户发言内容，判断其是否涉及垃圾广告、黄赌毒、引流、非法服务、虚拟货币交易、黑灰产、以及壮阳、性用品、迷药、夸张致富广告等内容。\n" +
            "请根据以下问题逐项进行详细判断：\n" +
            "1. **是否包含敏感关键词**：发言中是否提及以下关键词或相关变种词：区块链、虚拟货币（如比特币、以太坊等）、赚钱、黑产、灰产、赌博、色情、毒品、金融诈骗、个人隐私交易、洗钱、快速致富、暴富、提现、壮阳药、性用品、迷药等？\n" +
            "2. **非法服务或活动**：用户是否提供或引导他人参与非法服务或活动，例如：\n" +
            "   - 提供虚假支付信息或银行卡号；\n" +
            "   - 诱导用户加入社交群组（不限于QQ、微信，涵盖任何社交平台群，如Telegram、Discord等），点击虚假链接或参与欺诈活动；\n" +
            "   - 涉及赌博平台、色情服务、毒品交易、贩卖非法物品、虚拟货币交易所等；\n" +
            "   - 涉嫌黑灰产，例如虚假票据、代开发票、黑客攻击服务等。\n" +
            "3. **引流和推广**：\n" +
            "   - 用户是否试图诱导他人点击个人简介、私信联系、加好友、或加入任何社交群组（包括但不限于QQ、微信、Telegram、Discord等）；\n" +
            "   - 是否推广某些平台、项目、APP下载链接，或通过发放红包、优惠券吸引用户；\n" +
            "   - 是否推广高风险金融产品、投资项目或类似传销的内容。\n" +
            "4. **色情与性用品相关内容**：\n" +
            "   - 发言中是否包含任何形式的色情内容（如图片、视频、网站链接），或涉及性交易、成人服务、性用品（如壮阳药、情趣用品等）的推广；\n" +
            "   - 是否推广成人内容平台，或暗示性服务（如“约炮”、“裸聊”、“一夜情”等）。\n" +
            "5. **赌博相关内容**：\n" +
            "   - 发言中是否提及赌博相关内容，包括在线赌博平台、赌博游戏、线上赌场、彩票或博彩活动；\n" +
            "   - 是否推广赌博网站或提供赌博技术支持（如破解系统、投注建议等）。\n" +
            "6. **毒品及迷药相关内容**：\n" +
            "   - 发言中是否涉及毒品交易、使用、贩卖，或迷药（如迷奸药物）的推广；\n" +
            "   - 是否提供获取毒品或迷药的方式或渠道，或讨论毒品购买、运输、使用的细节。\n" +
            "7. **视觉混淆手段**：用户是否使用特殊符号、变体字、谐音字、特殊字符、拼音或emoji等方式试图绕过内容过滤？例如使用“曰结”代替“日结”，以及使用“橡木”代替“项目”，或“zhuāngyáng”代替“壮阳”。\n" +
            "8. **夸张致富承诺**：\n" +
            "   - 用户是否使用了不切实际或夸张的致富承诺，例如“踏实肯干几天破万”、“轻松几天买车买房”、“快速致富，无需经验”等；\n" +
            "   - 这种信息通常具有误导性，常见于非法投资、传销、虚假项目推广。\n" +
            "9. **网站分享**：\n" +
            "   - 用户分享的链接是否来自知名的正规网站（如github.com、google.com、youtube.com、twitter.com）？如果是，请谨慎判断是否为广告；\n" +
            "   - 如果链接来自不知名或可疑网站，尤其是涉及博彩、色情、毒品、虚拟货币交易、黑灰产等内容，需严格审查。\n" +
            "10. **发言长度与频率**：\n" +
            "   - 用户的发言是否过短且没有实质内容，或反复发送相同信息？\n" +
            "   - 频繁发送短信息（如链接或联系方式），通常表明具有广告或引流性质。\n" +
            "11. **隐蔽推销与暗语**：\n" +
            "   - 用户是否通过隐晦语言或模糊词语推销产品或服务，或暗示违法活动（如“搞钱”、“有项目”、“招人”等）。\n" +
            "12. **虚拟货币和投资陷阱**：\n" +
            "   - 是否涉及虚拟货币的非法交易、币圈诈骗、挖矿骗局等内容；\n" +
            "   - 是否推销不明来源的投资机会、快速致富计划，或承诺高额回报的投资。\n" +
            "13. **加入社交群组的诱导**：用户是否通过各种方式（如红包、承诺高回报、免费服务等）诱导他人加入任何社交平台的群组（如QQ群、微信群、Telegram群、Discord群等）？\n" +
            "以下是用户的发言内容：\n%s\n" +
            "请根据以上问题逐一判断，并以如下JSON格式返回结果：\n" +
            "{\n" +
            "\"spamChance\": <垃圾广告的可能性，范围为\"0\"-\"10\">,\n" +
            "\"spamReason\": \"<你判断该发言是否为垃圾广告或违法内容的原因>\"\n" +
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
