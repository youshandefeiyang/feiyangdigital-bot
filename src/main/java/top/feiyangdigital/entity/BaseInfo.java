package top.feiyangdigital.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class BaseInfo {
    private static final String CONFIG_PATH = String.valueOf(Paths.get("").toAbsolutePath().resolve("config.json"));
    private static final JSONObject CONFIG;

    static {
        try {
            CONFIG = readJson(CONFIG_PATH);
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件失败 " + CONFIG_PATH, e);
        }
    }

    public static JSONObject readJson(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return JSON.parseObject(sb.toString());
        }
    }

    public static String getConfigValue(String key, String subKey) {
        JSONObject subConfig = CONFIG.getJSONObject(key);
        return (subConfig != null) ? subConfig.getString(subKey) : null;
    }

    public static String getBotName() {
        return getConfigValue("botConfig", "name");
    }

    public static String getBotToken() {
        return getConfigValue("botConfig", "token");
    }
}
