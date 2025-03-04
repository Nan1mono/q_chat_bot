package com.project.bot.module.ha.core.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.project.bot.module.chat.core.qq.impl.GroupMessageService;
import com.project.bot.module.chat.core.tg.InitializationTgBot;
import com.project.bot.module.chat.core.tg.bot.NanimonoBot;
import com.project.bot.module.chat.pojo.entity.HomeAssistantHandshake;
import com.project.bot.module.chat.pojo.entity.HomeAssistantUser;
import com.project.bot.module.chat.pojo.vo.qq.group.QGroupMessage;
import com.project.bot.module.chat.serivice.HomeAssistantHandshakeService;
import com.project.bot.module.chat.serivice.HomeAssistantUserService;
import com.project.bot.module.ha.HaSocketTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@Slf4j
@Configuration
public class HaWebSocketListener {

    private final HomeAssistantHandshakeService homeAssistantHandshakeService;

    private final HomeAssistantUserService homeAssistantUserService;

    private final InitializationTgBot initializationTgBot;

    private final HaSocketTemplate haSocketTemplate;

    private final GroupMessageService groupMessageService;

    @Autowired
    public HaWebSocketListener(HomeAssistantHandshakeService homeAssistantHandshakeService,
                               HomeAssistantUserService homeAssistantUserService,
                               InitializationTgBot initializationTgBot,
                               HaSocketTemplate haSocketTemplate,
                               GroupMessageService groupMessageService) {
        this.homeAssistantHandshakeService = homeAssistantHandshakeService;
        this.homeAssistantUserService = homeAssistantUserService;
        this.initializationTgBot = initializationTgBot;
        this.haSocketTemplate = haSocketTemplate;
        this.groupMessageService = groupMessageService;
    }





    @ServiceActivator(inputChannel = "webSocketInboundChannel")
    public void listen(Message<String> message) {
        log.info("receive web socket message: {}", message);
        JSONObject payload = JSON.parseObject(message.getPayload());
        if ("auth_required".equals(payload.getString("type"))) {
            log.info("auth request is send...");
            return;
        } else if ("auth_ok".equals(payload.getString("type"))) {
            log.info("auth ok");
            return;
        } else if ("false".equals(payload.getString("success"))) {
            return;
        }
        if (!"true".equals(payload.getString("success"))) {
            log.info("web socket message error: {}", payload);
            return;
        }
        String payloadId = payload.getString("id");
        HomeAssistantHandshake handshake = homeAssistantHandshakeService.getByPayloadId(payloadId);
        if (handshake == null) {
            log.error("handshake not found");
            return;
        }
        HomeAssistantUser homeAssistantUser = homeAssistantUserService.getById(handshake.getHomeAssistantUserId());
        if (handshake.getSessionMethod().equals("telegram")) {
            String response = haSocketTemplate.filterDeviceListFromTelegram(message.getPayload(), homeAssistantUser.getTelegramId());
            NanimonoBot bot = initializationTgBot.getNaimonoBot();
            bot.sendMessageToChat(response, handshake.getChatId());
        }else {
            String response = haSocketTemplate.filterDeviceListFromQq(message.getPayload(), homeAssistantUser.getQqId());
            groupMessageService.sendMessage(QGroupMessage.buildQGroupMsg(response).toGroup(handshake.getChatId()));
        }
    }

}
