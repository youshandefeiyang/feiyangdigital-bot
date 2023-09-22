package top.feiyangdigital.utils.userNameToUserId;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import top.feiyangdigital.entity.BaseInfo;

import java.io.IOException;

@Component
@Slf4j
public class ObtainUserId {
    public JSONObject fetchUserWithOkHttp(String userName) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BaseInfo.getApiServer() + "/api/getInfo/?id=" + userName)
                .build();

        System.out.println("最终链接是："+request.url());
        try (Response response = client.newCall(request).execute()) {

            System.out.println("响应："+response);
            System.out.println("响应体："+response.body());
            if (response.body() != null) {
                String jsonStr = response.body().string();
                System.out.println("响应体字符串："+jsonStr);
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                return jsonObject.getJSONObject("response").getJSONObject("User");
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("从userName获取userId失败");
        }
        return null;
    }
}
