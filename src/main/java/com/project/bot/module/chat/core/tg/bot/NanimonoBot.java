package com.project.bot.module.chat.core.tg.bot;

import com.project.bot.module.chat.core.ernie.BaiduErnieService;
import com.project.bot.module.chat.core.exception.ChatCoreException;
import com.project.bot.module.chat.pojo.entity.HomeAssistantUser;
import com.project.bot.module.chat.serivice.HomeAssistantHandshakeService;
import com.project.bot.module.chat.serivice.HomeAssistantUserService;
import com.project.bot.module.hs.HaSocketTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

    private HaSocketTemplate haSocketTemplate;

    private HomeAssistantUserService homeAssistantUserService;

    private HomeAssistantHandshakeService homeAssistantHandshakeService;

    public NanimonoBot(DefaultBotOptions options, String botToken, String botName) {
        super(options, botToken);
        this.botName = botName;
    }

    public NanimonoBot setService(BaiduErnieService baiduErnieService,
                                  HaSocketTemplate haSocketTemplate,
                                  HomeAssistantUserService homeAssistantUserService,
                                  HomeAssistantHandshakeService homeAssistantHandshakeService) {
        this.baiduErnieService = baiduErnieService;
        this.haSocketTemplate = haSocketTemplate;
        this.homeAssistantUserService = homeAssistantUserService;
        this.homeAssistantHandshakeService = homeAssistantHandshakeService;
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
            // 查询是否配置了指定用户
            HomeAssistantUser homeAssistantUser = homeAssistantUserService.getByTelegramId(fromUser.getId());
            if (ObjectUtils.isNotEmpty(homeAssistantUser)) {
                // 发送webSocket请求，后续结果将从haSocketListener中接受并完成回调
                haSocketTemplate.searchDevice("telegram", homeAssistantUser.getId(), message.getChatId());
            } else {
                response = baiduErnieService.chat(messageText);
            }
            message.setText(response);
            try {
                if (StringUtils.isNotBlank(response)) {
                    message.setText(response);
                    execute(message);
                }
            } catch (TelegramApiException e) {
                throw new ChatCoreException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    public void sendMessageToChat(String message, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            if (StringUtils.isNotBlank(message)) {
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            throw new ChatCoreException(e);
        }
    }

}
