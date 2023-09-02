package top.feiyangdigital.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseInfo {
    private static final String configPath = String.valueOf(Paths.get("").toAbsolutePath().resolve("config.json"));

    public static JSONObject readJson(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();

        return JSON.parseObject(sb.toString());
    }
    public static Map<String,String> getConfig() {
        Map<String,String> map = new ConcurrentHashMap<>();
        try {
            JSONObject config = readJson(configPath);
            map.put("botName",String.valueOf(config.getJSONObject("botConfig").get("name")));
            map.put("botToken",String.valueOf(config.getJSONObject("botConfig").get("token")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String getBotName() {
        return getConfig().get("botName");
    }

    public static String getBotToken() {
        return getConfig().get("botToken");
    }
}
