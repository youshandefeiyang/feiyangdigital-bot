package top.feiyangdigital.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import top.feiyangdigital.entity.KeywordsFormat;

import java.util.ArrayList;
import java.util.List;

@Component
public class SendContent {
    public SendMessage messageText(Update update,String text){
        String chatId;
        if (update.getMessage() == null) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        } else {
            chatId = update.getMessage().getChatId().toString();
        }
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }

    public EditMessageText editMessageText(Update update, String text){
        String chatId = "";
        Integer messageId = 0;

        if (update.getCallbackQuery() != null) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            messageId = ((Message)update.getCallbackQuery().getMessage()).getMessageId();
        } else if (update.getMessage() != null) {
            chatId = update.getMessage().getChatId().toString();
            messageId = update.getMessage().getMessageId();
        }
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setText(text);
        return message;
    }

    public Object createResponseMessage(Update update, KeywordsFormat keyword,String textFormat) {
        String chatId;
        if (update.getMessage() == null) {
            if (update.getCallbackQuery() ==null){
                chatId = update.getChatMember().getChat().getId().toString();
            }else {
                chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            }
        } else {
            chatId = update.getMessage().getChatId().toString();
        }
        String replyText = keyword.getReplyText();
        List<String> keywordsButtons = keyword.getKeywordsButtons();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonLine : keywordsButtons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttons = buttonLine.split("%%");

            for (String btnData : buttons) {
                InlineKeyboardButton button = new InlineKeyboardButton();

                if (btnData.contains("$$")) {
                    String[] buttonParts = btnData.split("\\$\\$");
                    if (buttonParts.length == 2) {
                        button.setText(buttonParts[0]);
                        button.setUrl(buttonParts[1]);
                    }
                } else if (btnData.contains("##")) {
                    String[] buttonParts = btnData.split("##");
                    if (buttonParts.length == 2) {
                        button.setText(buttonParts[0]);
                        button.setCallbackData(buttonParts[1]);
                    }
                }

                row.add(button);
            }

            keyboard.add(row);
        }

        // 设置文本格式
        String formattedText = replyText;
        String parseMode = null;
        switch (textFormat) {
            case "markdown":
                parseMode = ParseMode.MARKDOWN;
                break;
            case "markdownV2":
                formattedText = escapeMarkdownV2(replyText);
                parseMode = ParseMode.MARKDOWNV2;
                break;
            case "html":
                parseMode = ParseMode.HTML;
                break;
            default:
                // 使用原始文本，不设置解析模式
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // 检查图片URL
        if (StringUtils.hasText(keyword.getPhotoUrl())) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            InputFile inputFile = new InputFile();
            inputFile.setMedia(keyword.getPhotoUrl());
            sendPhoto.setPhoto(inputFile);
            sendPhoto.setCaption(formattedText);
            sendPhoto.setReplyMarkup(markup);
            if (parseMode != null) {
                sendPhoto.setParseMode(parseMode);
            }
            return sendPhoto;
        }
        // 检查视频URL
        else if (StringUtils.hasText(keyword.getVideoUrl())) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(chatId);
            InputFile inputFile = new InputFile();
            inputFile.setMedia(keyword.getVideoUrl());
            sendVideo.setVideo(inputFile);
            sendVideo.setCaption(formattedText);
            sendVideo.setReplyMarkup(markup);
            if (parseMode != null) {
                sendVideo.setParseMode(parseMode);
            }
            return sendVideo;
        }
        // 默认返回一个SendMessage对象
        else {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(formattedText);
            message.setReplyMarkup(markup);
            if (parseMode != null) {
                message.setParseMode(parseMode);
            }
            return message;
        }
    }

    public EditMessageText editResponseMessage(Update update, KeywordsFormat keyword, String textFormat) {
        String chatId = "";
        Integer messageId = 0;

        if (update.getCallbackQuery() != null) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            messageId = ((Message)update.getCallbackQuery().getMessage()).getMessageId();
        } else if (update.getMessage() != null) {
            chatId = update.getMessage().getChatId().toString();
            messageId = update.getMessage().getMessageId();
        }

        String replyText = keyword.getReplyText();
        List<String> keywordsButtons = keyword.getKeywordsButtons();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonLine : keywordsButtons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttons = buttonLine.split("%%");

            for (String btnData : buttons) {
                InlineKeyboardButton button = new InlineKeyboardButton();

                if (btnData.contains("$$")) {
                    String[] buttonParts = btnData.split("\\$\\$");
                    if (buttonParts.length == 2) {
                        button.setText(buttonParts[0]);
                        button.setUrl(buttonParts[1]);
                    }
                } else if (btnData.contains("##")) {
                    String[] buttonParts = btnData.split("##");
                    if (buttonParts.length == 2) {
                        button.setText(buttonParts[0]);
                        button.setCallbackData(buttonParts[1]);
                    }
                }

                row.add(button);
            }

            keyboard.add(row);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);

        switch (textFormat) {
            case "markdown":
                editMessage.setText(replyText);
                editMessage.setParseMode(ParseMode.MARKDOWN);
                break;
            case "markdownV2":
                editMessage.setText(escapeMarkdownV2(replyText));
                editMessage.setParseMode(ParseMode.MARKDOWNV2);
                break;
            case "html":
                editMessage.setText(replyText);
                editMessage.setParseMode(ParseMode.HTML);
                break;
            default:
                editMessage.setText(replyText);
        }

        editMessage.setReplyMarkup(markup);

        return editMessage;
    }


    public Object replyToUser(Update update, KeywordsFormat keyword,String textFormat) {
        String replyText = keyword.getReplyText();
        List<String> keywordsButtons = keyword.getKeywordsButtons();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonLine : keywordsButtons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttons = buttonLine.split("%%");

            for (String btnData : buttons) {
                InlineKeyboardButton button = new InlineKeyboardButton();

                if (btnData.contains("$$")) {
                    String[] buttonParts = btnData.split("\\$\\$");
                    if (buttonParts.length == 2) {
                        button.setText(buttonParts[0]);
                        button.setUrl(buttonParts[1]);
                    }
                } else if (btnData.contains("##")) {
                    String[] buttonParts = btnData.split("##");
                    if (buttonParts.length == 2) {
                        button.setText(buttonParts[0]);
                        button.setCallbackData(buttonParts[1]);
                    }
                }

                row.add(button);
            }

            keyboard.add(row);
        }

        String chatId = update.getMessage().getChatId().toString();
        Integer messageId = update.getMessage().getMessageId();
        String formattedText = replyText;
        String parseMode = null;
        switch (textFormat) {
            case "markdown":
                parseMode = ParseMode.MARKDOWN;
                break;
            case "markdownV2":
                formattedText = escapeMarkdownV2(replyText);
                parseMode = ParseMode.MARKDOWNV2;
                break;
            case "html":
                parseMode = ParseMode.HTML;
                break;
            default:
                // 使用原始文本，不设置解析模式
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // 检查图片URL
        if (StringUtils.hasText(keyword.getPhotoUrl())) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setReplyToMessageId(messageId);
            InputFile inputFile = new InputFile();
            inputFile.setMedia(keyword.getPhotoUrl());
            sendPhoto.setPhoto(inputFile);
            sendPhoto.setCaption(formattedText);
            sendPhoto.setReplyMarkup(markup);
            if (parseMode != null) {
                sendPhoto.setParseMode(parseMode);
            }
            return sendPhoto;
        }
        // 检查视频URL
        else if (StringUtils.hasText(keyword.getVideoUrl())) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(chatId);
            sendVideo.setReplyToMessageId(messageId);
            InputFile inputFile = new InputFile();
            inputFile.setMedia(keyword.getVideoUrl());
            sendVideo.setVideo(inputFile);
            sendVideo.setCaption(formattedText);
            sendVideo.setReplyMarkup(markup);
            if (parseMode != null) {
                sendVideo.setParseMode(parseMode);
            }
            return sendVideo;
        }
        // 默认返回一个SendMessage对象
        else {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setReplyToMessageId(messageId);
            message.setText(formattedText);
            message.setReplyMarkup(markup);
            if (parseMode != null) {
                message.setParseMode(parseMode);
            }
            return message;
        }
    }

    public Object createGroupMessage(String chatId, KeywordsFormat keyword,String textFormat) {
        String replyText = keyword.getReplyText();
        List<String> keywordsButtons = keyword.getKeywordsButtons();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonLine : keywordsButtons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttons = buttonLine.split("%%");

            for (String btnData : buttons) {
                InlineKeyboardButton button = new InlineKeyboardButton();

                if (btnData.contains("$$")) {
                    String[] buttonParts = btnData.split("\\$\\$");
                    if (buttonParts.length == 2) {
                        button.setText(buttonParts[0]);
                        button.setUrl(buttonParts[1]);
                    }
                } else if (btnData.contains("##")) {
                    String[] buttonParts = btnData.split("##");
                    if (buttonParts.length == 2) {
                        button.setText(buttonParts[0]);
                        button.setCallbackData(buttonParts[1]);
                    }
                }

                row.add(button);
            }

            keyboard.add(row);
        }

        // 设置文本格式
        String formattedText = replyText;
        String parseMode = null;
        switch (textFormat) {
            case "markdown":
                parseMode = ParseMode.MARKDOWN;
                break;
            case "markdownV2":
                formattedText = escapeMarkdownV2(replyText);
                parseMode = ParseMode.MARKDOWNV2;
                break;
            case "html":
                parseMode = ParseMode.HTML;
                break;
            default:
                // 使用原始文本，不设置解析模式
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // 检查图片URL
        if (StringUtils.hasText(keyword.getPhotoUrl())) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            InputFile inputFile = new InputFile();
            inputFile.setMedia(keyword.getPhotoUrl());
            sendPhoto.setPhoto(inputFile);
            sendPhoto.setCaption(formattedText);
            sendPhoto.setReplyMarkup(markup);
            if (parseMode != null) {
                sendPhoto.setParseMode(parseMode);
            }
            return sendPhoto;
        }
        // 检查视频URL
        else if (StringUtils.hasText(keyword.getVideoUrl())) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(chatId);
            InputFile inputFile = new InputFile();
            inputFile.setMedia(keyword.getVideoUrl());
            sendVideo.setVideo(inputFile);
            sendVideo.setCaption(formattedText);
            sendVideo.setReplyMarkup(markup);
            if (parseMode != null) {
                sendVideo.setParseMode(parseMode);
            }
            return sendVideo;
        }
        // 默认返回一个SendMessage对象
        else {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(formattedText);
            message.setReplyMarkup(markup);
            if (parseMode != null) {
                message.setParseMode(parseMode);
            }
            return message;
        }
    }

    private String escapeMarkdownV2(String text) {
        String[] symbols = new String[]{"_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!"};

        for (String symbol : symbols) {
            text = text.replace(symbol, "\\" + symbol);
        }

        return text;
    }
}