package com.project.bot.module.chat.core.tg.bot;

import com.project.bot.module.chat.core.ernie.BaiduErnieService;
import com.project.bot.module.chat.core.exception.ChatCoreException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@NoArgsConstructor
public class NanimonoBot extends TelegramLongPollingBot {

    private BaiduErnieService baiduErnieService;

    @Autowired
    public void setBaiduErnieService(BaiduErnieService baiduErnieService){
        this.baiduErnieService = baiduErnieService;
    }


    @Value("${chat.telegram.bot-name:your-tg-bot-name}")
    private String botName;

    public NanimonoBot(DefaultBotOptions options, String botToken) {
        super(options, botToken); // 调用父类的构造方法
    }



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            String response = baiduErnieService.chat(messageText);
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
        return botName;
    }

}
