package com.project.bot.module.hs.core.sender;

import com.project.bot.module.chat.serivice.HomeAssistantHandshakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HaWebSocketSender {

    private final WebSocketOutboundMessageHandler messageHandler;

    private final HomeAssistantHandshakeService homeAssistantHandshakeService;

    @Autowired
    public HaWebSocketSender(WebSocketOutboundMessageHandler messageHandler,
                             HomeAssistantHandshakeService homeAssistantHandshakeService) {
        this.messageHandler = messageHandler;
        this.homeAssistantHandshakeService = homeAssistantHandshakeService;
    }

    @ServiceActivator(inputChannel = "webSocketOutboundChannel")
    public void sendToWebSocket(@Payload String payload) {
//        Message<String> message = MessageBuilder.withPayload(payload).build();
//        UUID id = message.getHeaders().getId();
//        assert id != null;
//        HomeAssistantHandshake handshake = new HomeAssistantHandshake()
//                .setChatId(chatId)
//                .setSessionId(id.toString())
//                .setSessionMethod(sessionMethod)
//                .setHomeAssistantUserId(homeAssistantUserId);
        messageHandler.handleMessage(MessageBuilder.withPayload(payload).build());
//        homeAssistantHandshakeService.save(handshake);
    }
}
