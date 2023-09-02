package top.feiyangdigital.entity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class KeywordsFormat {
    private String uuid;
    private String regex;
    private String replyText;

    private List<String> keywordsButtons = new ArrayList<>();

    private Map<String, String> delMap = new ConcurrentHashMap<>();

    public KeywordsFormat(){

    }

    public KeywordsFormat(String data) {
        String[] keyValuePair = data.split(" \\| ");
        this.uuid = keyValuePair[0];

        String[] parts = keyValuePair[1].split("===");
        this.regex = parts[0];
        String[] actions = parts[1].split("&&");
        this.replyText = actions[0];
        for (String action : actions) {
            if (action.startsWith("btns=")) {
                String[] btnParts = action.substring(5).split("\\$\\$\\$");
                keywordsButtons.addAll(Arrays.asList(btnParts));
            } else if (action.startsWith("del=")) {
                delMap.put("DeleteAfterXSeconds", action.substring(4).split("、")[0].split("=")[1]);
                delMap.put("DeleteReplyAfterYSeconds", action.substring(4).split("、")[1].split("=")[1]);
            }
        }
    }

    // Getter methods...

    public String getUuid() {
        return uuid;
    }

    public String getRegex() {
        return regex;
    }

    public String getReplyText() {
        return replyText;
    }

    public List<String> getKeywordsButtons() {
        return keywordsButtons;
    }

    public Map<String, String> getDelMap() {
        return delMap;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public void setKeywordsButtons(List<String> keywordsButtons) {
        this.keywordsButtons = keywordsButtons;
    }

    public void setDelMap(Map<String, String> map) {
        this.delMap = map;
    }

    @Override
    public String toString() {
        return "KeywordsFormat{" +
                "uuid='" + uuid + '\'' +
                ", regex='" + regex + '\'' +
                ", replyText='" + replyText + '\'' +
                ", keywordsButtons=" + keywordsButtons +
                ", map=" + delMap +
                '}';
    }
}
