package top.feiyangdigital.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import top.feiyangdigital.entity.KeywordsFormat;

import java.util.ArrayList;
import java.util.List;

@Component
public class SendContent {
    public SendMessage messageText(Update update,String text){
        String chatId = "";
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
            messageId = update.getCallbackQuery().getMessage().getMessageId();
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

    public SendMessage createResponseMessage(Update update, KeywordsFormat keyword,String textFormat) {
        String chatId = "";
        if (update.getMessage() == null) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
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

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        switch (textFormat) {
            case "markdown":
                message.setText(replyText);
                message.setParseMode(ParseMode.MARKDOWN);
                break;
            case "markdownV2":
                message.setText(escapeMarkdownV2(replyText));
                message.setParseMode(ParseMode.MARKDOWNV2);
                break;
            case "html":
                message.setText(replyText);
                message.setParseMode(ParseMode.HTML);
                break;
            default:
                message.setText(replyText);
        }

        message.setReplyMarkup(markup);

        return message;
    }

    public EditMessageText editResponseMessage(Update update, KeywordsFormat keyword, String textFormat) {
        String chatId = "";
        Integer messageId = 0;

        if (update.getCallbackQuery() != null) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
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


    public SendMessage replyToUser(Update update, KeywordsFormat keyword,String textFormat) {
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

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setReplyToMessageId(update.getMessage().getMessageId());

        switch (textFormat) {
            case "markdown":
                message.setText(replyText);
                message.setParseMode(ParseMode.MARKDOWN);
                break;
            case "markdownV2":
                message.setText(escapeMarkdownV2(replyText));
                message.setParseMode(ParseMode.MARKDOWNV2);
                break;
            case "html":
                message.setText(replyText);
                message.setParseMode(ParseMode.HTML);
                break;
            default:
                message.setText(replyText);
        }

        message.setReplyMarkup(markup);

        return message;
    }


    private String escapeMarkdownV2(String text) {
        String[] symbols = new String[]{"_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!"};

        for (String symbol : symbols) {
            text = text.replace(symbol, "\\" + symbol);
        }

        return text;
    }
}
