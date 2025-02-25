package com.project.bot.module.chat.core.tg.bot;

import com.project.bot.module.chat.core.ernie.BaiduErnieService;
import com.project.bot.module.chat.core.exception.ChatCoreException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class NanimonoBot extends TelegramLongPollingBot {

    private final String botName;

    private BaiduErnieService baiduErnieService;

    public NanimonoBot(DefaultBotOptions options, String botToken, String botName) {
        super(options, botToken);
        this.botName = botName;
    }

    public NanimonoBot setService(BaiduErnieService baiduErnieService) {
        this.baiduErnieService = baiduErnieService;
        return this;
    }



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            String messageText = update.getMessage().getText();
            log.info("tg bot receive message: {}", messageText);
            message.setChatId(chatId);
            User fromUser = update.getMessage().getFrom();
            String response = "";
            if (fromUser.getId() == 6871508569L){
                response = "朱 玉坤，您好！我是您的智能家居控制机器人";
            }else {
                response = baiduErnieService.chat(messageText);
            }
            message.setText(response);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new ChatCoreException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }
}
