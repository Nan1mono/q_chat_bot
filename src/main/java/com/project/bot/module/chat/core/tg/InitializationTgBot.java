package com.project.bot.module.chat.core.tg;

import com.project.bot.module.chat.core.exception.ChatCoreException;
import com.project.bot.module.chat.core.tg.bot.NanimonoBot;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class InitializationTgBot {

    @Value("${chat.telegram.bot-token:your-tg-bot-token}")
    private String botToken;


    @PostConstruct
    public void init(){
        try {
            DefaultBotOptions botOptions = new DefaultBotOptions();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            DefaultBotOptions options = new DefaultBotOptions();
            botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            botOptions.setProxyHost("127.0.0.1");
            botOptions.setProxyPort(7890);
            botsApi.registerBot(new NanimonoBot(options, botToken));
            System.out.println("机器人已启动！");
        } catch (TelegramApiException e) {
            throw new ChatCoreException(e);
        }
    }

}
