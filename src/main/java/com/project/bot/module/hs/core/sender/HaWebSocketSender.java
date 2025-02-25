package com.project.bot.module.hs.core.sender;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.project.bot.module.chat.pojo.entity.HomeAssistantHandshake;
import com.project.bot.module.chat.serivice.HomeAssistantHandshakeService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HaWebSocketSender {

    @Value("${home-assistant.token}")
    private String token;

    private final WebSocketOutboundMessageHandler messageHandler;

    private final HomeAssistantHandshakeService homeAssistantHandshakeService;


    @Autowired
    public HaWebSocketSender(WebSocketOutboundMessageHandler messageHandler,
                             HomeAssistantHandshakeService homeAssistantHandshakeService) {
        this.messageHandler = messageHandler;
        this.homeAssistantHandshakeService = homeAssistantHandshakeService;
    }

    @ServiceActivator(inputChannel = "webSocketOutboundChannel")
    public void sendToWebSocket(@Payload String payload,
                                @Header(name = "sessionMethod", required = false) String sessionMethod,
                                @Header(name = "homeAssistantUserId", required = false) Long homeAssistantUserId,
                                @Header(name = "chatId", required = false) String chatId) {
        JSONObject object = JSON.parseObject(payload);
        Message<String> message = MessageBuilder.withPayload(payload)
                .build();
        HomeAssistantHandshake handshake = new HomeAssistantHandshake()
                .setChatId(chatId)
                .setPayloadId(object.getString("id"))
                .setSessionMethod(sessionMethod)
                .setHomeAssistantUserId(homeAssistantUserId);
        messageHandler.handleMessage(message);
        homeAssistantHandshakeService.save(handshake);
    }

    @PostConstruct
    public void auth() {
        messageHandler.handleMessage(MessageBuilder.withPayload(sendAuthMessage().toJSONString()).build());
    }

    protected JSONObject sendAuthMessage() {
        JSONObject authMessage = new JSONObject();
        authMessage.put("type", "auth");
        authMessage.put("access_token", token);
        return authMessage;
    }
}
