package com.project.bot.module.chat.core.tg;

import com.project.bot.module.chat.core.ernie.BaiduErnieService;
import com.project.bot.module.chat.core.exception.ChatCoreException;
import com.project.bot.module.chat.core.tg.bot.NanimonoBot;
import com.project.bot.module.chat.serivice.HomeAssistantUserService;
import com.project.bot.module.ha.HaSocketTemplate;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class InitializationTgBot {

    private final BaiduErnieService baiduErnieService;

    private final HaSocketTemplate haSocketTemplate;

    private final HomeAssistantUserService homeAssistantUserService;

    @Value("${chat.telegram.bot-token:your-tg-bot-token}")
    private String botToken;

    @Value("${chat.telegram.bot-name:your-tg-bot-name}")
    private String botName;

    private NanimonoBot nanimonoBot;

    @Autowired
    public InitializationTgBot(BaiduErnieService baiduErnieService,
                               HaSocketTemplate haSocketTemplate,
                               HomeAssistantUserService homeAssistantUserService) {
        this.baiduErnieService = baiduErnieService;
        this.haSocketTemplate = haSocketTemplate;
        this.homeAssistantUserService = homeAssistantUserService;
    }


    @PostConstruct
    public void init() {
        try {
            DefaultBotOptions botOptions = new DefaultBotOptions();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            DefaultBotOptions options = new DefaultBotOptions();
            botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            botOptions.setProxyHost("127.0.0.1");
            botOptions.setProxyPort(7890);
            nanimonoBot = new NanimonoBot(options, botToken, botName)
                    .setService(
                            baiduErnieService,
                            haSocketTemplate,
                            homeAssistantUserService
                    );
            botsApi.registerBot(nanimonoBot);
        } catch (TelegramApiException e) {
            throw new ChatCoreException(e);
        }
    }

    public NanimonoBot getNaimonoBot() {
        return nanimonoBot;
    }

}
