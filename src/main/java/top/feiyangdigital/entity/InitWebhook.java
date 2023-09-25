package top.feiyangdigital.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.util.List;

public class InitWebhook {
    private static final OkHttpClient client = new OkHttpClient();

    public static boolean diySetWebhook(String botToken, String webhookUrl, List<String> allowedUpdates) {
        // 将 allowed_updates 集合转换为 JSON 字符串
        String allowedUpdatesJson = JSON.toJSONString(allowedUpdates);

        // 创建请求体，包含你想要设置的webhook的URL和 allowed_updates
        RequestBody requestBody = new FormBody.Builder()
                .add("url", webhookUrl)
                .add("allowed_updates", allowedUpdatesJson)
                .build();

        // 创建POST请求到Telegram API的setWebhook端点
        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot" + botToken + "/setWebhook")
                .post(requestBody)
                .build();

        try {
            // 执行请求并获取响应
            Response response = client.newCall(request).execute();
            // 检查响应是否成功
            if (response.isSuccessful()) {
                // 解析响应体
                String responseBody = response.body().string();
                // 使用 fastjson 将响应体解析为JSON对象
                JSONObject jsonResponse = JSONObject.parseObject(responseBody);
                // 检查"ok"字段是否为true
                return jsonResponse.getBooleanValue("ok");
            } else {
                return false;
            }
        } catch (Exception e) {
            // 如果执行请求时出现异常，打印错误信息
            e.printStackTrace();
            return false;
        }
    }
}
