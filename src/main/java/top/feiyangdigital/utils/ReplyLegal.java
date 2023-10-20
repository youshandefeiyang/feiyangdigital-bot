package top.feiyangdigital.utils;

import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Component
public class ReplyLegal {

    public boolean validateRule(String input) {
        if (input.contains(" | ")) return false;
        String[] mainParts = input.split("===");
        if (mainParts.length != 2) return false;

        try {
            Pattern.compile(mainParts[0]);
        } catch (PatternSyntaxException e) {
            return false;
        }

        String[] conditions = mainParts[1].split("&&");
        if (conditions.length < 1 || conditions.length > 4) return false;

        // 验证消息部分
        if (conditions[0].isEmpty()) return false;

        boolean hasBtns = false, hasPhoto = false, hasVideo = false;

        for (int i = 1; i < conditions.length; i++) {
            if (conditions[i].startsWith("btns=") && (conditions[i].contains("$$") || conditions[i].contains("##"))) {
                hasBtns = true;
                String[] buttonsLines = conditions[i].split("\\$\\$\\$");
                for (String buttonLine : buttonsLines) {
                    String[] buttons = buttonLine.split("%%");
                    for (String button : buttons) {
                        String[] buttonParts = button.split("\\$\\$|##");
                        if (buttonParts.length != 2) return false;
                        if (buttonParts[0].trim().isEmpty() || buttonParts[1].trim().isEmpty()) return false;
                    }
                }
            } else if (conditions[i].startsWith("photo=")) {
                hasPhoto = true;
                String photoLink = conditions[i].substring(6).trim();
                if (!photoLink.startsWith("https://")) return false; // 检查链接有效性
            } else if (conditions[i].startsWith("video=")) {
                hasVideo = true;
                String videoLink = conditions[i].substring(6).trim();
                if (!videoLink.startsWith("https://")) return false; // 检查链接有效性
            }
        }

        if (hasPhoto && hasVideo) return false;  // 如果同时存在photo和video，则返回false

        // 验证机器人操作部分
        if ((conditions.length == 2 && !hasBtns && !hasPhoto && !hasVideo) || conditions.length == 3 || conditions.length == 4) {
            String botAction = conditions[conditions.length - 1];
            String[] botActionParts = botAction.split("=");

            if (botActionParts.length == 1) return false;
            // 确保操作有效
            String operation = botActionParts[0];
            if (!Arrays.asList("del", "kick", "ban", "intoGroupBan", "crontab", "welcome").contains(operation))
                return false;

            // 如果只有操作和字符串（针对部分群组设置）
            if ((operation.equals("welcome") || operation.equals("intoGroupBan") || operation.equals("crontab")) && botActionParts.length == 2) {
                if (operation.equals("crontab")) {
                    try {
                        new CronExpression(conditions[0]);
                    } catch (Exception e) {
                        return false;
                    }
                    String[] timeLegal = botActionParts[1].split("、");
                    return timeLegal.length >= 3 && timeLegal.length <= 4;
                }
                return true;
            } else if (botActionParts.length == 2) {
                return false;
            }

            // 如果有一个操作和x值的时间设置
            if (botActionParts.length == 3) {
                // 对时间设置部分进行进一步的分析
                if (!botActionParts[1].startsWith("x")) return false;

                try {
                    int x = Integer.parseInt(botActionParts[2]);
                } catch (NumberFormatException e) {
                    return false;
                }

                return true;  // 如果上述检查都通过，则返回true
            }

            // 如果有操作、x值和y值的时间设置
            if (botActionParts.length == 4) {
                // 对时间设置部分进行进一步的分析
                if (!botActionParts[1].startsWith("x")) return false;
                String[] timeParts = botActionParts[2].split("、");
                try {
                    int x = Integer.parseInt(timeParts[0]);
                } catch (NumberFormatException e) {
                    return false;
                }
                if (!botActionParts[2].contains("y")) return false;
                try {
                    int y = Integer.parseInt(botActionParts[3]);
                } catch (NumberFormatException e) {
                    return false;
                }

                return true;  // 如果上述检查都通过，则返回true
            }
        }

        return true;  // 其他情况，返回true
    }
}